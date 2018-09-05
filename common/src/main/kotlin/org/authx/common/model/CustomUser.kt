package org.authx.common.model

import org.authx.common.util.RegexUtil
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.validation.constraints.Pattern


sealed class CustomUser {
    //封装登录时传递的参数
    data class Login(
            val username: String, val password: String, val realm: String, val domain: String,
            @field:Pattern(regexp = RegexUtil.EMAIL, message = "email格式错误")
            val email: String
    )

    //封装从后台加载的user数据（password != null）
    //用token换authenticationPrincipal（password = null）
    data class Load(
            private val username: String,
            private val password: String?,
            private val authorities: List<GrantedAuthority>,
            private val isEnabled: Boolean = true,
            private val isCredentialsNonExpired: Boolean = true, //密码没有过期
            private val isAccountNonExpired: Boolean = true, //账号没有过期
            private val isAccountNonLocked: Boolean = true,//账号没有被锁定
            val email: String,
            val realm: String
    ) : UserDetails {
        override fun getAuthorities(): Collection<out GrantedAuthority> = authorities

        override fun isEnabled(): Boolean = isEnabled

        override fun getUsername(): String = username

        override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired

        override fun getPassword(): String? = password

        override fun isAccountNonExpired(): Boolean = isAccountNonExpired

        override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    }
}

data class CurrentUser(
        val username: String,
        val authorities: List<GrantedAuthority>,
        val isEnabled: Boolean = true,
        val isCredentialsNonExpired: Boolean = true, //密码没有过期
        val isAccountNonExpired: Boolean = true, //账号没有过期
        val isAccountNonLocked: Boolean = true,//账号没有被锁定
        val email: String,
        val realm: String
)