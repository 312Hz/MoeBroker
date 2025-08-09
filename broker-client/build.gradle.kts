plugins {
    application
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "me.xiaoying.moebroker.client"

application {
    mainClass.set("me.xiaoying.moebroker.client.BootStrap")
}

dependencies {
    implementation(project(":broker-api"))
}