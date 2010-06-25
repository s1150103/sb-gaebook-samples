package gaebook.mail;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LowMail extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        Message message = new Message(
                user.getEmail(),          // sender
                user.getEmail(),          // to
                "こんにちは",                  // subject 
                "hello from App Engine! - こんにちは！"  // content
                );  
        
        MailService ms = MailServiceFactory.getMailService();
        ms.send(message);
        
        resp.setContentType("text/plain");
        resp.getWriter().println("mail sent!");
    }
}
