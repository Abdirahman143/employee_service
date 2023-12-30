package com.ems.employee_service.config.openApi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Employee service API documentation")
                .description("Documentation for Employee Rest service")
                .version("v1")
                .contact(getContactDetails())
                .license(getLicenseDetails()));
    }

    private Contact getContactDetails() {
        return new Contact().name("Contact Person").email("bashirabdi1994@gmail.com").url("mmmm");
    }

    private License getLicenseDetails() {
        return new License().name("License name").url("cccccccc");
    }
}
