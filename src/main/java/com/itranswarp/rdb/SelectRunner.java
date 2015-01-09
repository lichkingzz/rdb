package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SelectRunner {

    static final List<String> SELECT_ALL = Arrays.asList("*");

    static final Object[] EMPTY_ARGS = new Object[0];

    final SelectInfo selectInfo;

    SelectRunner(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public String dryRun() {
        return generateSQL(false);
    }

    public String dryRun(boolean includeParams) {
        return generateSQL(includeParams);
    }

    @SuppressWarnings("unchecked")
    public <T> T first() {
        return (T) getResultSet(true, false);
    }

    @SuppressWarnings("unchecked")
    public <T> T unique() {
        return (T) getResultSet(true, true);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list() {
        return (List<T>) getResultSet(false, false);
    }

    Object getResultSet(boolean firstRow, boolean uniqueRow) {
        String sql = generateSQL(false);
        Object[] args = generateArgs();
        boolean shouldCloseConn = this.selectInfo.rdb.dataSourceManager.shouldCloseConnection();
        Connection conn = this.selectInfo.rdb.dataSourceManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            int n = 0;
            for (Object arg : args) {
                n ++;
                ps.setObject(n, arg);
            }
            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            String[] fields = new String[meta.getColumnCount()];
            int[] sqlTypes = new int[meta.getColumnCount()];
            for (int i=0; i<fields.length; i++) {
                fields[i] = meta.getColumnLabel(i+1);
                sqlTypes[i] = meta.getColumnType(i+1);
            }
            return mapRows(firstRow, uniqueRow, rs, fields);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataException(e);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {}
            }
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException e) {}
            }
            if (conn != null && shouldCloseConn) {
                try {
                    conn.close();
                }
                catch (SQLException e) {}
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    Object mapRows(boolean firstRow, boolean uniqueRow, ResultSet rs, String[] fields) throws Exception {
        if (firstRow) {
            if (!rs.next()) {
                if (uniqueRow) {
                    throw new EmptyRowException("Empty row fetched.");
                }
                return null;
            }
            Object r = mapRow(rs, fields);
            if (uniqueRow && rs.next()) {
                throw new NonUniqueRowException("Multiple rows fetched.");
            }
            return r;
        }
        List list = new ArrayList();
        while (rs.next()) {
            list.add(mapRow(rs, fields));
        }
        return list;
    }

    Object mapRow(ResultSet rs, String[] fields) throws Exception {
        if (this.selectInfo.beanClass == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i=0; i<fields.length; i++) {
                map.put(fields[i], rs.getObject(i+1));
            }
            return map;
        }
        Object bean = this.selectInfo.beanClass.newInstance();
        BeanMapper mapper = Mappers.getMapper(this.selectInfo.beanClass);
        for (int i=0; i<fields.length; i++) {
            String key = fields[i];
            Object value = rs.getObject(i + 1);
            Property prop = mapper.getProperty(key);
            if (prop == null) {
                throw new MappingException("There is no property \"" + key + "\" found in class: " + this.selectInfo.beanClass.getName());
            }
            if (!prop.isSimpleType()) {
                Class<?> type = prop.getPropertyType();
                if (value != null && !type.isInstance(value)) {
                    // convert value to property type:
                    value = this.selectInfo.rdb.convertTo(type, value);
                }
            }
            prop.setProperty(bean, value);
        }
        return bean;
    }

    String generateSQL(boolean includeParams) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT ");
        for (String field : generateSelectFields()) {
            sb.append(field).append(", ");
        }
        // remove last ", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" FROM ")
          .append(selectInfo.table);
        if (selectInfo.whereClause != null) {
            sb.append(" WHERE ")
              .append(selectInfo.whereClause);
        }
        if (selectInfo.orderBys != null) {
            sb.append(" ORDER BY");
            for (OrderByInfo orderBy : selectInfo.orderBys) {
                sb.append(' ').append(orderBy.field);
                if (orderBy.desc) {
                    sb.append(" DESC");
                }
                sb.append(",");
            }
            // remove last ",":
            sb.deleteCharAt(sb.length()-1);
        }
        if (selectInfo.limit != null) {
            sb.append(" LIMIT ?, ?");
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
        if (selectInfo.whereArgs == null) {
            if (selectInfo.limit == null) {
                return EMPTY_ARGS;
            }
            else {
                return new Object[] { selectInfo.limit[0], selectInfo.limit[1] };
            }
        }
        else {
            if (selectInfo.limit == null) {
                return selectInfo.whereArgs;
            }
            else {
                Object[] args = new Object[selectInfo.whereArgs.length + 2];
                System.arraycopy(selectInfo.whereArgs, 0, args, 0, selectInfo.whereArgs.length);
                args[args.length-2] = selectInfo.limit[0];
                args[args.length-1] = selectInfo.limit[1];
                return args;
            }
        }
    }

    List<String> generateSelectFields() {
        if (selectInfo.fields != null) {
            // manually set "select a, b, c":
            return selectInfo.fields;
        }
        if (selectInfo.excludeFields == null) {
            // return "select *"
            return SELECT_ALL;
        }
        // find bean properties and exclude some fields:
        Set<String> names = this.selectInfo.beanMapper.getProperyNames();
        for (String s : selectInfo.excludeFields) {
            if (!names.remove(s)) {
                throw new IllegalArgumentException("There is no field named \"" + s + "\" to be excluded.");
            }
        }
        if (names.isEmpty()) {
            throw new IllegalArgumentException("All fields are excluded.");
        }
        return new ArrayList<String>(names);
    }
}
