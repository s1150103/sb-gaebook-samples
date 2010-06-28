package gaebook.images;

import gaebook.util.ImageEntity;
import gaebook.util.ImageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageDisplay extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String imageId = req.getParameter("imageId");
        String delete = req.getParameter("delete");

        if (delete != null && delete.equals("true")) {
            ImageUtil.delete(imageId);
            resp.sendRedirect("/dataManager");
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
