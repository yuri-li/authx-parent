package org.authx.account.security.service

import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class CustomService {

    fun getRole(username: String): String = when (username) {
        "user" -> "ROLE_USER"
        else -> "ROLE_ADMIN"
    }

    fun hasAccess(authorities: String): Boolean {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        //request.method
        return authorities.contains(request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern") as String)
    }
}