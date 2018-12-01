import core.Sparrow;
import mvc.Router;
import mvc.View;

public class LoginPage {
    private static final String USER_NAME = "yang";
    private static final String PASS_WORD = "400820";

    public static void main(String[] args) {
        Router.post("/loginCheck", model -> {
            if (model.get("username").equals(USER_NAME) && model.get("password").equals(PASS_WORD)) {
                return View.ok();
            }
            return View.error();
        });
        Sparrow.fly();
    }
}
