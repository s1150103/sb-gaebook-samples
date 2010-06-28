package gaebook.memo

import java.util.Date
import javax.jdo.annotations._
import com.google.appengine.api.users.User
import scala.reflect.BeanProperty

@PersistenceCapable{val identityType = IdentityType.APPLICATION}
class Memo (
    @Persistent @BeanProperty private var author:  User,
    @Persistent @BeanProperty private var content: String,
    @Persistent @BeanProperty private var date:    Date
){
    @PrimaryKey @BeanProperty  
    @Persistent{val valueStrategy = IdGeneratorStrategy.IDENTITY}
    private var id: java.lang.Long = null
}

