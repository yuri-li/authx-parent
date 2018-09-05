package org.authx.account.config

import com.google.common.base.Predicates
import org.authx.common.model.CurrentUser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.OAuth2Authentication
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@EnableSwagger2
@Configuration
class Swagger2Config {

    @Bean
    fun restApi(): Docket = Docket(DocumentationType.SWAGGER_2)
            .groupName("default-rest-api")
            .select()
            .apis(RequestHandlerSelectors.basePackage("org.authx.account.controller"))
            .build()
            .ignoredParameterTypes(CurrentUser::class.java, OAuth2Authentication::class.java)
            .securitySchemes(listOf(ApiKey("Authorization", "Authorization", "header")))
            .securityContexts(listOf(
                    SecurityContext(
                            listOf(SecurityReference("Authorization", arrayOf())),
                            Predicates.not(PathSelectors.regex("/login"))
                    )
            ))

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