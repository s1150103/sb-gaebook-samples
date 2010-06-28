package gaebook.images;

import gaebook.util.ImageEntity;


import gaebook.util.ImageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.images.Composite;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;

@SuppressWarnings("serial")
public class CompositeTest extends HttpServlet {
    static Logger logger = Logger.getLogger(CompositeTest.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        List<Composite> composites = new ArrayList<Composite>();
        Image darksky = ImageUtil.readImage("WEB-INF/darksky.tif");
        composites.add(ImagesServiceFactory.makeComposite(darksky, 0, 0, 1.0f, Composite.Anchor.CENTER_CENTER));
        Image star = ImageUtil.readImage("WEB-INF/star.tif");

        composites.add(ImagesServiceFactory.makeComposite(star, 10, 10, 0.8f, Composite.Anchor.TOP_LEFT));
        composites.add(ImagesServiceFactory.makeComposite(star, -10, 10, 0.7f, Composite.Anchor.TOP_RIGHT));
        composites.add(ImagesServiceFactory.makeComposite(star, 10, -100, 0.9f, Composite.Anchor.CENTER_CENTER));
        composites.add(ImagesServiceFactory.makeComposite(star, 100, -10, 0.8f, Composite.Anchor.CENTER_CENTER));
        
        Image newImage = ImagesServiceFactory.getImagesService().composite(composites, darksky.getWidth(), darksky.getHeight(), 0);
        
        long newImageId = ImageUtil.putImage(newImage.getImageData(), "sky");
        resp.sendRedirect("/imageDisplay?imageId=" + newImageId);    
    }
}