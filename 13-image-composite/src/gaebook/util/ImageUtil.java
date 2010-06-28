package gaebook.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;


public class ImageUtil {
    
    public static long putImage(byte [] bytes, String name){

       PersistenceManager pm = PMF.get().getPersistenceManager();
       ImageEntity entity = new ImageEntity(bytes);
       entity.setName(name);
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

    public static List<ImageEntity> getImageEntities() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ImageEntity.class);

        return (List<ImageEntity>) query.execute();
    }

    public static Image readImage(String filename) throws IOException {
        File f = new File(filename);
        InputStream is = new FileInputStream(f);
        int length = (int) f.length();
        byte [] buffer = new byte[length];
        
        int current = 0;
        while (current < length) {
            int len = is.read(buffer, current, length - current);
            if (len < 0)
                throw new IOException("could not fully read the file");
            current += len;
        }
        return ImagesServiceFactory.makeImage(buffer);
    }

    public static void delete(String imageId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ImageEntity.class);
        
        query.setFilter("id == imageId");
        query.declareParameters("Long imageId");
    
        List<ImageEntity> images = (List<ImageEntity>) query.execute(new Long(imageId));    

        try {
            if (!images.isEmpty()) 
                pm.deletePersistent(images.get(0));
        } finally {
            pm.close();
        }
    }
    
    
}
