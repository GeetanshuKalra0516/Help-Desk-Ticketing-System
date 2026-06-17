package com.wip.helpdesk_ticketing_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI helpDeskAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Help Desk Ticketing System API")
                        .version("1.0")
                        .description("APIs for User, Ticket, Assignment, Resolution Management"));
    }
}