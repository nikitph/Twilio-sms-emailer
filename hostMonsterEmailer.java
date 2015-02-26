import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.twilio.sdk.resource.factory.MessageFactory;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class hostMonsterEmailer
{

    public static final String ACCOUNT_SID = "ACac4953fc8d049aca76edcfe4197c30fd";
    public static final String AUTH_TOKEN = "082874fb2547132c67450817d67ac080";

    public static void main(String[] args) throws TwilioRestException, IOException, COSVisitorException
    {


        ArrayList<String> disallowedWords = new ArrayList<String>();
        disallowedWords.add("the");
        disallowedWords.add("a");
        disallowedWords.add("an");
        disallowedWords.add("and");
        disallowedWords.add("for");
        disallowedWords.add("i");
        disallowedWords.add("but");
        disallowedWords.add("because");
        disallowedWords.add("of");
        disallowedWords.add("it");
        disallowedWords.add("was");
        disallowedWords.add("he");
        disallowedWords.add("that");
        disallowedWords.add("in");
        disallowedWords.add("you");
        disallowedWords.add("to");
        disallowedWords.add("his");
        disallowedWords.add("s");
        disallowedWords.add("t");


        PDDocument pdf = PDDocument.load(new File("ys.pdf"));

        PDFTextStripper stripper = new PDFTextStripper();

        String plainText = stripper.getText(pdf);

        StringTokenizer st = new StringTokenizer(plainText, " ,?;+=.\t\n\r\f\"\')([]|-");
        TreeMap<String, Integer> textMap = new TreeMap<String, Integer>();
        while (st.hasMoreTokens())
        {
            String temp = st.nextToken().toLowerCase();
            if (disallowedWords.contains(temp))
                continue;

            if (textMap.containsKey(temp))
            {
                Integer x = textMap.get(temp);
                int b = x + 1;
                textMap.replace(temp, x, b);
            } else
                textMap.put(temp, 1);
        }

        TreeMap<Integer, String> inverseMap = new TreeMap<Integer, String>();
        for (Map.Entry<String, Integer> entry : textMap.entrySet())
        {
            inverseMap.put(entry.getValue(), entry.getKey());
        }

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
            message.setFrom(new InternetAddress("alpha@nikitph.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("nikitph@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("the most used word is : " + inverseMap.lastEntry().getValue() + " and occurs :" + inverseMap.lastEntry().getKey() + " times" + inverseMap.toString());

            Transport.send(message);

            System.out.println("Done");

            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", "the most used word is : " + inverseMap.lastEntry().getValue() + " and occurs :" + inverseMap.lastEntry().getKey() + " times"));
            params.add(new BasicNameValuePair("To", "+16502729075"));
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