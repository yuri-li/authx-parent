pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "https://repo.spring.io/plugins-snapshot")
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "authx-parent"
include("server", "account", "common")