package gaebook.imageTransform;

import gaebook.util.Renderer;
import java.io.IOException;
import javax.servlet.http.*;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

 @SuppressWarnings("serial")
 public class ImageTransformPanel extends HttpServlet {
     public void doGet(HttpServletRequest req, HttpServletResponse resp)
             throws IOException {
         String imageId = req.getParameter("imageId");
            
         Context context = new VelocityContext();
         context.put("imageId", imageId);
         context.put("redirect_to", req.getRequestURI());
         
         resp.setContentType("text/html");
         resp.setCharacterEncoding("utf-8");
         Renderer.render("WEB-INF/imageTransformPanel.vm", context, resp.getWriter());
    }
}
