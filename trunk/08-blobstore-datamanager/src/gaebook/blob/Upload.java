package gaebook.blob;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


public class Upload extends HttpServlet {
    Logger logger = Logger.getLogger(Upload.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        for (String fileTag: blobs.keySet())
            logger.info("uploaded: " + fileTag + ", key = " + blobs.get(fileTag).getKeyString());            
        res.sendRedirect("/");
    }
}