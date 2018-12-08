import tool.MailWorker;

public class SendEmail {
    public static void main(String[] args) {
        // configure sender mail and authentication code in sparrow.properties firstly
        new MailWorker().sendMailAsync("hello world", "this mail was sent by java program", "1948638989@qq.com");
        System.out.println("done");
    }
}
