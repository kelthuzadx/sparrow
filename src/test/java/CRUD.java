import database.DBTemplate;

import java.util.Date;

public class CRUD {
    public static void main(String[] args) {
        // !!NOTE THAT THIS DEMO MIGHT NOT WORK CORRECTLY SINCE YOU HAVEN'T PREPARED TABLE AND DATA INGREDIENTS!!
        int i = DBTemplate.insert("insert into videohub_user values(?,?,?,?,?,?,?)"
                , new Object[]{0, "bot", "123", "test/avatar.png", "admin@gmail.com", 500, new Date()});
        DBTemplate.query("select * from videohub_user where username=?", new Object[]{"bot"}, result -> {
            if (result.next()) {
                System.out.printf("username:%s\temail:%s\tpassword:%s\n",
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("password"));
            }
        });

        int u = DBTemplate.update("update videohub_user set password=? where username=?", new Object[]{"3523523", "bot"});
        DBTemplate.query("select * from videohub_user where username=?", new Object[]{"bot"}, result -> {
            if (result.next()) {
                System.out.printf("username:%s\temail:%s\tpassword:%s\n",
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("password"));
            }
        });
        int d = DBTemplate.delete("delete from videohub_user where username=? and password=?", new Object[]{"bot", "3523523"});
        DBTemplate.query("select * from videohub_user where username=?", new Object[]{"bot"}, result -> {
            if (result.next()) {
                System.out.printf("username:%s\temail:%s\tpassword:%s\n",
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("password"));
            }
        });
        System.out.println("count for inserting/updating/deleting: " + i + "/" + u + "/" + d);
    }
}
