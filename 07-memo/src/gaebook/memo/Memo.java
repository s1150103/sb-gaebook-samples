package gaebook.memo;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

// データ構造を定義したクラス
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Memo {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long   id;
    @Persistent
    private User   author;
    @Persistent
    private String content;
    @Persistent
    private Date   date;

    public Memo(User author, String content, Date date) {
        this.author = author;
        this.content = content;
        this.date = date;
    }

    // getter/setter メソッド群
    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}