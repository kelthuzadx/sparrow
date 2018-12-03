package database;

import core.PropertiesHolder;
import core.Sparrow;
import org.apache.log4j.Logger;

import java.sql.*;

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

    private static PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement pr = null;
        try {
            pr = dbt.con.prepareStatement(sql);
        } catch (SQLException e) {
            logger.error("can not get prepared statement due to internal problem");
        }
        return pr;
    }

    private static ResultSet getResultSet(PreparedStatement pr) {
        ResultSet rs = null;
        try {
            rs = pr.executeQuery();
        } catch (SQLException e) {
            logger.error("can not get result set");
        }
        return rs;
    }

    public static void queryOne(String sql, Row row) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            if (rs != null && rs.next()) {
                row.getRow(rs);
                rs.close();
                pr.close();
            }
        } catch (SQLException e) {
            logger.error("retrived result set from database but can not inspect its internal data");
        }
    }

    public static void queryList(String sql, MultiRow multiRow) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            while (rs.next()) {
                multiRow.getMultiRow(rs);
            }
            rs.close();
            pr.close();
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
