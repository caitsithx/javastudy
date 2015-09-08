package sample;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by xiaoliangl on 9/7/15.
 */
@Configuration("AsyncConfigurations")
@EnableAsync
@EnableScheduling
public class AsyncConfigurations {
   @Bean(name="SimpleExecutor", destroyMethod = "shutdown")
   @Qualifier("SimpleExecutor")
   public Executor taskExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(7);
      executor.setMaxPoolSize(42);
      executor.setQueueCapacity(11);
      executor.setThreadNamePrefix("SimpleExecutor-");
      executor.initialize();
      return executor;
   }

   @Bean(destroyMethod = "shutdown")
   @Qualifier("SimpleScheduler")
   public ScheduledExecutorService taskScheduler() {
      return Executors.newScheduledThreadPool(100);
   }
}
