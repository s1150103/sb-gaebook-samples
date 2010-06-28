package gaebook.util;
import java.io.IOException;

import java.util.logging.*;
import com.google.appengine.api.xmpp.*;

public class XMPPHandler extends Handler{
    private Formatter formatter = new SimpleFormatter();
    private String notifyTo;

    public XMPPHandler(String notifyTo){
        this.notifyTo = notifyTo;
    }
    @Override
    public void close() throws SecurityException {
        // nothing to do;
    }
    @Override
    public void flush() {
        // nothing to do;
    }
    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        String msg;
        try {
            msg = formatter.format(record);
            System.err.println("publish: msg = " + msg);
            writeXMPP(notifyTo, msg);
        } catch (Exception ex) {
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }
    }

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
