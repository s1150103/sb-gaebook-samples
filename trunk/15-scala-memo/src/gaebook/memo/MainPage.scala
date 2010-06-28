package gaebook.memo
import gaebook.util._
import java.io.IOException
import javax.servlet.http._
import java.util.HashMap  
import javax.jdo.PersistenceManager
import com.google.appengine.api.users.User
import com.google.appengine.api.users.UserService
import com.google.appengine.api.users.UserServiceFactory

class MainPage extends HttpServlet {
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val userService = UserServiceFactory.getUserService
    val user = userService.getCurrentUser
    var pm: PersistenceManager = null
    try {
        pm = PMF.get.getPersistenceManager
        val query = pm.newQuery(classOf[Memo])
        query.setFilter("author == user_name")
        query.setOrdering("date desc")
        query.declareParameters("com.google.appengine.api.users.User user_name")
        val memos = query.execute(user)
        var map = Map(
            "user" -> user, 
            "signOutUrl" -> userService.createLogoutURL(req.getRequestURI()),
            "memos" -> memos
        )
        resp.setContentType("text/html")
        resp.setCharacterEncoding("utf-8")
        Renderer.render("WEB-INF/mainPage.vm", map, resp.getWriter())
    } finally {
      if (pm != null && !pm.isClosed)
        pm.close
    }
  }
    
}
