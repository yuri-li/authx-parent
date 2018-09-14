package org.authx.account.controller

import org.apache.commons.logging.LogFactory
import org.authx.common.model.CurrentUser
import org.authx.common.model.CustomUser
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
class CustomController(val oauthClient: OauthClient, val clientCredentialsResourceDetails: ClientCredentialsResourceDetails) {
    val log = LogFactory.getLog(this.javaClass)

    @RequestMapping(value = ["/customers"], method = [RequestMethod.GET])
    fun find(@ApiIgnore authentication: CurrentUser): CurrentUser {
        return authentication
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@Valid @RequestBody model: CustomUser.Login): OAuth2AccessToken {
        log.info("2. login params:${model}")
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

}

@FeignClient(url = "http://localhost:8082/authx-server", value = "authx-server")
interface OauthClient {
    @RequestMapping(value = ["/oauth/token"], method = [RequestMethod.POST])
    fun oauthToken(@RequestParam parameters: Map<String, String>): OAuth2AccessToken
}