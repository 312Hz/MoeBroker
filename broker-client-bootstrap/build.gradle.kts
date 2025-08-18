plugins {
    application

    alias(libs.plugins.shadow)
}

application {
    mainClass.set("me.xiaoying.moebroker.client.bootstrap.BootStrap")
}

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