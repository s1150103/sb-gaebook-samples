package gaebook.imageTransform;

import gaebook.util.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.images.CompositeTransform;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

import java.text.MessageFormat;

@SuppressWarnings("serial")
public class TransformEngine extends HttpServlet {
    static Logger logger = Logger.getLogger(TransformEngine.class.getName());

    //Transformのマップ
    static final Map<String, Transform> tMap = new HashMap<String, Transform>();
    static {
        tMap.put("rotate-90", ImagesServiceFactory.makeRotate(90)); 
        tMap.put("rotate-180", ImagesServiceFactory.makeRotate(180)); 
        tMap.put("rotate-270", ImagesServiceFactory.makeRotate(270)); 
        tMap.put("flip-ud", ImagesServiceFactory.makeVerticalFlip());
        tMap.put("flip-lr", ImagesServiceFactory.makeHorizontalFlip());
        tMap.put("lucky", ImagesServiceFactory.makeImFeelingLucky());
     }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String redirect_to = req.getParameter("redirect_to");
        String imageId     = req.getParameter("imageId");
        
        CompositeTransform ct = ImagesServiceFactory.makeCompositeTransform();
        // パラメータを読んで Transformを作成，CompositeTransformに追加
        for (int i = 1; i <= 5; i++) {
            String ti          = req.getParameter("t" + i);
            String ti_text     = req.getParameter("t" + i +"-text");
            Transform tr = getTransform(ti, ti_text);
            if (tr != null)
                ct.concatenate(tr);
        }
        // ImageEntityを取得        
        ImageEntity im = ImageUtil.getImageEntity(Long.parseLong(imageId));
        // 画像にCompositeTransformを適用
        Image newImage = ImagesServiceFactory.getImagesService().applyTransform(ct, im.getImage());
        // できたファイルをデータストアに書き出し
        long newImageId = ImageUtil.putImage(newImage.getImageData(), im.getName() + ".mod");
        
        if (redirect_to != null)
            resp.sendRedirect(redirect_to + "?imageId=" + newImageId);
        else
            resp.sendRedirect("/");
    }
    

    private Transform getTransform(String tag, String param) {
        if (tag == null)
            return null;
        Transform t = tMap.get(tag);       
        if (t != null)
            return t;
        // マップにない場合 - resizeかcrop        
        if (tag.equals("resize")) {
            String [] p = param.split("[,x ]");
            if (p.length != 2) {
                logger.warning("size spec is illegal");
                throw new IllegalArgumentException();
            }
            return ImagesServiceFactory.makeResize(
                    Integer.parseInt(p[0].trim()), 
                    Integer.parseInt(p[1].trim()));
        }
        if (tag.equals("crop")) {
            String [] p = param.split("[, ]");
            if (p.length != 4) {
                logger.warning("size spec is illegal");
                throw new IllegalArgumentException();
            }
            double d[] = new double[4];
            for (int i = 0; i < 4; i++)
                d[i] = Double.parseDouble(p[i].trim());            
            logger.info(MessageFormat.format("crop - {0}, {1}, {2}, {3}", 
                    d[0], d[1], d[2], d[3]));
            return ImagesServiceFactory.makeCrop(d[0], d[1], d[2], d[3]);
        }
        return null;
    }
}