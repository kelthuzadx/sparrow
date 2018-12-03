import database.DBTemplate;

public class CRUD {
    public static void main(String[] args) {
        // !!NOTE THAT THIS DEMO MIGHT NOT WORK CORRECTLY SINCE YOU HAVEN'T PREPARED TABLE AND ROW INGREDIENT!!
        DBTemplate.queryOne("select * from videohub_user where id=1", result -> {
            System.out.println(result.getString("username"));
            System.out.println(result.getString("email"));
            System.out.println(result.getString("password"));
        });

        DBTemplate.queryList("select * from videohub_resource", result -> {
            System.out.println(result.getString("video_title"));
            System.out.println(result.getString("video_file_name"));
        });
    }
}
