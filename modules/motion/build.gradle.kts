plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
}

dependencies {
    implementation("com.github.mworzala.mc_debug_renderer:minestom:1.19.2-rv1")

    implementation("org.jetbrains.kotlin:kotlin-scripting-common:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:1.7.20")

    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.7.20")

}
