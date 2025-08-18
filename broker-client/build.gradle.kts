plugins {
    alias(libs.plugins.shadow)

    id("moebroker-publish")
}

dependencies {
    implementation(project(":broker-api"))

    // netty
    implementation(libs.netty)
}