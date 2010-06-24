package gaebook.blob;

import gaebook.util.Renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.*;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class DataManager extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        
        String uploadUrl = blobstoreService.createUploadUrl("/upload");        
        BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

        List<BlobInfo> blobs = new ArrayList<BlobInfo>();
        Iterator<BlobInfo> iter = blobInfoFactory.queryBlobInfos();
        while (iter.hasNext()) 
            blobs.add(iter.next());
        
        Context context = new VelocityContext();
        context.put("uploadUrl", uploadUrl);
        context.put("blobs", blobs);
    
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        Renderer.render("WEB-INF/dataManager.vm", context, resp.getWriter());
    }
}
