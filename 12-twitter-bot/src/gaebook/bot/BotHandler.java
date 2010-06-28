package gaebook.bot;

import java.io.IOException;
import java.util.logging.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class BotHandler extends HttpServlet {
    private static Logger logger = Logger.getLogger(BotHandler.class.getName());

    // ConsumerKey をweb.xmlから読み出してセット
    @Override
    public void init() throws ServletException {
        super.init();
        ConsumerKey.init(this);
    }
    static TwitterFactory factory = new TwitterFactory();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String botId = req.getParameter("botId");
        
        // 対象となるボットを取得
        BotDefinition bot = BotDefinition.getBotDefinition(Long.parseLong(botId));

        // Twitterオブジェクトをコンシューマキー、アクセストークンを用いて初期化
        Twitter twitter = 
            factory.getOAuthAuthorizedInstance(ConsumerKey.key, ConsumerKey.secret,
                                               bot.getAccessToken());
        try {
            // 指定された文字列を含むtweetを検索
            Query q = new Query(bot.getWord());
            q.setSinceId(bot.getSinceId());
            long lastId = bot.getSinceId();
            QueryResult result = twitter.search(q);
            for (Tweet tw: result.getTweets()) {
                if (tw.getFromUser().equals(twitter.getScreenName())) 
                    continue;  // 自分のtweet には反応しない
                // QTでメッセージを作成
                String tweet = bot.pickTweet() + " QT @" + 
                               tw.getFromUser() + " " + tw.getText();
                if (tweet.length() > 140)
                    tweet = tweet.substring(0, 140);
                logger.info(tweet);
                // つぶやく
                twitter.updateStatus(tweet);
                lastId = Math.max(tw.getId(), lastId);
            }
            // 反応した最後のIDを保存
            bot.setSinceId(lastId);
            BotDefinition.makePersistent(bot);
        } catch (TwitterException e) {
            logger.log(Level.SEVERE, "failed to twitter API call", e);
            throw new ServletException(e);
        }

    }
}
