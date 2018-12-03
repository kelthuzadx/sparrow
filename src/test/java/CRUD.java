import database.DBTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUD {
    public static void main(String[] args) throws SQLException {
        DBTemplate template = new DBTemplate();
        ResultSet re = template.query("select * from videohub_user");
        if (re.next()) {
            System.out.println(re.getString("username"));
            System.out.println(re.getString("email"));
        }
    }
}
