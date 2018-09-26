package org.authx.account.service

import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class CustomService {
    val log = LogFactory.getLog(this.javaClass)
    fun getRole(path: String): String {
        log.info("servlet path:${path}")
        return "ROLE_ADMIN"
    }

    fun hasAccess(authorities: String): Boolean {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        //request.method
        return authorities.contains(request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern") as String)
    }
}