package gaebook.mail;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.mail.MailService.Message;

@SuppressWarnings("serial")
public class EchoMailHandler extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        URL url = new URL(req.getRequestURL().toString());
        String sender = "echo@"
                + url.getHost().replace("appspot", "appspotmail");
        Properties prop = System.getProperties();
        Session session = Session.getInstance(prop, null);
        MimeMessage msg;
        try {
            msg = new MimeMessage(session, req.getInputStream());
            Message message = new Message();
            message.setSubject(msg.getSubject());
            message.setTo(msg.getFrom()[0].toString());
            if (msg.getContentType().startsWith("text/plain"))
                message.setTextBody((String) msg.getContent());
            else
                message.setTextBody("unknow message format");
            message.setSender(sender);
            MailServiceFactory.getMailService().send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IOException("Message Excetion: " + e.getMessage());
        }
    }
}


