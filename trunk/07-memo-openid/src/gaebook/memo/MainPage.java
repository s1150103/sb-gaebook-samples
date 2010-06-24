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

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

//      
        if (userService.isUserLoggedIn())
            System.err.println("userAdmin? " + userService.isUserAdmin());
//        
        
        
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Memo.class);
        query.setOrdering("date desc");

        query.setFilter("author == user_name");
        query.declareParameters("com.google.appengine.api.users.User user_name");
        

        List<Memo> memos = (List<Memo>) query.execute(user);
        Context context = new VelocityContext();
        context.put("memos", memos);

        String signOutUrl = userService.createLogoutURL(req.getRequestURI());
        String signInUrl = "/_ah/login_required?continue=" + req.getRequestURL();
        context.put("signOutUrl", signOutUrl);
        context.put("signInUrl", signInUrl);
        context.put("user", user == null ? null : 
            (user.getNickname().isEmpty() ? 
                    user.getFederatedIdentity(): user.getNickname()));

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        Renderer.render("WEB-INF/mainPage.vm", context, resp.getWriter());
    }
    

    public void doGet2(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();

            Query query = pm.newQuery(Memo.class);
            query.setFilter("author == user_name");
            query.setOrdering("date desc");
            query.declareParameters("com.google.appengine.api.users.User" +
                     " user_name");

            List<Memo> memos = (List<Memo>) query.execute(user);
            String signOutUrl = userService.createLogoutURL(req.getRequestURI());
            String signInUrl =  userService.createLoginURL(req.getRequestURI());
            if (user != null && user.getFederatedIdentity() == null)
                signInUrl = "/_ah/login_required";

            String userStr;
            if (user == null) 
                userStr = null;
            else if (user.getNickname().isEmpty()) 
                userStr = user.getFederatedIdentity();
            else
                userStr = user.getNickname();
            
            Context context = new VelocityContext();
            context.put("memos", memos);
            context.put("signOutUrl", signOutUrl);
            context.put("signInUrl", signInUrl);
            context.put("user", userStr); 
/*
            System.err.println("user.getAuthDomain() = " + user.getAuthDomain());    
            System.err.println("user.getEmail() = " + user.getEmail());
            System.err.println("user.getFederatedIdentity() = " + user.getFederatedIdentity());
            System.err.println("user.getNickName() = " + user.getNickname());
            System.err.println("user.getUserId() = " + user.getUserId());
            System.err.println("req.getUserPrincipal().getName() = " + req.getUserPrincipal().getName());                        
*/
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");

            Renderer.render("WEB-INF/mainPage.vm", context, resp.getWriter());
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    
    }
}
