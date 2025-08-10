plugins {
    application
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "me.xiaoying.moebroker.server"

dependencies {
    implementation(project(":broker-api"))

    // netty
    implementation("io.netty:netty-all:5.0.0.Alpha2")
}

application {
    mainClass.set("me.xiaoying.moebroker.server.BootStrap")
}

tasks {
    jar {
        enabled = false
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("${rootProject.name}-server-${project.version}.jar")
    }
}