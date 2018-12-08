package tool;

import core.PropertiesHolder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailWorker {
    private Properties props;
    private Authenticator authenticator;

    public MailWorker() {
        props = new Properties();
        config();
        authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
    }

    private void config() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.user", PropertiesHolder.readProp("email.sender"));
        props.put("mail.password", PropertiesHolder.readProp("email.authcode"));
    }


    public void sendMailAsync(String subject, String body, String toMail) {
        new Thread(() -> {
            try {
                Session mailSession = Session.getInstance(props, authenticator);
                MimeMessage message = new MimeMessage(mailSession);
                InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
                message.setFrom(form);
                InternetAddress to = new InternetAddress(toMail);
                message.setRecipient(MimeMessage.RecipientType.TO, to);
                message.setSubject(subject);
                message.setContent(body, "text/html;charset=UTF-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
    }
}