package gaebook.util;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.*;
import java.util.*;
import javax.jdo.PersistenceManager;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;

public class Deferred {
    public static class PermanentTaskFailure extends RuntimeException {
        public PermanentTaskFailure() {
        }
        public PermanentTaskFailure(String msg) {
            super(msg);
        }
        public PermanentTaskFailure(Throwable t) {
            super(t);
        }
        public PermanentTaskFailure(String msg, Throwable t) {
            super(msg, t);
        }
    }
    
    private Queue queue;
    public Deferred(){
        queue = QueueFactory.getDefaultQueue();
    }
    public Deferred(String queueName){
        queue = QueueFactory.getQueue(queueName);
    }
    
    public void defer(Class clazz, String methodName, Object... params) 
    throws IOException {
        DeferredEntity entity = new DeferredEntity();
        entity.setClassName(clazz.getName());
        entity.setMethodName(methodName);
        entity.setParams(new ArrayList<Object>(Arrays.asList(params)));
        
        entity.getMethod(); // if not found, the method throws IOException

        byte [] bytes = entity.serialize(); 
        if (bytes.length > 9 * 1024) {    
            PersistenceManager pm = null;
            try {
                pm = PMF.get().getPersistenceManager();
                pm.makePersistent(entity);
                queue.add(url("/deferredHandler").
                          param("deferredId", ""+ entity.getId()));
            } finally {
                if (pm != null && !pm.isClosed()) 
                    pm.close();
            }        
        } else {
            queue.add(url("/deferredHandler"). 
                      payload(bytes, "application/x-java-serialized-object"));
        }
    }
}