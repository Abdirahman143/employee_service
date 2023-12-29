package com.ems.employee_service.config.openApi;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi(){
        return  GroupedOpenApi.
                builder().
                 group("employee-service")
                .pathsToMatch("/api/v1/employee/**")
                .build();
    }
}
