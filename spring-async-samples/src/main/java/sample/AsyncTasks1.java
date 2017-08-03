package sample;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaoliangl on 9/7/15.
 */
@Component
public class AsyncTasks1 {
   private final static Logger LOGGER = Logger.getLogger(AsyncTasks1.class);
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

   @Async("SimpleExecutor")
   public void reportCurrentTime() {
      LOGGER.info("The AsyncTasks time is now " + dateFormat.format(new Date()));
   }

   @Async("SimpleExecutor")
   public void wrapCall() {
      LOGGER.info("wrap call");
      reportCurrentTime();
   }
}
