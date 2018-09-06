dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("javax.validation:validation-api:2.0.1.Final")
    compile("org.springframework.security:spring-security-core:5.0.7.RELEASE") {
        exclude(group = "org.springframework")
    }
}