package org.authx.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

@Configuration
@EnableResourceServer
class ResourceConfig : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers(
                        "/findUser/**",
                        "/delUser/**",
                        "/findAccount/**",

                        //swagger2
                        "/swagger-ui.html/**",
                        "/webjars/springfox-swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",

                        //oauth2
                        "/health",
                        "/oauth/authorize", // 授权端点
                        "/oauth/token", // 令牌端点
                        "/oauth/confirm_access", // 用户确认授权提交端点
                        "/oauth/error", // 授权服务错误信息端点
                        "/oauth/check_token", // 用于资源服务访问的令牌解析端点
                        "/oauth/token_key" // 提供公有密匙的端点，如果你使用JWT令牌的话
                ).permitAll() // 上面设置的url，允许不登录，就可以访问
                .anyRequest().authenticated() //其他url，都必须授权成功后，才允许访问
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //关闭session
                .and()
                .formLogin().disable()
                .cors().disable() //关闭CORS
                .csrf().disable() //关闭CSRF
                .httpBasic().disable() // 关闭httpBasic
    }
}