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
    implementation(project(":broker-server-bootstrap-api"))

    implementation(libs.snakeyaml)
    implementation(libs.jline)
    implementation(libs.terminal)
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