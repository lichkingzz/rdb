package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class DeleteRunner {

    static final Log log = LogFactory.getLog(DeleteRunner.class);

    final Map<String, String> cache = new ConcurrentHashMap<String, String>();
    final DeleteInfo deleteInfo;

    DeleteRunner(DeleteInfo deleteInfo) {
        this.deleteInfo = deleteInfo;
    }

    public String dryRun(boolean includeParams) {
        return generateSQL(includeParams);
    }

    public void run() {
        if (this.deleteInfo.beanClass != null) {
            deleteBeans();
        }
        else {
            deleteTable();
        }
    }

    void deleteBeans() {
        String sql = generateSQLForBean();
        Connection conn = null;
        PreparedStatement ps = null;
        boolean shouldCloseConn = this.deleteInfo.rdb.getDataSourceManager().shouldCloseConnection();
        Property prop = this.deleteInfo.beanMapper.getProperty(this.deleteInfo.primaryKey);
        try {
            conn = this.deleteInfo.rdb.getDataSourceManager().getConnection();
            ps = conn.prepareStatement(sql);
            for (Object bean : this.deleteInfo.beans) {
                Object value = prop.getProperty(bean);
                if (value !=null && !prop.isSimpleType()) {
                    value = this.deleteInfo.rdb.javaTypeToJdbcType(prop.getPropertyType(), value);
                }
                ps.setObject(1, value);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataException(e);
        }
        finally {
            SQLUtils.close(ps);
            SQLUtils.close(conn, shouldCloseConn);
        }
    }

    String generateSQLForBean() {
        String key = this.deleteInfo.beanClass.getName();
        String sql = cache.get(key);
        if (sql == null) {
            sql = generateDeleteSQLForBean(this.deleteInfo.beanClass);
            log.info("Generate DELETE SQL for class: " + key + ", SQL: " + sql);
            cache.put(key, sql);
        }
        return sql;
    }

    String generateDeleteSQLForBean(Class<?> beanClass) {
        BeanMapper mapper = Mappers.getMapper(beanClass);
        StringBuilder sb = new StringBuilder(128);
        sb.append("DELETE FROM ")
          .append(mapper.table)
          .append(" WHERE ")
          .append(mapper.primaryKey)
          .append("=?");
        return sb.toString();
    }

    void deleteTable() {
        String sql = generateSQL(false);
        Object[] args = this.deleteInfo.whereArgs;
        Connection conn = null;
        PreparedStatement ps = null;
        boolean shouldCloseConn = this.deleteInfo.rdb.getDataSourceManager().shouldCloseConnection();
        try {
            conn = this.deleteInfo.rdb.getDataSourceManager().getConnection();
            ps = conn.prepareStatement(sql);
            SQLUtils.setPreparedStatementParameters(ps, args);
            ps.executeUpdate();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataException(e);
        }
        finally {
            SQLUtils.close(ps);
            SQLUtils.close(conn, shouldCloseConn);
        }
    }

    String generateSQL(boolean includeParams) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("DELETE FROM ")
          .append(this.deleteInfo.table);
        if (this.deleteInfo.whereClause != null) {
            sb.append(" WHERE ")
              .append(this.deleteInfo.whereClause);
        }
        String sql = sb.toString();
        if (includeParams) {
            Object[] args = this.deleteInfo.whereArgs;
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

}
