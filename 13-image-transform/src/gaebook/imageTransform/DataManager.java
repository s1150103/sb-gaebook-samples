package gaebook.imageTransform;

import gaebook.util.*;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

@SuppressWarnings("serial")
public class DataManager extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Context context = new VelocityContext();
        context.put("images", ImageUtil.getImageEntities());
   
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        
        Renderer.render("WEB-INF/dataManager.vm", context, resp.getWriter());
    }
}
