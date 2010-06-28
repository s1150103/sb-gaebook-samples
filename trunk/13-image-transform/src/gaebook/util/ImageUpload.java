package gaebook.util;

import java.io.*;
import java.util.logging.Logger;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("serial")
public class ImageUpload extends HttpServlet {
    static Logger logger = Logger.getLogger(ImageUpload.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            res.setContentType("text/plain");
            String redirect_to = req.getParameter("redirect_to");
            if (redirect_to == null)
                redirect_to = "/";
            
            // ファイルアップロードの処理
            FileItemIterator iterator = upload.getItemIterator(req);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();

                if (item.isFormField()) {
                    logger.warning("Got a form field: " + item.getFieldName());
                } else {
                    logger.warning("Got an uploaded file: "
                            + item.getFieldName() + ", name = "
                            + item.getName());
                    // ストリームからイメージを作成して保持
                    long id = ImageUtil.putImage(stream, item.getName());
                }
            }
            res.sendRedirect(redirect_to);
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }
}
