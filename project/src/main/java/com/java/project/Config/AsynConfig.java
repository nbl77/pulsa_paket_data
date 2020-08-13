package com.java.project.Config;

import com.java.project.BackEnd.Controller.ReceiverController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsynConfig {
    @Bean(name = "executors")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ProjectThread-");
        executor.initialize();
        Thread receiverInit = new Thread() {
            @Override
            public void run() {
                RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
                try {
                    rabbitMQConfig.consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        executor.execute(receiverInit);
        return null;
    }
}
