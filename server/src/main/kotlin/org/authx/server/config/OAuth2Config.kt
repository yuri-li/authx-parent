package org.authx.server.config

import org.authx.server.dao.CustomAuthenticationManager
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer


@Configuration
@EnableAuthorizationServer
class OAuth2Config(val passwordEncoder: PasswordEncoder,val details: OAuth2ClientProperties) : AuthorizationServerConfigurerAdapter() {
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
                .tokenKeyAccess("permitAll()") //允许访问
                .checkTokenAccess("isFullyAuthenticated()") //判断是否已认证
                .allowFormAuthenticationForClients() //允许client通过form传参
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient(details.clientId)
                .secret(passwordEncoder.encode(details.clientSecret))
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .authorities("ROLE_TRUSTED_CLIENT")
                .scopes("user_info")
                .autoApprove(true)

    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager())
    }

    @Bean
    @Primary
    fun authenticationManager() = CustomAuthenticationManager(passwordEncoder)
}