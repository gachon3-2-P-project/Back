package moguBackend.config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailTest {

    public static void main(String[] args) {
        final String username = "gachonmogu@gmail.com";
        final String password = "qubl vmoa kfjm dbid";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gachonmogu@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("bony9728@gmail.com"));
            message.setSubject("Test Subject");
            message.setText("This is a test email.");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
