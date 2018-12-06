package database;

import core.PropertiesHolder;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

public class DBTemplate {
    private static final Logger logger = Logger.getLogger(DBTemplate.class);

    private static final DBTemplate dbt = new DBTemplate();

    static {
        BasicConfigurator.configure();
    }
    private Connection con = null;

    private DBTemplate() {
        String driverName = PropertiesHolder.readProp("database.driver-class");
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.error("can not load database driver class" + driverName);
        }
    }

    private static PreparedStatement getPreparedStatement(String sql) {
        dbt.con = dbt.getConnection();

        PreparedStatement pr = null;
        try {
            pr = dbt.con.prepareStatement(sql);
            logger.debug("executing sql: " + sql);
        } catch (SQLException e) {
            logger.error("can not get prepared statement");
        }
        return pr;
    }

    private static ResultSet getResultSet(PreparedStatement pr) {
        ResultSet rs = null;
        try {
            rs = pr.executeQuery();
        } catch (SQLException e) {
            logger.error("can not get result set after sql executed");
        }
        return rs;
    }

    private static String fillSqlPlaceholder(String oldSql, Object[] params) {
        String concatedSql = oldSql;
        for (Object param : params) {
            if (param instanceof String || param instanceof Date) {
                concatedSql = concatedSql.replaceFirst("\\?", "'" + param + "'");
            } else {
                concatedSql = concatedSql.replaceFirst("\\?", "" + param);
            }
        }
        return concatedSql;
    }

    public static void query(String sql, AllRows alls) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            alls.getResultSet(rs);
            if (rs.next()) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can get one row data");
        } finally {
            try {
                pr.close();
            } catch (SQLException e) {
                logger.error("failed to close prepared statement resource");
            }
            try {
                dbt.con.close();
            } catch (SQLException e) {
                logger.error("failed to close database connection");
            }
        }
    }

    public static void queryOne(String sql, Object[] params, Row row) {
        queryOne(fillSqlPlaceholder(sql, params), row);
    }

    public static void queryOne(String sql, Row row) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            if (rs != null && rs.next()) {
                row.getRow(rs);
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can get one row data");
        } finally {
            try {
                pr.close();
            } catch (SQLException e) {
                logger.error("failed to close prepared statement resource");
            }
            try {
                dbt.con.close();
            } catch (SQLException e) {
                logger.error("failed to close database connection");
            }
        }
    }

    public static <T> T queryOne(String sql, Object[] params, Class<T> type) {
        return queryOne(fillSqlPlaceholder(sql, params), type);
    }

    public static <T> T queryOne(String sql, Class<T> type) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            if (rs != null && rs.next()) {
                T object = type.newInstance();

                Field[] fs = type.getDeclaredFields();
                for (Field f : fs) {
                    f.setAccessible(true);
                    f.set(object, rs.getObject(f.getName()));
                }
                rs.close();

                return object;
            }
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can get one row data");
        } catch (IllegalAccessException | InstantiationException e) {
            logger.error("retrieved result set from database but can map columns to specific object fields");
        } finally {
            try {
                pr.close();
            } catch (SQLException e) {
                logger.error("failed to close prepared statement resource");
            }
            try {
                dbt.con.close();
            } catch (SQLException e) {
                logger.error("failed to close database connection");
            }
        }
        return null;
    }

    public static <T> ArrayList<T> queryList(String sql, Class<T> type) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            ArrayList<T> list = new ArrayList<>();
            if (rs != null) {
                while (rs.next()) {
                    T object = type.newInstance();
                    Field[] fs = type.getDeclaredFields();
                    for (Field f : fs) {
                        f.setAccessible(true);
                        f.set(object, rs.getObject(f.getName()));
                    }
                    list.add(object);
                }
                rs.close();
            } else {
                logger.debug("empty result set returned for sql " + sql);
            }
            return list;
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can not get multi row data");
        } catch (IllegalAccessException | InstantiationException e) {
            logger.error("retrieved result set from database but can map columns to specific object fields");
        } finally {
            try {
                pr.close();
            } catch (SQLException e) {
                logger.error("failed to close prepared statement resource");
            }
            try {
                dbt.con.close();
            } catch (SQLException e) {
                logger.error("failed to close database connection");
            }
        }
        return null;
    }

    public static <T> ArrayList<T> queryList(String sql, Object[] params, Class<T> type) {
        return queryList(fillSqlPlaceholder(sql, params), type);
    }

    public static void queryList(String sql, MultiRow multiRow) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            if (rs != null) {
                while (rs.next()) {
                    multiRow.getMultiRow(rs);
                }
                rs.close();
            } else {
                logger.debug("empty result set returned for sql " + sql);
            }
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can not get multi row data");
        } finally {
            try {
                pr.close();
            } catch (SQLException e) {
                logger.error("failed to close prepared statement resource");
            }
            try {
                dbt.con.close();
            } catch (SQLException e) {
                logger.error("failed to close database connection");
            }
        }
    }

    public static void queryList(String sql, Object[] params, MultiRow multiRow) {
        queryList(fillSqlPlaceholder(sql, params), multiRow);
    }

    private Connection getConnection() {
        String url = PropertiesHolder.readProp("database.url");
        String username = PropertiesHolder.readProp("database.username");
        String password = PropertiesHolder.readProp("database.password");

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("can not get connection by " + url + " with " + username + "@" + password);
        }

        return con;
    }
}
