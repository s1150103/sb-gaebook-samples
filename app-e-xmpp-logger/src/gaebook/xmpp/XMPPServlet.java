package gaebook.xmpp;

import gaebook.util.XMPPHandler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.appengine.api.xmpp.SendResponse.Status;

@SuppressWarnings("serial")
public class XMPPServlet extends HttpServlet {
    static Logger logger = Logger.getLogger(XMPPServlet.class.getName());
    static private String notifyTo = "XXXXXXXXX@gmail.com";
    static {
        logger.addHandler(new XMPPHandler(notifyTo));
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        logger.severe("severe!");
        logger.warning("warning!");
        logger.info("info");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
     
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");
    }
}
