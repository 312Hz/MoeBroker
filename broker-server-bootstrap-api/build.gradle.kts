plugins {
    id("moebroker-publish")
//    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(project(":broker-api"))

    compileOnly(libs.snakeyaml)
}