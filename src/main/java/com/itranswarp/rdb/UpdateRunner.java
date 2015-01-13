package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class UpdateRunner {

    static final Log log = LogFactory.getLog(UpdateRunner.class);

    static final String[] EMPTY_FIELDS = new String[0];

    final UpdateInfo updateInfo;

    UpdateRunner(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String dryRun(boolean includeParams) {
        return generateSQL(includeParams);
    }

    public void run() {
        if (this.updateInfo.beanClass==null) {
            updateTable();
        }
        else {
            updateBean();
        }
    }

    void updateTable() {
        String sql = generateSQL(false);
        Object[] args = generateArgs();
        Connection conn = null;
        PreparedStatement ps = null;
        boolean shouldCloseConn = this.updateInfo.rdb.getDataSourceManager().shouldCloseConnection();
        try {
            conn = this.updateInfo.rdb.getDataSourceManager().getConnection();
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

    void updateBean() {
        String primaryKey = this.updateInfo.beanMapper.primaryKey;
        Set<String> props = this.updateInfo.beanMapper.getProperyNames();
        String[] fields = null;
        if (this.updateInfo.fieldsOnly != null) {
            for (String f : this.updateInfo.fieldsOnly) {
                if (! props.contains(f)) {
                    throw new IllegalArgumentException("Field \"" + f + "\" not found.");
                }
                if (primaryKey.equals(f)) {
                    log.warn("Update primary key is not recommended.");
                }
            }
            fields = this.updateInfo.fieldsOnly;
        }
        else {
            props.remove(primaryKey);
            fields = props.toArray(EMPTY_FIELDS);
        }
        String sql = generateSQLForBean(fields);
        log.info("EXECUTE SQL: " + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        boolean shouldCloseConn = this.updateInfo.rdb.getDataSourceManager().shouldCloseConnection();
        try {
            this.updateInfo.rdb.beforeUpdate(this.updateInfo.bean);
            Object[] args = generateArgsForBean(fields);
            conn = this.updateInfo.rdb.getDataSourceManager().getConnection();
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

    String generateSQLForBean(String[] fields) {
        String primaryKey = this.updateInfo.beanMapper.primaryKey;
        StringBuilder sb = new StringBuilder(128);
        sb.append("UPDATE ")
          .append(this.updateInfo.beanMapper.table)
          .append(" SET ");
        for (String field : fields) {
            sb.append(field).append("=?, ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" WHERE ")
          .append(primaryKey)
          .append("=?");
        return sb.toString();
    }

    Object[] generateArgsForBean(String[] fields) throws Exception {
        Object[] args = new Object[fields.length + 1];
        for (int i=0; i<fields.length; i++) {
            Property prop = this.updateInfo.beanMapper.getProperty(fields[i]);
            Object value = prop.getProperty(this.updateInfo.bean);
            if (value !=null && !prop.isSimpleType()) {
                value = this.updateInfo.rdb.javaTypeToJdbcType(prop.getPropertyType(), value);
            }
            args[i] = value;
        }
        Property prop = this.updateInfo.beanMapper.getProperty(this.updateInfo.beanMapper.primaryKey);
        args[args.length - 1] = prop.getProperty(this.updateInfo.bean);
        return args;
    }

    String generateSQL(boolean includeParams) {
        StringBuilder sb = new StringBuilder(128);
        sb.append("UPDATE ")
          .append(this.updateInfo.table)
          .append(" SET ");
        for (UpdateSetInfo ui : this.updateInfo.sets) {
            sb.append(ui.field).append("=?, ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        if (this.updateInfo.whereClause != null) {
            sb.append(" WHERE ").append(this.updateInfo.whereClause);
        }
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
        Object[] args = new Object[this.updateInfo.sets.size() + (this.updateInfo.whereArgs==null ? 0 : this.updateInfo.whereArgs.length)];
        int n = 0;
        for (UpdateSetInfo ui : this.updateInfo.sets) {
            args[n] = ui.value;
            n ++;
        }
        if (this.updateInfo.whereArgs!=null) {
            System.arraycopy(this.updateInfo.whereArgs, 0, args, this.updateInfo.sets.size(), this.updateInfo.whereArgs.length);
        }
        return args;
    }

}
