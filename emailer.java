import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Created by Omkareshwar on 2/24/15.
 */
public class emailer
{

    static String username = "alpha@nikitph.com";
    static String password = "davajvmasd123";

    public static void main(String[] args)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "mail.nikitph.com");
        props.put("mail.smtp.port", "26");

        hostMonsterEmailer emailer = new hostMonsterEmailer();
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });

    }
}
