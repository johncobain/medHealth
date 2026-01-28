package br.edu.ifba.inf012.medHealthAPI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SwaggerConfig {

    @Value("${gateway.url:http://localhost:8080/}")
    private String gatewayUrl;

    @Value("${local.url:http://localhost:8081}")
    private String localUrl;

    @Bean
    public OpenAPI customOpenAPI(){
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addServersItem(new Server().url(gatewayUrl).description("Gateway Server"))
                .addServersItem(new Server().url(localUrl).description("Local Server"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                    .components(
                        new Components()
                            .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                    .name(securitySchemeName)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                            )
                )
                .info(new Info()
                        .title("Medical Clinic Backend API")
                        .version("1.0")
                        .description("API Documentation"));
    }
}
