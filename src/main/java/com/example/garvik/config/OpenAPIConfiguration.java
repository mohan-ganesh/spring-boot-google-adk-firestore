package com.example.garvik.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Mohan Ganesh
 * @version 1.0
 * @since 2026-01-09
 * 
 *        OpenAPI configuration
 */
@Configuration
public class OpenAPIConfiguration {

        @Value("${server.servlet.context-path:/}")
        private String contextPath;

        /**
         * OpenAPI bean
         * 
         * @return
         */
        @Bean
        public OpenAPI openAPI() {

                final String securitySchemeName = "bearerAuth";

                return new OpenAPI()
                                .addServersItem(new Server().url(contextPath))
                                .components(
                                                new Components()
                                                                .addSecuritySchemes(
                                                                                securitySchemeName,
                                                                                new SecurityScheme()
                                                                                                .name(securitySchemeName)
                                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("bearer")
                                                                                                .bearerFormat("JWT")))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
        }
}
