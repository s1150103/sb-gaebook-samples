package gaebook.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.*;


import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailService.Attachment;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LowMailAttach extends HttpServlet {

    static byte [] getBytes(String filename) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(filename);
        byte [] buf = new byte[10 * 1024];
        int count = 0;
        while ((count = fis.read(buf)) > 0)
            bos.write(buf, 0, count);
        return bos.toByteArray();
    }

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
        Attachment pict = 
            new Attachment("appEngine.png", getBytes("WEB-INF/appEngine.png"));
            
        message.setAttachments(pict);
        MailService ms = MailServiceFactory.getMailService();
        ms.send(message);
        
        resp.setContentType("text/plain");
        resp.getWriter().println("mail sent!");
    }
}
