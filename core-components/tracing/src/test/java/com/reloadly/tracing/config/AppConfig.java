package com.reloadly.tracing.config;

import com.reloadly.tracing.service.ChildService;
import com.reloadly.tracing.service.ChildServiceImpl;
import com.reloadly.tracing.service.ParentService;
import com.reloadly.tracing.service.ParentServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ParentService parentService(ChildService childService) {
        return new ParentServiceImpl(childService);
    }

    @Bean
    public ChildService childService() {
        return new ChildServiceImpl();
    }
}
