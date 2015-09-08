package sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by xiaoliangl on 9/7/15.
 */
@Configuration("SchedulingConfigurations")
@EnableScheduling
public class SchedulingConfigurations implements SchedulingConfigurer{

   public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
      taskRegistrar.setScheduler(taskExecutor1());
      taskRegistrar.addFixedDelayTask(new Runnable() {
         @Override
         public void run() {
            System.out.println("The AsyncTasks time is now " + System.currentTimeMillis());

         }
      }, 5000);
   }

   @Bean(destroyMethod="shutdown")
   public ScheduledExecutorService taskExecutor1() {
      return Executors.newScheduledThreadPool(100);
   }
}
