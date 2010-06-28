package gaebook.counter;

import gaebook.util.PMF;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CounterEntity {
    private static final String keyName = "COUNTER";
    @PrimaryKey
    @Persistent private String key;
    @Persistent private long value;

    CounterEntity(long  value){
        this.key = keyName;
        this.value = value;
    }
    public long getValue() {
        return value;
    }
    public void setValue(long value) {
        this.value = value;
    }

    // データストアから値を取得
    static public long getCounter() {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            CounterEntity ce = pm.getObjectById(CounterEntity.class, keyName);
            return ce.getValue();
        } catch (JDOObjectNotFoundException e) {            
            return 0;
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // データストアに値を書き出し
    static public void setCounter(long val) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            CounterEntity ce = pm.getObjectById(CounterEntity.class, keyName);
            ce.setValue(val);   
        } catch (JDOObjectNotFoundException e) {            
            CounterEntity ce = new CounterEntity(val); 
            pm.makePersistent(ce);
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}