import database.DBTemplate;
import domain.User;

import java.util.List;

public class CRUD {
    public static void main(String[] args) {
        // !!NOTE THAT THIS DEMO MIGHT NOT WORK CORRECTLY SINCE YOU HAVEN'T PREPARED TABLE AND DATA INGREDIENTS!!
        DBTemplate.queryOne("select * from videohub_user where id=1", result -> {
            System.out.println(result.getString("username"));
            System.out.println(result.getString("email"));
            System.out.println(result.getString("password"));
        });

        DBTemplate.queryList("select * from videohub_resource", result -> {
            System.out.println(result.getString("video_title"));
            System.out.println(result.getString("video_file_name"));
        });

        User user = DBTemplate.queryOne("select * from videohub_user where id=1", User.class);
        System.out.println(user);
        List<User> userList = DBTemplate.queryList("select * from videohub_user", User.class);
        System.out.println(userList);


        DBTemplate.queryOne(
                "select * from videohub_user where id=? and username=? and password=?",
                new Object[]{1, "yang", "123"},
                result -> {
                    System.out.println(result.getString("username"));
                    System.out.println(result.getString("email"));
                    System.out.println(result.getString("password"));
                });
        List<User> userList1 = DBTemplate.queryList(
                "select * from videohub_user where id=? and username=? and password=?",
                new Object[]{1, "yang", "123"},
                User.class);
        System.out.println(userList1);
    }
}
