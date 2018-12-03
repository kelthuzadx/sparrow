package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MultiRow {
    void getMultiRow(ResultSet rs) throws SQLException;
}
