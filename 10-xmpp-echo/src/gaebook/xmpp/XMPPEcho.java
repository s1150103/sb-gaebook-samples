package gaebook.xmpp;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;


@SuppressWarnings("serial")
public class XMPPEcho extends HttpServlet {
    
    public void doPost(HttpServletRequest req, 
            HttpServletResponse res)
           throws IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message rcvMsg = xmpp.parseMessage(req);
        
        String xmppAddr = 
//          message.getFromJid().getId().split("/")[0];
            rcvMsg.getFromJid().getId();

        JID jid = new JID(xmppAddr);  
        Message sndMsg = new MessageBuilder()  
            .withMessageType(MessageType.CHAT) 
            .withRecipientJids(jid)  
            .withBody(rcvMsg.getBody())
            .build();  

        SendResponse resp = xmpp.sendMessage(sndMsg);
        SendResponse.Status status = resp.getStatusMap().get(jid);
        if (status != SendResponse.Status.SUCCESS)
            throw new IOException("Failed to send message: " + status);
    }
}
