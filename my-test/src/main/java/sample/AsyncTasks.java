package sample;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaoliangl on 9/7/15.
 */
@Component
public class AsyncTasks {
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

   @Async("SimpleExecutor")
   public void reportCurrentTime() {
      System.out.println("The AsyncTasks time is now " + dateFormat.format(new Date()));
   }
}
