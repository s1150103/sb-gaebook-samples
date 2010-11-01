package gaebook.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.util.logging.*;
import javax.jdo.PersistenceManager;
import javax.servlet.*;
import javax.servlet.http.*;


public class DeferredHandler extends HttpServlet {
    Logger logger = Logger.getLogger(DeferredHandler.class.getName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("deferredId");
        
        PersistenceManager pm = null;
        DeferredEntity entity = null;
        try {
            if (idStr != null) { // in datastore            
                pm = PMF.get().getPersistenceManager();
                entity = pm.getObjectById(DeferredEntity.class, Long.valueOf(idStr));
            } else 
                entity = DeferredEntity.readEntity(req);   
            try {
                entity.execute();
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof Deferred.PermanentTaskFailure)
                    logger.log(Level.WARNING, "got PermananentException", e);
                else 
                    throw new IOException(e);
            }
            if (idStr != null)
                pm.deletePersistent(entity);
        } finally {
            if (pm != null && !pm.isClosed()) 
                pm.close();
        }        
    }
}
