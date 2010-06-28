package gaebook.xmpp;
import java.io.IOException;
import java.util.logging.*;

import javax.servlet.http.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class XMPP2Twitter extends HttpServlet {
    static Logger logger = Logger.getLogger("XMPP2Twitter");
    
    static String xmppAccount = "XXXXXX";
    
    static String consumerKey    = "XXXXXXXX";
    static String consumerSecret = "XXXXXXXX";

    static String accessToken = "XXXXXXXX";
    static String accessTokenSecret = "XXXXXXXX";
    
    public void doPost(HttpServletRequest req, 
            HttpServletResponse res)
    throws IOException {

        // XMPPServiceを作成
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        // リクエストからメッセージを取り出し
        Message message = xmpp.parseMessage(req);

        String xmppAddr = message.getFromJid().getId();
        logger.info("xmppAddr = " + xmppAddr);
        
        // 登録されているJIDからでなければログに書いてリターン
        if (!xmppAddr.startsWith(xmppAccount)) {
            logger.severe(xmppAddr + " does not match");
            return;
        } 
        // Twitterに書き出し
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = 
            factory.getOAuthAuthorizedInstance(consumerKey, consumerSecret,
                    new AccessToken(accessToken, accessTokenSecret));
        try {
            twitter.updateStatus(message.getBody());
        } catch (TwitterException e) {
            logger.log(Level.SEVERE, "Twitter error", e);
        }
    }
}