import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

subprojects {
    apply(plugin = "java")
    apply(plugin = "net.ltgt.errorprone")

    group = "net.hollowcube.common"

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }


    dependencies {
        // A bug with kotlin dsl
        val implementation by configurations
        val testImplementation by configurations
        val errorprone by configurations

        errorprone("com.google.errorprone:error_prone_core:2.14.0")
        errorprone("com.uber.nullaway:nullaway:0.9.8")

        // Minestom
        implementation("com.github.minestommmo:Minestom:c6c97162a6")

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
}
