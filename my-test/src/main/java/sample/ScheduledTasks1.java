package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks1 {

    @Autowired
    private AsyncTasks asyncTasks;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        asyncTasks.reportCurrentTime();
    }
}
