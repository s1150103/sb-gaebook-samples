package gaebook.util;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ImageDisplay extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String imageId = req.getParameter("imageId");
        String delete = req.getParameter("delete");
        
        if (delete != null && delete.equals("true")) {
            ImageUtil.delete(imageId);
            String redirect_to = req.getParameter("redirect_to");
            if (redirect_to == null)
                redirect_to = "/";     // デフォルトではルートへリダイレクト
            resp.sendRedirect(redirect_to);
            return;
        }
        ImageEntity entity = ImageUtil.getImageEntity(new Long(imageId));
        if (entity == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else { 
            resp.setContentType(entity.getContentType());
            resp.getOutputStream().write(entity.getBytes());
        }
    }
}
