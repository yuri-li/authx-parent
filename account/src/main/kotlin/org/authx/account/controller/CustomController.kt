package org.authx.account.controller

import org.apache.commons.logging.LogFactory
import org.authx.common.model.CurrentUser
import org.authx.common.model.CustomUser
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
class CustomController(val oauthClient: OauthClient, val clientCredentialsResourceDetails: ClientCredentialsResourceDetails) {
    val log = LogFactory.getLog(this.javaClass)

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@Valid @RequestBody model: CustomUser.Login): OAuth2AccessToken {
        log.info("login params:${model}")
        return oauthClient.oauthToken(mapOf(
                "username" to model.username,
                "password" to model.password,
                "grant_type" to "password",
                "realm" to model.realm,
                "domain" to model.domain,
                "email" to model.email,
                "client_id" to clientCredentialsResourceDetails.clientId,
                "client_secret" to clientCredentialsResourceDetails.clientSecret
        ))
    }

    @GetMapping("/findUser/{username}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    fun findUser(@ApiIgnore authentication: CurrentUser, @PathVariable("username") username: String){
        log.info("find user by username:${username}")
    }

    @DeleteMapping("/delUser/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delUser(@ApiIgnore authentication: CurrentUser, @PathVariable("username") username: String) {
        log.info("delUser ${username}")
    }

    @GetMapping("/findAccount/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun findAccount(@ApiIgnore authentication: CurrentUser, @PathVariable("username") username: String){
        log.info("find account by username:${username}")
    }
}

@FeignClient(url = "http://localhost:8082/authx-server", value = "authx-server")
interface OauthClient {
    @RequestMapping(value = ["/oauth/token"], method = [RequestMethod.POST])
    fun oauthToken(@RequestParam parameters: Map<String, String>): OAuth2AccessToken
}