package gaebook.shard;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CounterTest extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        long value;
        String tmp = req.getParameter("shard");
        
        long start = System.nanoTime();            
        if (tmp != null && tmp.equalsIgnoreCase("true"))
            value = ShardCounter.increment("counter");
        else            
            value = JDOCounter.increment("counter");
        
        long end = System.nanoTime();
        long time = end - start;

        res.setContentType("text/plain");
        res.getWriter().println(System.currentTimeMillis() + " "+ value + " "+time);
    }
}
