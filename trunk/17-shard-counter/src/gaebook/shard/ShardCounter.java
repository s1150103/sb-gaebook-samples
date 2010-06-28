package gaebook.shard;


public class ShardCounter {
    static int numOfShard = 10;
    
    public static long increment(String counterName) {
        int target = (int) Math.floor(Math.random() * numOfShard);    
        
        long sum = JDOCounter.increment(counterName + target);
        for (int i = 0; i < numOfShard; i++)
            if (i != target)
                sum += JDOCounter.getValue(counterName + i);
        return sum;
    }
}
