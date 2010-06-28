package gaebook.util;

import java.io.*;
import java.lang.reflect.*;
import java.util.List;
import javax.jdo.annotations.*;
import javax.servlet.http.HttpServletRequest;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable ="true")
public class DeferredEntity implements Serializable {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String className;
    @Persistent
    private String methodName;
    @Persistent(serialized = "true")
    private List<Object> params;
    
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public List<Object> getParams() {
        return params;
    }
    public void setParams(List<Object> params) {
        this.params = params;
    }
    public Long getId() {
        return id;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.close();
        return bos.toByteArray();
    }

    public static DeferredEntity readEntity(HttpServletRequest req) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
        try {
            return (DeferredEntity)ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Method getMethod() throws IOException { 
        try {
            Class c = Class.forName(className);
            for (Method m: c.getMethods())
                if (checkAcceptable(m)) 
                    return m;
            throw new IOException("cannot find matching method for " + 
                    className + "." + methodName);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
    
    public void execute() throws IOException {
        try {
            getMethod().invoke(null, getParams().toArray());
        } catch (IllegalArgumentException e) {
            throw new IOException(e);
        } catch (IllegalAccessException e) {
            throw new IOException(e);
        } catch (InvocationTargetException e) {
            throw new IOException(e.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    private boolean checkAcceptable(Method m) throws IOException, ClassNotFoundException {
        if (! Modifier.isStatic(m.getModifiers()))
            return false;
        if (! m.getName().equals(methodName))
            return false;
        Class [] types = m.getParameterTypes();
        if (types.length != getParams().size())
            return false;
        for (int i = 0; i < types.length; i++) {
            Class type = types[i];
            Object o  = getParams().get(i);
            if (! type.isAssignableFrom(o.getClass()) &&
                ! isWrappingType(type, o.getClass())) 
                return false;
        }   
        return true;
    }

    private boolean isWrappingType(Class one, Class another) {
        return
                (one == Integer  .TYPE && another == Integer  .class) ||  
                (one == Long     .TYPE && another == Long     .class) ||                  
                (one == Short    .TYPE && another == Short    .class) ||                             
                (one == Character.TYPE && another == Character.class) ||                  
                (one == Double   .TYPE && another == Double   .class) ||  
                (one == Float    .TYPE && another == Float    .class) ||  
                (one == Boolean  .TYPE && another == Boolean  .class);  
    }
}
