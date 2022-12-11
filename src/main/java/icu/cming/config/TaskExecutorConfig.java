package icu.cming.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class TaskExecutorConfig {
    private static volatile ThreadPoolTaskExecutor threadPoolTaskExecutor;  //默认BeanName = applicationTaskExecutor

    @Bean(value = "applicationTaskExecutor")
    public Executor applicationTaskExecutor() {
        if (threadPoolTaskExecutor == null) {
            synchronized (ThreadPoolTaskExecutor.class) {
                if (threadPoolTaskExecutor == null) {
                    threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
                    threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() + 1);
                    threadPoolTaskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
                    threadPoolTaskExecutor.setQueueCapacity(9999);
                    threadPoolTaskExecutor.setThreadNamePrefix("DaKaBoot-");
                    threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                    threadPoolTaskExecutor.initialize();
                }
            }
        }
        return threadPoolTaskExecutor;
    }
}
