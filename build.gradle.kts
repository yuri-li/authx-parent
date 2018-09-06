import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinV = "1.2.60"
    val springbootV = "2.0.4.RELEASE"
    val springDepV = "1.0.6.RELEASE"
    val ideaExtV = "0.4.1"

    id("org.springframework.boot").version(springbootV).apply(false)
    id("org.jetbrains.kotlin.jvm").version(kotlinV).apply(false)
    id("org.jetbrains.kotlin.plugin.spring").version(kotlinV).apply(false)
    id("io.spring.dependency-management").version(springDepV).apply(false)
    id("org.jetbrains.kotlin.plugin.allopen").version(kotlinV).apply(false)
    id("org.jetbrains.gradle.plugin.idea-ext").version(ideaExtV).apply(false)
    `base`
}

group = "org.authx"
version = "1.0-SNAPSHOT"


allprojects {
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
}
dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}