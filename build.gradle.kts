import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinV = "1.2.60"
    val springbootV = "2.0.5.RELEASE"
    val springDepV = "1.0.6.RELEASE"
    val ideaExtV = "0.4.2"

    id("org.jetbrains.kotlin.jvm").version(kotlinV)

    id("org.springframework.boot").version(springbootV).apply(false)
    id("org.jetbrains.kotlin.plugin.spring").version(kotlinV).apply(false)
    id("io.spring.dependency-management").version(springDepV).apply(false)
    id("org.jetbrains.kotlin.plugin.allopen").version(kotlinV).apply(false)
    id("org.jetbrains.gradle.plugin.idea-ext").version(ideaExtV)
    `base`
}

group = "org.authx"
version = "1.0-SNAPSHOT"


allprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.gradle.plugin.idea-ext")
    }

    idea {
        module {
            setDownloadJavadoc(false)
            setDownloadSources(true)
        }
    }

    group = "org.authx"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
    tasks.withType<Wrapper> {
        gradleVersion = "4.10"
        distributionType = Wrapper.DistributionType.ALL
    }
}

val spekV = "1.1.5"
val logstashV = "5.2"
val swaggerV = "2.9.2"
subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    if (!"common".equals(project.name)) {
        apply {
            plugin("org.springframework.boot")
            plugin("org.jetbrains.kotlin.plugin.spring")
            plugin("io.spring.dependency-management")
            plugin("org.jetbrains.kotlin.plugin.allopen")
        }

        dependencies {
            compile(kotlin("reflect"))
            compile("org.springframework.boot:spring-boot-starter-web") {
                exclude(module = "spring-boot-starter-tomcat")
            }
            compile("org.springframework.boot:spring-boot-starter-undertow")
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
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}