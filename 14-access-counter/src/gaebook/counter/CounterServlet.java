package gaebook.counter;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class CounterServlet extends HttpServlet {
    private static String key = "COUNTER";

    private long increment(){
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();         
        Long val =  ms.increment(key, 1);
        if (val == null) {  
            // Memcacheに乗っていないので，datastore から取り出し
            val = CounterEntity.getCounter();
            val++;
            ms.put(key, val);   
            return val;
        } else {         
            // 100回に一回だけdatastoreに書き込む． 
            if (val % 100 == 0) 
                CounterEntity.setCounter(val);
            return val;
        }
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println(increment());
    }
}
