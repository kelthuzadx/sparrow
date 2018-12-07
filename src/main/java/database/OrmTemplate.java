package database;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrmTemplate {
    private static final Logger logger = Logger.getLogger(DBTemplate.class);

    public static <T> T queryOne(String sql, Class<T> type) {
        return DBTemplate.crudImpl(sql, (resultSet, affectRows) -> {
            T retVal = null;
            try {
                retVal = type.newInstance();
                if (resultSet != null) {
                    resultSet.next();
                    Field[] fs = type.getDeclaredFields();
                    for (Field f : fs) {
                        f.setAccessible(true);
                        f.set(retVal, resultSet.getObject(f.getName()));
                    }
                }
            } catch (SQLException e) {
                logger.error("can not perform user callback: " + e.getMessage());
            } catch (IllegalAccessException | InstantiationException e) {
                logger.error("can not instantiate object by given type");
            }
            return retVal;
        });
    }

    public static <T> ArrayList<T> queryList(String sql, Class<T> type) {
        return DBTemplate.crudImpl(sql, (resultSet, affectRows) -> {
            ArrayList<T> retVal = new ArrayList<>();
            try {
                if (resultSet != null) {

                    while (resultSet.next()) {
                        T val = type.newInstance();
                        Field[] fs = type.getDeclaredFields();
                        for (Field f : fs) {
                            f.setAccessible(true);
                            f.set(val, resultSet.getObject(f.getName()));
                        }
                        retVal.add(val);
                    }
                }
            } catch (SQLException e) {
                logger.error("can not perform user callback: " + e.getMessage());
            } catch (IllegalAccessException | InstantiationException e) {
                logger.error("can not instantiate object by given type");
            }
            return retVal;
        });
    }
}
