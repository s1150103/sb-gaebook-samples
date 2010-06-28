package gaebook.util
import javax.jdo._

object PMF {
  var pmfInstance: PersistenceManagerFactory = 
        JDOHelper.getPersistenceManagerFactory("transactions-optional")
  def get(): PersistenceManagerFactory = pmfInstance
}
