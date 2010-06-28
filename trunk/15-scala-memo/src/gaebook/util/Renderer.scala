package gaebook.util

import java.io.IOException
import java.io.Writer
import java.text.DateFormat
import java.util.TimeZone
import java.util.logging.Logger

import org.apache.velocity
import org.apache.velocity.app.Velocity
import org.apache.velocity.runtime.log.JdkLogChute
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.VelocityContext

object Renderer {
    private var initialized: Boolean = false
    private def initializeVelocity() {
        Velocity.setProperty(
            RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new JdkLogChute())
        Velocity.setProperty(
            RuntimeConstants.INPUT_ENCODING, "UTF-8")              
        Velocity.setProperty(
            RuntimeConstants.OUTPUT_ENCODING, "UTF-8")              
        Velocity.init();
        initialized = true
    } 
    private def exclusiveInit() = Renderer.synchronized(      
        if (! initialized) initializeVelocity
    )   
    
    val dateTimeFormat: DateFormat = DateFormat.getDateTimeInstance
    dateTimeFormat.setTimeZone(TimeZone.getTimeZone("JST"))

    def render(filename: String, map: Map[String, _], writer: Writer) {
        exclusiveInit()
        try {
            val context = new VelocityContext()
            map.foreach(pair => context.put(pair._1, pair._2))
            context.put("_datetimeFormat", dateTimeFormat)
            val template = Velocity.getTemplate(filename); 
            template.merge(context, writer);
        } catch {
          case e: Exception => {throw new IOException(e)} 
        }
    }
}
