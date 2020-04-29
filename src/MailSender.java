import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private  String mail = "my_mail" ; // entrez l adresse mail de l expediteur , une adresse mail ne peut envoyer que
                                            //100 mail par jours
    private  String mdp = "my_mdp" ;

    public MailSender(String adresse , String mdp){
        this.mail = adresse ;
        this.mdp = mdp ;
    }

    public  boolean sendConfirmationMail(String mail_destinataire,String num_confirmation){
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
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Confirm your mail adress !");

            // Now set the actual message
            //region html
            String html_text = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                    "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta content=\"telephone=no\" name=\"format-detection\">\n" +
                    "    <title></title>\n" +
                    "    <!--[if (mso 16)]>\n" +
                    "    <style type=\"text/css\">\n" +
                    "    a {text-decoration: none;}\n" +
                    "    </style>\n" +
                    "    <![endif]-->\n" +
                    "    <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "    <div class=\"es-wrapper-color\">\n" +
                    "        <!--[if gte mso 9]>\n" +
                    "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                    "\t\t\t\t<v:fill type=\"tile\" color=\"#323537\"></v:fill>\n" +
                    "\t\t\t</v:background>\n" +
                    "\t\t<![endif]-->\n" +
                    "        <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "            <tbody>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"esd-email-paddings\" valign=\"top\">\n" +
                    "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content esd-header-popover\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table class=\"es-content-body\" style=\"background-color: transparent;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure es-p5b es-p10r es-p10l\" esd-general-paddings-checked=\"false\" align=\"left\">\n" +
                    "                                                        <!--[if mso]><table width=\"580\" cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"280\" valign=\"top\"><![endif]-->\n" +
                    "                                                        <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td class=\"es-m-p0r es-m-p20b esd-container-frame\" width=\"280\" valign=\"top\" align=\"center\">\n" +
                    "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-image\" style=\"font-size: 0px;\"><a target=\"_blank\"><img class=\"adapt-img\" src=\"https://demo.stripocdn.email/content/guids/42668e66-0523-4ea2-9db4-f0dcaeee0fd3/images/58471588198710862.png\" alt style=\"display: block;\" width=\"280\"></a></td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                        <!--[if mso]></td><td width=\"20\"></td><td width=\"280\" valign=\"top\"><![endif]-->\n" +
                    "                                                        <table cellspacing=\"0\" cellpadding=\"0\" align=\"right\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td class=\"esd-container-frame\" width=\"280\" align=\"left\">\n" +
                    "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-empty-container\" style=\"display: none;\"></td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                        <!--[if mso]></td></tr></table><![endif]-->\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure es-p20t es-p20b es-p20r es-p20l\" esd-general-paddings-checked=\"false\" style=\"background-color: #3d85c6;\" bgcolor=\"#3d85c6\" align=\"left\" esd-custom-block-id=\"1700\">\n" +
                    "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td class=\"esd-container-frame\" width=\"560\" valign=\"top\" align=\"center\">\n" +
                    "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td class=\"esd-block-text es-p15t es-p15b\" align=\"center\">\n" +
                    "                                                                                        <div class=\"esd-text\">\n" +
                    "                                                                                            <h2 style=\"color: #242424; font-size: 30px;\"><strong>&nbsp;confirme your mail adress!&nbsp;</strong></h2>\n" +
                    "                                                                                        </div>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td class=\"esd-block-text es-p10l\" align=\"center\">\n" +
                    "                                                                                        <p style=\"color: #242424;\">Hi there, you're one step away from validating your account ,juste&nbsp;<br>enter this confimation code in the requested field in our applicaiton<br></p>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td class=\"esd-block-button es-p15t es-p15b es-p10r es-p10l\" align=\"center\"><span class=\"es-button-border\" style=\"border-radius: 20px; background: #191919 none repeat scroll 0% 0%; border-style: solid; border-color: #2cb543; border-width: 0px;\"><a href class=\"es-button\" target=\"_blank\" style=\"border-radius: 20px; font-family: lucida sans unicode,lucida grande,sans-serif; font-weight: normal; font-size: 18px; border-width: 10px 35px; background: #191919 none repeat scroll 0% 0%; border-color: #191919; color: #ffffff;\">"+num_confirmation+"</a></span></td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                            </tbody>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </tbody>\n" +
                    "                        </table>\n" +
                    "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content esd-footer-popover\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"es-p20t es-p20r es-p20l esd-structure\" align=\"left\">\n" +
                    "                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">\n" +
                    "                                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"left\" class=\"esd-block-text\">\n" +
                    "                                                                                        <p>this e-mail was used to creat an account on our application , if it wasn t you pls contact our support&nbsp;<br></p>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                            </tbody>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </tbody>\n" +
                    "                        </table>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </tbody>\n" +
                    "        </table>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";
            //endregion
            message.setContent(
                    html_text,
                    "text/html");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false ;
        }
        return true ;
    }



}
