import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

public class MailSender {

    private String mail; // sender's email @
    private String mdp; // sender's pwd

    public MailSender(String adresse, String mdp) {
        this.mail = adresse;
        this.mdp = mdp;
    }

    public boolean sendConfirmationMail(String mail_destinataire, String num_confirmation) {
        // Recipient's email ID needs to be mentioned.
        String to = mail_destinataire;

        // Sender's email ID needs to be mentioned
        String from = mail;

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(mail, mdp);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(Settings.DEBUG_MODE);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Confirm your mail address !");

            // Now set the actual message
            //region html

            Scanner scanner = null;
            try {
                scanner = new Scanner(getClass().getResourceAsStream("mail.md"), "UTF-8");
            } catch (Exception e) {
                Debug.debugException(e);
            }
            String mailHtml = scanner.useDelimiter("\\A").next();
            String html_text = mailHtml.replace("%%CODE%%", num_confirmation);
            scanner.close(); // Put this call in a finally block

            //endregion
            message.setContent(
                    html_text,
                    "text/html");

            Transport.send(message);
            Debug.debugMsg("" + "Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }


}
