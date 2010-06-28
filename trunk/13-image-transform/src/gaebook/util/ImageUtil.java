package gaebook.util;

import java.io.*;

import java.util.List;
import javax.jdo.*;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;

public class ImageUtil {
    // バイト列データを画像としてデータストアに保持．IDを返す
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
    
    // ストリームから画像を読み出してデータストアに保持．IDを返す
    public static long putImage(InputStream is, String name) throws IOException {
        return putImage(readBytes(is), name);
    }
    
    // IDを指定してImageEntityを取得
    public static ImageEntity getImageEntity(Long imageId) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(ImageEntity.class);

            query.setFilter("id == imageId");
            query.declareParameters("Long imageId");
            List<ImageEntity> images = 
                (List<ImageEntity>) query.execute(new Long(imageId));    
            if (!images.isEmpty()) 
                return images.get(0); 
            else
                return null;
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        } 
    }

    // すべてのImageEntityを取り出し
    public static List<ImageEntity> getImageEntities() {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(ImageEntity.class);
            List<ImageEntity> result =  (List<ImageEntity>) query.execute();
            pm.detachCopyAll(result);
            return result;            
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        } 
    }

    // ファイルから画像を読み込み
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

    // データストアから指定されたIDの画像を削除
    public static void delete(String imageId) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(ImageEntity.class);
            query.setFilter("id == imageId");
            query.declareParameters("Long imageId");
            List<ImageEntity> images = 
                (List<ImageEntity>) query.execute(new Long(imageId));    
            if (!images.isEmpty()) 
                pm.deletePersistent(images.get(0));
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // ヘルパメソッド．ストリームからバイト列を取りだし 
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
