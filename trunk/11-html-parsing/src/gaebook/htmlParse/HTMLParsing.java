package gaebook.htmlParse;

import java.io.*;
import java.net.URL;
import java.util.List;
import javax.servlet.http.*;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

@SuppressWarnings("serial")
public class HTMLParsing extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=utf8");
        PrintWriter writer = resp.getWriter();
        writer.println("<table width=\"800\" style=\"table-layout: fixed; word-wrap: break-word;\">");
        writer.println("<tr><th>link</th><th>link文字列</th></tr>");
        
        // パラメータからURL文字列を取得
        String url = req.getParameter("url");
        // パーザのソースに指定
        Source source=new Source(new URL(url));
        
        // <a href="xxx"> yyy </a> を選択，テーブルとして描画
        List<Element> elementList=source.getAllElements(HTMLElementName.A);
        for (Element element : elementList) {
            writer.println("<tr><td>");
            writer.println(element.getAttributeValue("href"));
            writer.println("</td><td>");
            writer.println(escape(CharacterReference.decodeCollapseWhiteSpace(element.getContent())));
            writer.println("</td></tr>");            
        }
        writer.println("</table>");
    }


    private static String escape(String input) {
        String tmp = input;
        tmp = tmp.replace("\"", "&quot;");
        tmp = tmp.replace("&", "&amp;");
        tmp = tmp.replace(">", "&gt;");
        tmp = tmp.replace("<", "&lt;");
        return tmp;
    }
}
    
