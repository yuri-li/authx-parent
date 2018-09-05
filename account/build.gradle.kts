plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.allopen")
}

val spekV = "1.1.5"
val logstashV = "5.2"
val swaggerV = "2.9.2"
dependencies {
    compile(project(":common"))
    compile(kotlin("reflect"))
    compile("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    compile("org.springframework.boot:spring-boot-starter-undertow")
    compile("org.springframework.cloud:spring-cloud-starter-openfeign:2.0.1.RELEASE")
    compile("org.springframework.cloud:spring-cloud-security:2.0.0.RELEASE")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("net.logstash.logback:logstash-logback-encoder:$logstashV")

    compile(kotlin("test"))
    compile("io.springfox:springfox-swagger2:$swaggerV")
    compile("io.springfox:springfox-swagger-ui:$swaggerV")
    testCompile("org.jetbrains.spek:spek-api:$spekV")
    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:$spekV")
    testCompile("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}