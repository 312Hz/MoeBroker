plugins {
    java
    `maven-publish`
}

extensions.configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}