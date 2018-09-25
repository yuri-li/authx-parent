package org.authx.account

import org.apache.commons.logging.LogFactory
import org.authx.account.controller.OauthClient
import org.authx.common.config.CurrentUserArgumentResolver
import org.authx.common.util.Extensions.openUrl
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

fun main(args: Array<String>) {
    SpringApplication.run(AccountConfig::class.java, *args)
    arrayOf("http://localhost:8083/account/swagger-ui.html").openUrl()
}

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class]) //去掉默认设置的user & password
@EnableFeignClients(clients = [OauthClient::class])
class AccountConfig {
    val log = LogFactory.getLog(this.javaClass)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun webMvcSecurityConfiguration(): WebMvcConfigurer = CurrentUserArgumentResolver()
}
