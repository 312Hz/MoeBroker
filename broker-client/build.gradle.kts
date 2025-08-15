plugins {
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "me.xiaoying.moebroker.client"

dependencies {
    implementation(project(":broker-api"))

    // netty
    implementation("io.netty:netty-all:5.0.0.Alpha2")
}