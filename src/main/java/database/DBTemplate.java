package database;

import core.PropertiesHolder;
import core.Sparrow;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

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
            logger.error("retrieved result set from database but can get one row data");
        }
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
                pr.close();
                return object;
            }
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can get one row data");
        } catch (IllegalAccessException | InstantiationException e) {
            logger.error("retrieved result set from database but can map columns to specific object fields");
        }
        return null;
    }

    public static <T> ArrayList<T> queryList(String sql, Class<T> type) {
        PreparedStatement pr = getPreparedStatement(sql);
        ResultSet rs = getResultSet(pr);

        try {
            ArrayList<T> list = new ArrayList<>();

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
            pr.close();
            return list;
        } catch (SQLException e) {
            logger.error("retrieved result set from database but can not get multi row data");
        } catch (IllegalAccessException | InstantiationException e) {
            logger.error("retrieved result set from database but can map columns to specific object fields");
        }
        return null;
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
            logger.error("retrieved result set from database but can not get multi row data");
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