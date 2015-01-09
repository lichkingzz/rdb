package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

class InsertT {

    InsertInfo insertInfo;

    InsertT(InsertInfo insertInfo, Object[] beans) {
        insertInfo.beans = beans;
        this.insertInfo = insertInfo;
    }

    public void run() {
        new InsertRun(this.insertInfo).run();
    }
}

class InsertInfo {

    final Rdb rdb;
    String table = null;
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

    public int run() {
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
            return ps.executeUpdate();
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
