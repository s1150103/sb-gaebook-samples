package gaebook.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class ImageUtil {
    
    public static long putImage(byte [] bytes){

       PersistenceManager pm = PMF.get().getPersistenceManager();
       ImageEntity entity = new ImageEntity(bytes);
        try {
            pm.makePersistent(entity);
        } finally {
            pm.close();
        }
        return entity.getId();    
    }
    
    public static ImageEntity getImageEntity(Long imageId) {
        
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ImageEntity.class);
        
        query.setFilter("id == imageId");
        query.declareParameters("Long imageId");
    
        List<ImageEntity> images = (List<ImageEntity>) query.execute(new Long(imageId));    

    
        if (!images.isEmpty()) 
            return images.get(0); 
        else
            return null;
    }
    
    public static byte[] readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buf = new byte[10 * 1024];
        while ((len = is.read(buf)) >= 0) {
            bos.write(buf, 0, len);
        }
        return bos.toByteArray();
    }    
}
