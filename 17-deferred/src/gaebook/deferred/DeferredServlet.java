package gaebook.deferred;

import gaebook.util.Deferred;
import gaebook.util.Deferred.PermanentTaskFailure;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;


@SuppressWarnings("serial")
public class DeferredServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");

        Deferred deferred = new Deferred();
        for (int i = 0; i < 10; i++) {
            deferred.defer(DeferredServlet.class, "func", i, new byte[20 * 1000]);
        }
    }

    public static void func(int i, byte [] buffer) {
        System.err.println("count " + i);    
        throw new Deferred.PermanentTaskFailure("saru");
    }

}
