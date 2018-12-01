import core.Sparrow;
import mvc.Router;
import mvc.View;

import java.io.IOException;

public class DefineRouter {
    public static void main(String[] args) {
        Router.get("/a", model -> View.create("home.html"));

        Router.get("/b", model -> View.create("index.jsp"));

        Router.get("/d", model -> {
            model.set("greeting", "hi");
            return View.create("home.html", model);
        });

        Router.get("/c", (req, resp) -> {
            try {
                resp.getWriter().println("<p>rendering page without view resolving</p>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Sparrow.fly();
    }
}
