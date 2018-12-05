package tool;

import core.Sparrow;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptchaGenerator {
    private static final Logger logger = Logger.getLogger(Sparrow.class);

    private static final String CHAR_NUM_SEQUENCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String code;
    private byte[] imgBinary;


    private CaptchaGenerator(String code, byte[] imgBinary) {
        this.code = code;
        this.imgBinary = imgBinary;
    }

    public static CaptchaGenerator simpleCaptcha(int codeLen) {
        int width = codeLen * 15;
        int height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        char[] rands = new char[codeLen];
        for (int i = 0; i < codeLen; i++) {
            int rand = (int) (Math.random() * 36);
            rands[i] = CHAR_NUM_SEQUENCE.charAt(rand);
        }
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        for (int i = 0; i < 120; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            g.setColor(new Color(red, green, blue));
            g.drawOval(x, y, 1, 0);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18));
        for (int i = 0; i < codeLen; i++) {
            g.drawString("" + rands[i], 1 + 15 * i, (i % 2 == 0 ? 15 : 17) + i % 4);
        }
        g.dispose();
        byte[] bs = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "JPEG", baos);
            bs = baos.toByteArray();
        } catch (IOException e) {
            logger.error("write image binary failed due to " + e.getMessage());
        }
        return new CaptchaGenerator(new String(rands), bs);
    }

    public String getCaptchaCode() {
        return code;
    }

    public byte[] getByteImage() {
        return imgBinary;
    }
}