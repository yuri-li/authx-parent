package org.authx.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


fun main(args: Array<String>) {
    SpringApplication.run(AuthxServerConfig::class.java, *args)
}

@SpringBootApplication
class AuthxServerConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

/*    @Bean
    fun userDetailsService(): UserDetailsService = InMemoryUserDetailsManager(
            User.withUsername("user")
                    .password(passwordEncoder().encode("123456"))
                    .roles("USER")
                    .build()
    )*/

}