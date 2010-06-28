package gaebook.bot;

import gaebook.util.ErrorPage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.RequestToken;

import com.google.appengine.api.users.*;

public class BotEditorHandler extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        ConsumerKey.init(this);
    }
    static TwitterFactory factory = new TwitterFactory();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        
        String botId          = req.getParameter("botId");
        String word           = req.getParameter("word");
        String tweet0         = req.getParameter("tweet0");
        String tweet1         = req.getParameter("tweet1");
        String tweet2         = req.getParameter("tweet2");
        
        if (word == null || word.isEmpty() ||
            tweet0 == null || tweet0.isEmpty()){
            ErrorPage.create(res, "情報が足りません", "/");
            return;
        }
        BotDefinition bot =
            BotDefinition.update(botId, user, word, tweet0, tweet1, tweet2); 
        if (! bot.hasAccessToken()) {
            // リクエストトークンを取得
            Twitter twitter = factory.getInstance();
            twitter.setOAuthConsumer(ConsumerKey.key, ConsumerKey.secret);
            try {
                RequestToken requestToken = twitter.getOAuthRequestToken();
                bot.setRequestToken(requestToken);
                BotDefinition.makePersistent(bot);
                long id = bot.getId();
                HttpSession session = req.getSession();
                session.setAttribute("requestingBotId", "" + id);
                res.sendRedirect(requestToken.getAuthorizationURL());
            } catch (TwitterException e) {
                throw new ServletException(e);
            }
        } else {  // ボットの内容を更新 アクセスキーはそのまま
            res.sendRedirect("/");
        }        
    }    
}
