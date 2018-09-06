val oauth2V = "2.3.3.RELEASE"
dependencies {
    compile(project(":common"))
    compile("org.springframework.security.oauth:spring-security-oauth2:$oauth2V")
}