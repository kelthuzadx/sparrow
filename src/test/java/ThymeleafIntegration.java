import core.Sparrow;
import mvc.Router;
import mvc.View;

import java.io.IOException;

public class ThymeleafIntegration {
    public static void main(String[] args) {
        Router.get("/ht", (req, resp) -> {
            try {
                resp.getWriter().println("hello thymeleaf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Router.get("/hello", model -> View.create("/ht"));
        Router.get("/myhome", model -> {
            model.set("welcome", "client");
            return View.create("home.html", model);
        });
        Router.get("/login", model -> View.create("login.jsp"));
        Sparrow.fly();
    }
}
