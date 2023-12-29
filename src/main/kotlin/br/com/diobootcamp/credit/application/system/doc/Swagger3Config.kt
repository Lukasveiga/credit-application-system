package br.com.diobootcamp.credit.application.system.doc

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {

    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem-public")
            .pathsToMatch("/api/v1/customers/**", "/api/v1/credits/**")
            .build()
    }

    @Bean
    fun infosPublicApi(): OpenAPI? {
        return OpenAPI().info(Info()
            .title("DIO Bootcamp: Desenvolvimento Backend com Kotlin - API Credit Application System")
            .contact(Contact().name("Lukas Veiga").email("lukas.veiga10@gmail.com"))
            .description("Rest API desenvolvida no bootcamp da DIO utlizando Kotlin e Spring Boot + PostgreSQL")
            .version("V0.0.1")
        )
    }
}