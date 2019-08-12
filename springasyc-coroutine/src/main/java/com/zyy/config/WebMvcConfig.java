package com.zyy.config;

import com.zzy.core.corouting.DeferredExecutor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    private ThreadPoolTaskExecutor executorService = new ThreadPoolTaskExecutor();
    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(new DeferredExecutor());
//        executorService.initialize();
//        executorService.setCorePoolSize(10);
//        configurer.setTaskExecutor(executorService);
        configurer.setDefaultTimeout(5000);
        super.configureAsyncSupport(configurer);
    }
}
