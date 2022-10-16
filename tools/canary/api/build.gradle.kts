plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}

dependencies {

    api(kotlin("script-runtime"))
//    api("org.jetbrains.kotlin:kotlin-scripting-common:1.7.20")

    api("com.github.hollow-cube:Minestom:e84114b752")
}
