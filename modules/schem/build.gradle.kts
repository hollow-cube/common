plugins {
    `java-library`
    `maven-publish`
    id("me.champeau.jmh") version "0.7.0"
}

dependencies {
    testImplementation("org.openjdk.jmh:jmh-core:1.35")
    testAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.35")
}




