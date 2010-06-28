package gaebook.util;

import java.io.IOException;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

public class XMPPUtil {
    private static XMPPService service = XMPPServiceFactory.getXMPPService(); 
    
    public static void writeXMPP(String to, String msg) throws IOException{
        JID jid = new JID(to);  
        Message message = new MessageBuilder()  
            .withMessageType(MessageType.CHAT) 
            .withRecipientJids(jid)  
            .withBody(msg)
            .build();  
        SendResponse resp = service.sendMessage(message);
        SendResponse.Status status = resp.getStatusMap().get(jid);
        if (status != SendResponse.Status.SUCCESS)
            throw new IOException("Failed to send message: " + status);
    }
}
