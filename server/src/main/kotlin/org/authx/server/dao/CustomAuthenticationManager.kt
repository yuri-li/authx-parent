package org.authx.server.dao

import org.apache.commons.logging.LogFactory
import org.authx.common.model.CustomUser
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.SpringSecurityMessageSource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import javax.validation.Validation


class CustomAuthenticationManager(val passwordEncoder: PasswordEncoder) : AuthenticationManager {
    val log = LogFactory.getLog(this.javaClass)
    val messages = SpringSecurityMessageSource.getAccessor()

    override fun authenticate(authentication: Authentication): Authentication {
        val auth = authentication.toCustom()
        val userDetails = loadUserDetails(auth)
        val presentedPassword = auth.password
        val encodedPassword = userDetails.password

        if (!passwordEncoder.matches(presentedPassword, encodedPassword)) {
            log.debug("Authentication failed: password does not match stored value")
            throw BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
        }
        return UsernamePasswordAuthenticationToken(
                userDetails.copy(password = null), null,
                userDetails.authorities)
    }

    private fun Authentication.toCustom(): CustomUser.Login {
        val details = this.details as LinkedHashMap<String, Any>
        val auth = CustomUser.Login(
                this.name,
                this.credentials.toString(),
                details.get("realm").toString(),
                details.get("domain").toString(),
                details.get("email").toString()
        )
        validate(auth)
        return auth
    }

    private fun <T> validate(obj: T) {
        val validations = Validation.buildDefaultValidatorFactory().validator.validate(obj)
        if (validations.isNotEmpty()) {
            throw OAuth2Exception(validations.map { it.message }.joinToString(";"))
        }
    }

    /**
     * 真实环境中，加载用户的数据，可能是通过“从库”拿到的，也可能是通过第三方服务拿到的。总之，oauthx“read only”
     */
    private fun loadUserDetails(authentication: CustomUser.Login): CustomUser.Load =
            if ("user".equals(authentication.username)) {
                CustomUser.Load(username = authentication.username, password = passwordEncoder.encode("123456"), authorities = listOf(SimpleGrantedAuthority("ROLE_USER")), email = "163@qq.com", realm = "CP")
            } else if ("admin".equals(authentication.username)) {
                CustomUser.Load(username = authentication.username, password = passwordEncoder.encode("123456"), authorities = listOf(SimpleGrantedAuthority("ROLE_USER")), email = "163@qq.com", realm = "CP")
            } else {
                throw UsernameNotFoundException(authentication.toString())
            }

}