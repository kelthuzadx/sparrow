package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Row {
    void getRow(ResultSet rs) throws SQLException;
}
