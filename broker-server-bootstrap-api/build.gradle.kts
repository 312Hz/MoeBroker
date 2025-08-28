plugins {
    id("moebroker-publish")
//    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(project(":broker-api"))
    compileOnly(project(":broker-server"))

    compileOnly(libs.snakeyaml)
}