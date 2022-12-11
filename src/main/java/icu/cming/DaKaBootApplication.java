package icu.cming;

import icu.cming.scheduled.ScheduledSign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DaKaBootApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DaKaBootApplication.class, args);
        applicationContext.getBean("scheduledSign", ScheduledSign.class).distribute();
    }

}
