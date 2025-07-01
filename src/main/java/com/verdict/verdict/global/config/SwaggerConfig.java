package com.verdict.verdict.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 문서")
                        .description("Verdict.gg API 명세서")
                        .version("v1.0.0"))
                .addServersItem(new Server().url("http://localhost:8080").description("로컬 서버"))
                .addServersItem(new Server().url("https://verlol.site").description("스테이징 서버"))
                .addServersItem(new Server().url("https://api.verdict.gg").description("프로덕션 서버"));
    }
}