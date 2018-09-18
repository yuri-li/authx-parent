val oauth2V = "2.3.3.RELEASE"
dependencies {
    compile(project(":common"))
    compile("org.springframework.security.oauth:spring-security-oauth2:$oauth2V")
    compile("org.springframework.boot:spring-boot-starter-cache")
    compile("com.github.ben-manes.caffeine:caffeine:2.6.2")
}