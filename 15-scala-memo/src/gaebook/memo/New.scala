package gaebook.memo

import java.io.IOException
import javax.servlet.http._
import gaebook.util._

import javax.jdo.PersistenceManager
import com.google.appengine.api.users.User
import com.google.appengine.api.users.UserService
import com.google.appengine.api.users.UserServiceFactory

class New extends HttpServlet {
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    val user = UserServiceFactory.getUserService.getCurrentUser 

    val content = req.getParameter("content")
    val memo = new Memo(user, content, new java.util.Date())
    val pm = PMF.get.getPersistenceManager
    try {
      pm.makePersistent(memo) 
    } finally {
      pm.close
    }
    resp.sendRedirect("/mainPage")
  }
}
 
