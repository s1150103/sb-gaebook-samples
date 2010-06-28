package gaebook.shard;
import java.util.logging.Logger;
import gaebook.util.PMF;
import javax.jdo.*;
import javax.jdo.annotations.*;


@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable ="true")
public class JDOCounter {
    static Logger logger = 
        Logger.getLogger(JDOCounter.class.getName());
    @PrimaryKey
    @Persistent private String key;
    @Persistent private long value;
    
    public long getValue() {
        return value;
    }
    public void setValue(long value) {
        this.value = value;
    }
    public JDOCounter(String key, long value) {
        this.key = key;
        this.value = value;
    }

    public static long getValue(String counterName){
        PersistenceManager pm = PMF.get().getPersistenceManager();        
        try {
            JDOCounter counter = 
                pm.getObjectById(JDOCounter.class, counterName);
            return counter.getValue();
        } catch (JDOObjectNotFoundException e) {
            return 0L;
        }
    }
    
    public static long increment(String counterName){
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            pm = PMF.get().getPersistenceManager(); 
            while (true) {
                tx = pm.currentTransaction();
                tx.begin();
                JDOCounter counter = null;
                try {
                    counter = pm.getObjectById(JDOCounter.class, counterName);
                } catch (JDOObjectNotFoundException e) {
                    counter = new JDOCounter(counterName, 0);
                }
                long value = counter.getValue();
                counter.setValue(++value);
                pm.makePersistent(counter);
                try {
                    tx.commit();
                    return value;
                } catch (JDOCanRetryException e) {
                    logger.warning("commit failed, retry!");
                }
            }
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }   
    }
}
