package br.com.jotave_erref.RestWithSpringBoot.infra.springdoc;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;


@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("RESTful API With Java 17 and Spring Boot 3")
                        .version("v1")
                        .description("Person Management")
                        .termsOfService("https://example.com.br/example")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://game/api/licenca")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**/**")
                .build();
    }
}
