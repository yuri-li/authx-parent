package org.authx.common.config

import org.authx.common.model.CurrentUser
import org.springframework.core.MethodParameter
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest

class CurrentUserArgumentResolver : WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(object : HandlerMethodArgumentResolver {
            override fun supportsParameter(parameter: MethodParameter): Boolean = (CurrentUser::class.java == parameter.parameterType)

            override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): CurrentUser {
                val result = resolveArgument(webRequest.getNativeRequest(HttpServletRequest::class.java)!!)
                return result
            }

            private fun resolveArgument(request: HttpServletRequest): CurrentUser {
                val authentication = request.getUserPrincipal() as OAuth2Authentication
                val map = authentication.userAuthentication.details as Map<String, Any>
                return CurrentUser(map.get("username") as String,
                        (map.get("authorities") as List<LinkedHashMap<String,String>>).map { it.get("authority") as String },
                        map.get("enabled") as Boolean,
                        map.get("credentialsNonExpired") as Boolean,
                        map.get("accountNonExpired") as Boolean,
                        map.get("accountNonLocked") as Boolean,
                        map.get("email") as String,
                        map.get("realm") as String)
            }
        })
    }
}