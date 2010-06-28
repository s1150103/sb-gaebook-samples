package gaebook.images;

import gaebook.util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("serial")
public class ImageUpload extends HttpServlet {
    static Logger logger = Logger.getLogger(ImageUpload.class.getName());

    private byte[] readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buf = new byte[10 * 1024];
        while ((len = is.read(buf)) >= 0) {
            bos.write(buf, 0, len);
        }
        return bos.toByteArray();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            res.setContentType("text/plain");

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

                    long id = ImageUtil.putImage(readBytes(stream), item
                            .getName());
                    res.sendRedirect("/");

                }
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

}
