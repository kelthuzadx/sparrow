import core.Sparrow;
import mvc.Router;
import tool.CaptchaGenerator;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public class CaptchaGenerating {
    public static void main(String[] args) {
        Router.get("/code", (request, response) -> {
            try {
                response.setContentType("image/jpeg");
                // simple 4 chars captcha
                CaptchaGenerator cg = CaptchaGenerator.simpleCaptcha(4);
                System.out.println(cg.getCaptchaCode());
                byte[] imgBinary = cg.getByteImage();
                ServletOutputStream sos = response.getOutputStream();
                sos.write(imgBinary);
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Sparrow.fly();
    }
}
