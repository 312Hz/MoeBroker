plugins {
    id("moebroker-publish")
}

dependencies {
    implementation(project(":broker-api"))

    compileOnly(libs.snakeyaml)
}