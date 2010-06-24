package gaebook.util;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

public class BasicAuthFilter implements Filter {
    private static final Logger log = Logger.getLogger(BasicAuthFilter.class
                                                        .getName());
    public static final String BASIC_AUTH_USERNAME = "BASIC_AUTH_USERNAME";
    
    Map<String, String>  userMap = new HashMap<String, String>();

    private boolean tryAuth(String authHeader, HttpServletRequest req) {
        if (authHeader == null)
            return false;
        String[] pair = authHeader.split(" ");
        if (pair.length != 2) {
            log.severe("failed to parse authHeader: " + authHeader);
            return false;
        }
        if (!pair[0].equals("Basic")) { // 認証スキームがBasicであることを確認
            log.severe("unsupported login scheme: " + pair[0]);
            return false;
        }
        String decoded = new String(Base64.decodeBase64(pair[1].getBytes()));
        String[] userPass = decoded.split(":");
        if (userPass.length != 2 || !userMap.containsKey(userPass[0])
                || !userMap.get(userPass[0]).equals(userPass[1])) {
            log.severe("AuthFailure: " + decoded);
            return false;
        }
        log.info("authentication succeeded for user '" + userPass[0] + "'");
        req.setAttribute(BASIC_AUTH_USERNAME, userPass[0]);
        return true;
    }

    // 401 レスポンスを送信
    private void send401(ServletResponse response, String realm, String message)
            throws IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(401);
        res.setHeader("WWW-Authenticate", "Basic realm=" + realm);
        res.getWriter().println("<body><h1>" + message + "</h1></body>\n");
        return;
    }

    String REALM = "Basic"; // defaultのREALM
    String passwdFile = "WEB-INF/passwd.prop"; // defaultのパスワードファイル
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String authHeader = req.getHeader("Authorization");
            // Authorization ヘッダを確認
            if (!tryAuth(authHeader, req)) 
                send401(response, REALM, "Authentication Required for" + REALM);
            else
                chain.doFilter(request, response);
        } catch (ServletException e) {
            log.severe(e.getMessage());
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }
    
    @Override
    // 初期化
    public void init(FilterConfig filterConfig) throws ServletException {
        // REALMを設定
        String tmp = filterConfig.getInitParameter("realm");
        if (tmp != null)
            REALM = tmp;
        log.info("realm = " + REALM);
        
        // パスワードファイルを設定
        tmp =  filterConfig.getInitParameter("passwdFile");
        if (tmp != null) 
            passwdFile = tmp;
        log.info("passwdFile = " + passwdFile);

        //パスワードファイルの読み込み
        Properties passProp = new Properties();
        try {
            passProp.load(new FileReader(passwdFile));
            for (Object o: passProp.keySet()) {
                String user = (String)o;
                String pass = passProp.getProperty(user).trim();
                userMap.put(user, pass);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        // なにもしない
    }
}
