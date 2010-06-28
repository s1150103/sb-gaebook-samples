package gaebook.memo
import java.io.IOException
import javax.servlet.http._
  
class HelloServlet extends HttpServlet {
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
      resp.setContentType("text/plain")
      resp.getWriter().println("Hello, world, from Scala!")       
  }
}
