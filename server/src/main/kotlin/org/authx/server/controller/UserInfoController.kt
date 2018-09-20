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
}