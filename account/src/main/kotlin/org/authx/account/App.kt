package org.authx.account

import org.authx.account.config.SpringContextHolder
import org.authx.account.controller.OauthClient
import org.authx.account.service.UserServiceI
import org.authx.common.model.CurrentUser
import org.authx.common.util.Extensions.openUrl
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.core.MethodParameter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest

fun main(args: Array<String>) {
    SpringApplication.run(AccountConfig::class.java, *args)
    arrayOf("http://localhost:8083/account/swagger-ui.html").openUrl()
}

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class]) //去掉默认设置的user & password
@EnableFeignClients(clients = [OauthClient::class])
class AccountConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun webMvcSecurityConfiguration(): WebMvcConfigurer = object : WebMvcConfigurer {
        override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
            argumentResolvers.add(object : HandlerMethodArgumentResolver {
                override fun supportsParameter(parameter: MethodParameter): Boolean = (CurrentUser::class.java == parameter.parameterType)

                override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): CurrentUser{
                    val result = resolveArgument(webRequest.getNativeRequest(HttpServletRequest::class.java)!!)
                    val service = SpringContextHolder.getBean(UserServiceI::class.java)
                    println("================${service.getRole()}")
                    return result
                }

                private fun resolveArgument(request: HttpServletRequest): CurrentUser {
                    val map = (request.getUserPrincipal() as OAuth2Authentication).userAuthentication.details as Map<String, Any>
                    return CurrentUser(map.get("username") as String,
                            map.get("authorities") as List<GrantedAuthority>,
                            map.get("enabled") as Boolean,
                            map.get("credentialsNonExpired") as Boolean,
                            map.get("accountNonExpired") as Boolean,
                            map.get("accountNonLocked") as Boolean,
                            map.get("email") as String,
                            map.get("realm") as String)
                }
            })
        }
    }
}
