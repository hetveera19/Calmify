/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.managers;

import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Singleton
@DependsOn("UserFacade")

public class NotificationManager {

    private List<User> listOfSubscribedUsers = null;

    @EJB
    private UserFacade userFacade;

    Properties emailServerProperties;   // java.util.Properties
    Session emailSession;               // javax.mail.Session
    MimeMessage htmlEmailMessage;       // javax.mail.internet.MimeMessage


    /*
        Schedule a timer for automatic creation with a timeout schedule based on a cron-like time expression. The annotated method is used as the timeout callback method.
        All elements of this annotation are optional. If none are specified a persistent timer will be created with callbacks occuring every day at midnight in the default time zone associated with the container in which the application is executing.
        There are seven elements that constitute a schedule specification which are listed below. In addition, the timezone element may be used to specify a non-default time zone in whose context the schedule specification is to be evaluated;
        the persistent element may be used to specify a non-persistent timer, and the info element may be used to specify additional information that may be retrieved whe
     */
    @Schedule(minute = "30", hour = "08", persistent = false)
    //@Schedule(minute="*/5", hour="*", persistent=false)

    public void sendEmail() throws AddressException, MessagingException, Exception {

        String quoteJson = "https://zenquotes.io/api/today/";

        String imageUrl = "<img src=\"https://email-assets.calm.com/Content/Holiday/2020/Breathe-Bubble/Breathe_EN.gif\" width=\"250px\">";

        String searchResultsJsonData = Methods.readUrlContent(quoteJson);

        // Create a new JSON array from the returned file
        JSONArray searchResultsJson = new JSONArray(searchResultsJsonData);
        JSONObject jsonObject = searchResultsJson.getJSONObject(0);
        String author = (String) jsonObject.get("a");
        String quote = (String) jsonObject.get("q");

        System.out.println(quote);

        // Email message content cannot be empty
        if (quote.isEmpty()) {
            return;
        }

        listOfSubscribedUsers = userFacade.findBySubscribe(true);

        for (User user : listOfSubscribedUsers) {

            // Set Email Server Properties
            emailServerProperties = System.getProperties();
            emailServerProperties.put("mail.smtp.port", "587");
            emailServerProperties.put("mail.smtp.auth", "true");
            emailServerProperties.put("mail.smtp.starttls.enable", "true");

            try {
                // Create an email session using the email server properties set above
                emailSession = Session.getDefaultInstance(emailServerProperties, null);
                /*
                Create a Multi-purpose Internet Mail Extensions (MIME) style email
                message from the MimeMessage class under the email session created.
                 */
                htmlEmailMessage = new MimeMessage(emailSession);

                // Set the email TO field to emailTo, which can contain only one email address

                // Set the email subject line
                htmlEmailMessage.setSubject(Constants.EMAIL_SUBJECT);

                // Set the email body to the HTML type text

                // Create a transport object that implements the Simple Mail Transfer Protocol (SMTP)
                Transport transport = emailSession.getTransport("smtp");

                /*
                Connect to Gmail's SMTP server using the username and password provided.
                For the Gmail's SMTP server to accept the unsecure connection, the
                Cloud.Software.Email@gmail.com account's "Allow less secure apps" option is set to ON.
                 */
                transport.connect("smtp.gmail.com", "hello.calmify@gmail.com", "nbnxyuizvmryzkbh");


                String emailBodyText = "<div align=\"left\"> Hi " + user.getFirstName() +
                        ", <br /> &emsp; &emsp; &emsp; You are one step closer to a happier and healthier life. Our mission is to help you manage anxiety," +
                        " lower stress and uncover the best version of yourself. <br/> <br/> </div> <div align=\"center\"> " + imageUrl +
                        " <br/> -----------------------------------------------------------------<br/> <em> " + quote + "</em> <br/> <b> "
                        + author + " </b><br/>-----------------------------------------------------------------<br/> " +
                        "There is a lot more to discover. <br/> Visit us at: <a href=\"http://34.207.113.52:8080/Calmify/\" target=\"_blank\">Calmify</a></div>";

                htmlEmailMessage.setContent(emailBodyText, "text/html");
                htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                // Send the htmlEmailMessage created to the specified list of addresses (recipients)
                transport.sendMessage(htmlEmailMessage, htmlEmailMessage.getAllRecipients());
                //transport.sendMessage(htmlEmailMessage, new InternetAddress("nandaa@vt.edu"));

                System.out.println(user.getEmail());
                transport.close();
                // Close this service and terminate its connection

            } catch (AddressException ae) {
                Methods.showMessage("Fatal Error", "Email Address Exception Occurred!",
                        "See: " + ae.getMessage());

            } catch (MessagingException me) {
                Methods.showMessage("Fatal Error",
                        "Email Messaging Exception Occurred! Internet Connection Required!",
                        "See: " + me.getMessage());
            }
        }
    }
}