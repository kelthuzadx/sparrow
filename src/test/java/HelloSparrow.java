import core.Sparrow;
import mvc.Router;

import java.io.IOException;

public class HelloSparrow {
    public static void main(String[] args) {
        Router.get("/test", (req, resp) -> {
            try {
                resp.getWriter().println("hello world");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Sparrow.fly();
    }
}
