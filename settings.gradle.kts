pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "authx-parent"
include("server", "account", "common")