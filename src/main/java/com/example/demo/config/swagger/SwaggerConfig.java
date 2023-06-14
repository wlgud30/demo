package com.example.demo.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "킹카클럽 API",
                description = "API 명세서",
                version = "v1"))
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    private final CustomOperationCustomizer customOperationCustomizer;

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("킹카클럽 API v1")
                .pathsToMatch(paths)
                .addOperationCustomizer(customOperationCustomizer)
                .build();
    }
}

