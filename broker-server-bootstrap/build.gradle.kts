import org.gradle.kotlin.dsl.build
import org.gradle.kotlin.dsl.invoke

plugins {
    application

    id("com.github.johnrengelman.shadow").version("8.1.1")
}

application {
    mainClass.set("me.xiaoying.moebroker.server.bootstrap.BootStrap")
}

group = "me.xiaoying.moebroker.server.bootstrap"

dependencies {
    implementation(project(":broker-api"))
    implementation(project(":broker-server"))
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
        archiveFileName.set("moebroker-server-bootstrap-${project.version}.jar")
    }
}