package org.authx.server.controller

import org.apache.commons.logging.LogFactory
import org.authx.common.model.CurrentUser
import org.authx.common.model.CustomUser
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
class UserInfoController {
    val log = LogFactory.getLog(this.javaClass)

    @RequestMapping(value = ["/userInfo"], method = [RequestMethod.GET])
    @PreAuthorize("#oauth2.hasScope('user_info')")
    fun getUser(@ApiIgnore @AuthenticationPrincipal user: CustomUser.Load?): CustomUser.Load {
        if (user == null) {
            throw RuntimeException("Please use the user's token, not client's token")
        }
        return user
    }

    @GetMapping("/findUser/{username}")
    @Cacheable(cacheNames = ["user"], key = "#username")
    fun findUser(@PathVariable("username") username: String): CurrentUser {
        log.info("findUser ${username}")
        return if ("user".equals(username)) {
            CurrentUser(username = username, authorities = listOf(SimpleGrantedAuthority("ROLE_USER")), email = "163@qq.com", realm = "CP")
        } else if ("admin".equals(username)) {
            CurrentUser(username = username, authorities = listOf(SimpleGrantedAuthority("ROLE_USER")), email = "163@qq.com", realm = "CP")
        } else {
            throw UsernameNotFoundException(username)
        }
    }

    @DeleteMapping("/delUser/{username}")
    @CacheEvict(cacheNames = ["user", "account"], key = "#username")
    fun delUser(@PathVariable("username") username: String) {
        log.info("delUser ${username}")
    }

    @GetMapping("/findAccount/{username}")
    @Cacheable(cacheNames = ["account"], key = "#username")
    fun findAccount(@PathVariable("username") username: String): String {
        log.info("findAccount ${username}")

        return if ("user".equals(username)) {
            "user account 001"
        } else if ("admin".equals(username)) {
            "user account 002"
        } else {
            throw UsernameNotFoundException(username)
        }
    }

}