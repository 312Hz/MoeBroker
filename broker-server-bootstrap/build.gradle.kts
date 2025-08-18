import org.gradle.kotlin.dsl.build
import org.gradle.kotlin.dsl.invoke

plugins {
    application

    alias(libs.plugins.shadow)
}

application {
    mainClass.set("me.xiaoying.moebroker.server.bootstrap.BootStrap")
}

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