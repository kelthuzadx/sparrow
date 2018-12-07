package database;

import core.PropertiesHolder;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.function.BiFunction;

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

    /*package*/
    static <T> T crudImpl(String sql, BiFunction<ResultSet, Integer, T> function) {
        // prepare statement execute sql
        dbt.con = dbt.getConnection();
        PreparedStatement pr = null;
        ResultSet rs = null;
        T retVal = null;
        try {
            pr = dbt.con.prepareStatement(sql);
            logger.debug("executing sql: " + sql);
        } catch (SQLException e) {
            logger.error("can not get prepared statement");
        }
        try {
            if (pr != null) {
                pr.execute();
            }
        } catch (SQLException e) {
            logger.error("error occurred while performing sql statement: " + e.getMessage());
        }

        // execute user defined logic
        if (function != null) {
            try {
                retVal = function.apply(pr.getResultSet(), pr.getUpdateCount());
            } catch (SQLException e) {
                logger.error("can not get result set from prepared statement");
            }
        }

        // release resources
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("failed to close result set");
        }
        try {
            if (pr != null) {
                pr.close();
            }
        } catch (SQLException e) {
            logger.error("failed to close prepared statement");
        }
        try {
            if (dbt.con != null) {
                dbt.con.close();
            }
        } catch (SQLException e) {
            logger.error("failed to close database connection");
        }
        return retVal;
    }

    private static String fillSqlPlaceholder(String oldSql, Object[] params) {
        String concatedSql = oldSql;
        for (Object param : params) {
            if (param instanceof String || param instanceof java.sql.Date) {
                concatedSql = concatedSql.replaceFirst("\\?", "'" + param + "'");
            } else if (param instanceof java.util.Date) {
                concatedSql = concatedSql.replaceFirst("\\?", "'" +
                        new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(param) + "'");
            } else {
                concatedSql = concatedSql.replaceFirst("\\?", "" + param);
            }
        }
        return concatedSql;
    }

    public static void query(String sql, Row row) {
        crudImpl(sql, (resultSet, affectRows) -> {
            try {
                row.getRow(resultSet);
            } catch (SQLException e) {
                logger.error("can not perform user callback: " + e.getMessage());
            }
            return null;
        });
    }

    public static int insert(String sql) {
        int rows = crudImpl(sql, (resultSet, affectRows) -> affectRows);
        return rows == -1 ? 0 : rows;
    }

    public static int delete(String sql) {
        return crudImpl(sql, (resultSet, affectRows) -> affectRows);
    }

    public static int update(String sql) {
        return crudImpl(sql, (resultSet, affectRows) -> affectRows);
    }

    public static void query(String sql, Object[] params, Row row) {
        query(fillSqlPlaceholder(sql, params), row);
    }

    public static int insert(String sql, Object[] params) {
        int rows = crudImpl(fillSqlPlaceholder(sql, params), (resultSet, affectRows) -> affectRows);
        return rows == -1 ? 0 : rows;
    }

    public static int delete(String sql, Object[] params) {
        return crudImpl(fillSqlPlaceholder(sql, params), (resultSet, affectRows) -> affectRows);
    }

    public static int update(String sql, Object[] params) {
        return crudImpl(fillSqlPlaceholder(sql, params), (resultSet, affectRows) -> affectRows);
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
