# Database template
Sparrow database template provides a simple way to do CRUD works
 without caring about connection getting and resource releasing. Public APIs of it are as follows:
```java
public class DBTemplate{
    public static void query(String sql, Row row);

    public static int insert(String sql);

    public static int delete(String sql);

    public static int update(String sql);

    public static void query(String sql, Object[] params, Row row);

    public static int insert(String sql, Object[] params);

    public static int delete(String sql, Object[] params);

    public static int update(String sql, Object[] params);
```
```java
public class OrmTemplate{
    // Get one row/multi rows by given sql and map result columns to specif domain type
    public static <T> T queryOne(String sql, Class<T> type);
    public static <T> T queryOne(String sql, Object[] params, Class<T> type);

    public static <T> ArrayList<T> queryList(String sql, Object[] params, Class<T> type);

    public static <T> ArrayList<T> queryList(String sql, Class<T> type);
}
```