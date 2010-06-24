package gaebook.memo;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.http.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class OpenIDLoginHandler extends HttpServlet {
    static Logger logger = Logger.getLogger(OpenIDLoginHandler.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // 認証後に進むべきページ        
        String continuePage = req.getParameter("continue");
        // OpenIDのID
        String openidIdentifier = req.getParameter("openid_identifier");
        // 認証の対象となるドメイン。URLの最後のスラッシュ以降をすてている
        String authDomain = continuePage.substring(0, continuePage.lastIndexOf("/"));
        
        Set<String> attributesRequest = new HashSet<String>(); 
        attributesRequest.add("openid.mode=checkid_immediate");
        attributesRequest.add("openid.ns=http://specs.openid.net/auth/2.0");
        attributesRequest.add("openid.return_to=" + continuePage);
        
        UserService userService = UserServiceFactory.getUserService();
        String createdUrl = userService.createLoginURL(continuePage, authDomain, 
                                                       openidIdentifier, attributesRequest);
        logger.info(createdUrl);
        // リダイレクト
        resp.sendRedirect(createdUrl);
    }        
}
