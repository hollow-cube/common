import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

subprojects {
    apply(plugin = "java")
    apply(plugin = "net.ltgt.errorprone")
    apply(plugin = "maven-publish")

    group = "net.hollowcube.common"

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }


    dependencies {
        // A bug with kotlin dsl
        val compileOnly by configurations
        val implementation by configurations
        val annotationProcessor by configurations
        val testImplementation by configurations
        val testAnnotationProcessor by configurations
        val errorprone by configurations

        errorprone("com.google.errorprone:error_prone_core:2.16")
        errorprone("com.uber.nullaway:nullaway:0.10.6")

        // Auto service (SPI)
        annotationProcessor("com.google.auto.service:auto-service:1.0.1")
        testAnnotationProcessor("com.google.auto.service:auto-service:1.0.1")
        implementation("com.google.auto.service:auto-service-annotations:1.0.1")

        // Minestom
        compileOnly("com.github.hollow-cube:Minestom:a9535e5d29")

        // Testing
        testImplementation(project(":modules:test"))
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.errorprone.disableWarningsInGeneratedCode.set(true)
        options.errorprone {
            check("NullAway", CheckSeverity.ERROR)
            option("NullAway:AnnotatedPackages", "com.uber")
        }
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
    }
}
