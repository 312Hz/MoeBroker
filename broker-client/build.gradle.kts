plugins {
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "me.xiaoying.moebroker.client"

dependencies {
    implementation(project(":broker-api"))

    // javassist
    implementation("org.javassist:javassist:3.30.2-GA")
    // netty
    implementation("io.netty:netty-all:5.0.0.Alpha2")
}

//tasks {
//    jar {
//        enabled = false
//    }
//
//    build {
//        dependsOn(shadowJar)
//    }
//
//    shadowJar {
//        archiveClassifier.set("")
//        archiveFileName.set("moebroker-client-${project.version}.jar")
//    }
//}