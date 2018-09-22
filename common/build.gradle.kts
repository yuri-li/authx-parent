dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("javax.validation:validation-api:2.0.1.Final")
    compile("org.springframework.boot:spring-boot-starter-security") {
        exclude(group = "org.springframework")
    }
}