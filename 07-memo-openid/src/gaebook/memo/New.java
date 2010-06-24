package gaebook.memo;

import gaebook.util.PMF;

import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

// 新しいメモをデータストアに保存するサーブレット 
public class New extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // User情報取り出し
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        // リクエストからメモの中身を取り出し
        String content = req.getParameter("content");
        // Memo インスタンスを作成
        Memo memo = new Memo(user, content, new Date());

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            // データストアに保存
            pm.makePersistent(memo);
        } finally {
            pm.close();
        }
        // もとのページにリダイレクト
        resp.sendRedirect("/mainPage");
    }
}
