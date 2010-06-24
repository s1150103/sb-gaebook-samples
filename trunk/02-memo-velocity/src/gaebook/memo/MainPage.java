package gaebook.memo;

import gaebook.util.PMF;
import gaebook.util.Renderer;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

@SuppressWarnings("serial")
//#@@range_begin(servlet_body)
public class MainPage extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Memo.class);
		query.setOrdering("date desc");

		List<Memo> memos = (List<Memo>) query.execute();

		Context context = new VelocityContext();
		context.put("memos", memos);

		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");

		Renderer.render("WEB-INF/mainPage.vm", context, resp.getWriter());
	}
}
//#@@range_end(servlet_body)
