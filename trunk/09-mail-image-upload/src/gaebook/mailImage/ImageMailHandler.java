package gaebook.mailImage;

import gaebook.util.ImageUtil;

import java.io.*;
import java.net.URL;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;

import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.mail.MailService.Message;

@SuppressWarnings("serial")
public class ImageMailHandler extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
        URL url = new URL(req.getRequestURL().toString());
        // 送信元アドレス
        String sender = "echo@"
            + url.getHost().replace("appspot", "appspotmail");
        // 画像表示サーブレットのURL
        String imageDisplayUrl = 
            url.getProtocol() + "://" + url.getHost() + "/imagedisplay";

        Properties prop = System.getProperties();
        Session session = Session.getInstance(prop, null);

        try {
            MimeMessage rcvMsg = new MimeMessage(session, req.getInputStream());
            Message sndMsg = new Message();
            sndMsg.setSubject(rcvMsg.getSubject());
            sndMsg.setTo(rcvMsg.getFrom()[0].toString());

            StringBuffer sb = 
                new StringBuffer("<html><body><h3> Picture uploaded</h3><ul>");
            // 受信メッセージをパーズすると同時に返信メイルのボディを作成
            createMessage(rcvMsg, sb, imageDisplayUrl);
            sb.append("</ul></body></html>");

            sndMsg.setHtmlBody(sb.toString());
            sndMsg.setSender(sender);
            // メイル返信
            MailServiceFactory.getMailService().send(sndMsg);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IOException("Message Excetion: " + e.getMessage());      
        }
    }

    private void createMessage(Part msg, StringBuffer sb, String url) 
    throws IOException, MessagingException{
        if (msg.getContentType().startsWith("text/")) { 
            // テキスト部分は無視 
            return;
        }
        if (msg.getContentType().startsWith("image/")) {
            // 画像ファイル
            Object o = msg.getContent();
            // org.apache.geronimo.mail.util.Base64DecoderStream のはず 
            // 画像をデータストアに格納
            long id = ImageUtil.putImage(ImageUtil.readBytes((InputStream)o));
            // リンクを作成．
            sb.append("<li> <a href=\"" + url + "?imageId=" + id +  "\"> " +
            		  "image </a> </li>");        
            return;
        }
        if (msg.getContentType().startsWith("multipart/")) {
            // マルチパートをそれぞれ再起的に処理
            Multipart mp = (Multipart)msg.getContent();
            for (int i = 0; i < mp.getCount(); i++)
                createMessage(mp.getBodyPart(i), sb, url);
            return;
        }
        throw new IOException("unexpected content: '" + 
                              msg.getContentType() + "'");
    }
}


