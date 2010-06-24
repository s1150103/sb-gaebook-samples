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
        //#@@range_begin(basicUser)
        String user = (String) req.getAttribute(
                gaebook.util.BasicAuthFilter.BASIC_AUTH_USERNAME);
        //#@@range_end(basicUser)
        
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Memo.class);
        query.setFilter("author == user_name");
        query.setOrdering("date desc");
        // 
        query.declareParameters("String user_name");

        List<Memo> memos = (List<Memo>) query.execute(user);

        Context context = new VelocityContext();
        context.put("memos", memos);
        context.put("user", user);
        
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        Renderer.render("WEB-INF/mainPage.vm", context, resp.getWriter());
    }
}
