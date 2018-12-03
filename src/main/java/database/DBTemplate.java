package database;

import core.PropertiesHolder;
import core.Sparrow;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTemplate {
    private static final DBTemplate dbt = new DBTemplate();
    private static final Logger logger = Logger.getLogger(Sparrow.class);
    private Connection con = null;

    private DBTemplate() {
        String driverName = PropertiesHolder.readProp("database.driver-class");
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.error("can not load database driver class" + driverName);
        }
        con = getConnection();
    }

    private static ResultSet getResultSet(String sql) {
        ResultSet rs = null;

        try {
            rs = dbt.con.prepareStatement(sql).executeQuery();
        } catch (SQLException e) {
            logger.error("can not execute " + sql + " due to create statement failure or execution failure");
        }
        return rs;
    }

    public static void queryOne(String sql, Row row) {
        ResultSet rs = getResultSet(sql);

        try {
            if (rs != null && rs.next()) {
                row.getRow(rs);
                rs.close();
            }
        } catch (SQLException e) {
            logger.error("retrived result set from database but can not inspect its internal data");
        }
    }

    public static void queryList(String sql, MultiRow multiRow) {
        ResultSet rs = getResultSet(sql);

        try {
            while (rs.next()) {
                multiRow.getMultiRow(rs);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("retrived result set from database but can not inspect its internal data");
        }
    }

    private Connection getConnection() {
        String url = PropertiesHolder.readProp("database.url");
        String username = PropertiesHolder.readProp("database.username");
        String password = PropertiesHolder.readProp("database.password");

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("can not get connection by " + url + "with " + username + "@" + password);
        }

        return con;
    }
}
