plugins {
    application

    id("com.github.johnrengelman.shadow").version("8.1.1")
}

application {
    mainClass.set("me.xiaoying.moebroker.client.bootstrap.BootStrap")
}

group = "me.xiaoying.moebroker.client.bootstrap"

dependencies {
    implementation(project(":broker-api"))
    implementation(project(":broker-client"))
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
        archiveFileName.set("moebroker-client-bootstrap-${project.version}.jar")
    }
}