package org.authx.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@EnableSwagger2
@Configuration
class Swagger2Config {

    /**
     *  swagger2 API，去掉默认的error接口
     */
    @Bean
    fun oauthApi(): Docket = Docket(DocumentationType.SWAGGER_2)
            .groupName("oauth")
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("/oauth.*"))
            .build()

}