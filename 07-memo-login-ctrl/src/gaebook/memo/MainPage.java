package gaebook.memo;

import gaebook.util.PMF;
import gaebook.util.Renderer;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.*;
import javax.servlet.http.*;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

@SuppressWarnings("serial")
public class MainPage extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //// {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        //// }

        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();

            Query query = pm.newQuery(Memo.class);
            query.setOrdering("date desc");
        //// {
            query.setFilter("author == user_name");
            query.declareParameters("com.google.appengine.api.users.User user_name");
        //// }
        
            List<Memo> memos = (List<Memo>) query.execute(user);
            Context context = new VelocityContext();
            context.put("memos", memos);
        
        //// {
            String signOutUrl = userService.createLogoutURL(req.getRequestURI());
            String signInUrl = userService.createLoginURL(req.getRequestURI());
            context.put("signOutUrl", signOutUrl);
            context.put("signInUrl", signInUrl);
            context.put("user", user);
        ///// }
        
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");

            Renderer.render("WEB-INF/mainPage.vm", context, resp.getWriter());
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }            
    }
}
