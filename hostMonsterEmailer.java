import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.twilio.sdk.resource.factory.MessageFactory;


public class hostMonsterEmailer
{

    public static final String ACCOUNT_SID = "ACac4953fc8d049aca76edcfe4197c30fd";
    public static final String AUTH_TOKEN = "082874fb2547132c67450817d67ac080";

    public static void main(String[] args) throws TwilioRestException
    {

        final String username = "alpha@nikitph.com";
        final String password = "davajvmasd123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "mail.nikitph.com");
        props.put("mail.smtp.port", "26");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try
        {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nikitph@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("nikitph@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", "love u poopi"));
            params.add(new BasicNameValuePair("To", "+16502729074"));
            params.add(new BasicNameValuePair("From", "+13853991732"));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            com.twilio.sdk.resource.instance.Message
                    message1 = messageFactory.create(params);
            System.out.println(message1.getSid());

        } catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }
}