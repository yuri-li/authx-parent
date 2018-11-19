package org.authx.account.controller

import io.swagger.annotations.Api
import org.apache.commons.logging.LogFactory
import org.authx.common.model.CurrentUser
import org.authx.common.model.CustomUser
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
@Api(tags = ["Custom API"])
class CustomController(
        val oauthClient: OauthClient,
        val details: OAuth2ClientProperties,
        val mapping: RequestMappingHandlerMapping) {
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
                "client_id" to details.clientId,
                "client_secret" to details.clientSecret
        ))
    }

    @GetMapping("/findUser/{username}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    fun findUser(@ApiIgnore authentication: CurrentUser, @PathVariable username: String) {
        log.info("find user by username:${username}")
    }

    @DeleteMapping("/delUser/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun delUser(@ApiIgnore authentication: CurrentUser, @PathVariable username: String) {
        log.info("delUser ${username}")
    }

    @GetMapping("/findAccount/{username}")
    @PreAuthorize("@customService.getRole(#username) == 'ROLE_USER' and @customService.hasAccess(#authentication.authorities)")
    fun findAccount(@ApiIgnore authentication: CurrentUser, @PathVariable username: String) {
        log.info("find account by username:${username}")
    }

    @GetMapping("/endpoints")
    fun showAllEndpoints() {
        mapping.handlerMethods.forEach { t, u ->
            println("name: ${t.methodsCondition.methods.map { it.name }.joinToString(", ")}")
        }
    }
}

@FeignClient(url = "http://localhost:8082/authx-server", value = "authx-server")
interface OauthClient {
    @RequestMapping(value = ["/oauth/token"], method = [RequestMethod.POST])
    fun oauthToken(@RequestParam parameters: Map<String, String>): OAuth2AccessToken
}