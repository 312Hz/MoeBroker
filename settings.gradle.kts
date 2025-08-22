pluginManagement {
    includeBuild("build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "moebroker"

include(
    ":broker-api",
    ":broker-client",
    ":broker-server",
    ":broker-client-bootstrap",
    ":broker-server-bootstrap-api",
    ":broker-server-bootstrap"
)