package gaebook.memo;

import gaebook.util.Renderer;
import java.io.IOException;
import javax.servlet.http.*;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

public class OpenIDLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String continuePage = req.getParameter("continue");
        
        Context context = new VelocityContext();
        context.put("continue", continuePage);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Renderer.render("WEB-INF/openIDLogin.vm", context, resp.getWriter());
    }
}
