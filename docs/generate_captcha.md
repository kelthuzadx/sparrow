# Generate CAPTCHA
```java
import tool.CaptchaGenerator;

CaptchaGenerator cg = CaptchaGenerator.simpleCaptcha(4);
System.out.println(cg.getCaptchaCode());
byte[] imgBinary = cg.getByteImage();
System.out.println(imgBinary);
```