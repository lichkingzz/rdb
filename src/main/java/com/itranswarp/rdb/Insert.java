package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Insert {

    final InsertInfo insertInfo;

    Insert(InsertInfo insertInfo, String table) {
        insertInfo.table = table;
        this.insertInfo = insertInfo;
    }

    public InsertSet set(String field, Object value) {
        return new InsertSet(this.insertInfo, field, value);
    }

}

class InsertInfo {

    final Rdb rdb;
    String table = null;
    Class<?> beanClass = null;
    Object[] beans = null;
    List<InsertFieldInfo> sets = null;

    InsertInfo(Rdb rdb) {
        this.rdb = rdb;
    }
}

class InsertFieldInfo {

    String field;
    Object value;

    InsertFieldInfo(String field, Object value) {
        this.field = field;
        this.value = value;
    }

}

class InsertRun {

    InsertInfo insertInfo;

    InsertRun(InsertInfo insertInfo) {
        this.insertInfo = insertInfo;
    }

    public String dryRun(boolean includeParams) {
        return generateSQL(includeParams);
    }

    public void run() {
        if (this.insertInfo.beanClass != null) {
            insertBeans();
        }
        else {
            insertTable();
        }
    }

    void insertBeans() {
        Connection conn = null;
        PreparedStatement ps = null;
        Class<?> beanClass = this.insertInfo.beans[0].getClass();
        BeanMapper mapper = Mappers.getMapper(beanClass);
        List<String> fields = new ArrayList<String>(mapper.getProperyNames());
        String sql = generateInsertSQLForBean(mapper.table, fields);
        System.out.println("Generate INSERT: " + sql);
        boolean shouldCloseConn = this.insertInfo.rdb.getDataSourceManager().shouldCloseConnection();
        boolean autoCommit = true;
        try {
            conn = this.insertInfo.rdb.getDataSourceManager().getConnection();
            if (shouldCloseConn) {
                autoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
            }
            ps = conn.prepareStatement(sql);
            for (Object bean : this.insertInfo.beans) {
                Object[] args = prepareBeanPropertyValues(bean, mapper, fields);
                SQLUtils.setPreparedStatementParameters(ps, args);
                ps.addBatch();
            }
            ps.executeBatch();
            if (shouldCloseConn) {
                conn.commit();
            }
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataException(e);
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(autoCommit);
                    conn.close();
                }
                catch (SQLException e) {
                }
            }
        }
    }

    Object[] prepareBeanPropertyValues(Object bean, BeanMapper mapper, List<String> fields) throws Exception {
        Object[] args = new Object[fields.size()];
        int n = 0;
        for (String field: fields) {
            Property prop = mapper.getProperty(field);
            Object value = prop.getProperty(bean);
            if (value !=null && !prop.isSimpleType()) {
                value = this.insertInfo.rdb.javaTypeToJdbcType(prop.getPropertyType(), value);
            }
            args[n] = value;
            n ++;
        }
        return args;
    }

    void insertTable() {
        String sql = generateSQL(false);
        Object[] args = generateArgs();
        Connection conn = null;
        PreparedStatement ps = null;
        boolean shouldCloseConn = this.insertInfo.rdb.getDataSourceManager().shouldCloseConnection();
        try {
            conn = this.insertInfo.rdb.getDataSourceManager().getConnection();
            ps = conn.prepareStatement(sql);
            for (int i=0; i<args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            ps.executeUpdate();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataException(e);
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException e) {
                }
            }
            if (shouldCloseConn && conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                }
            }
        }
    }

    String generateSQL(boolean includeParams) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("INSERT INTO ")
          .append(this.insertInfo.table)
          .append(" (");
        // append each fields:
        for (InsertFieldInfo fi : this.insertInfo.sets) {
            sb.append(fi.field).append(", ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append(") VALUES (");
        // append each ?:
        for (int i=0; i < this.insertInfo.sets.size(); i++) {
            sb.append("?, ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append(')');
        String sql = sb.toString();
        if (includeParams) {
            Object[] args = generateArgs();
            for (Object arg : args) {
                int n = sql.indexOf('?');
                String s = SQLUtils.toArgString(arg);
                sql = sql.substring(0, n) + s + sql.substring(n+1);
            }
        }
        return sql;
    }

    String generateInsertSQLForBean(String table, List<String> fields) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("INSERT INTO ")
          .append(table)
          .append(" (");
        // append each fields:
        for (String field : fields) {
            sb.append(field).append(", ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append(") VALUES (");
        // append each ?:
        for (int i=0; i < fields.size(); i++) {
            sb.append("?, ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append(')');
        return sb.toString();
    }

    Object[] generateArgs() {
        Object[] args = new Object[this.insertInfo.sets.size()];
        int n = 0;
        for (InsertFieldInfo fi : this.insertInfo.sets) {
            args[n] = fi.value;
            n ++;
        }
        return args;
    }

}
