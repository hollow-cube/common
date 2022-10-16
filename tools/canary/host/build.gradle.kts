plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":tools:canary:api"))

    implementation(project(":modules:schem"))

    api("org.jetbrains.kotlin:kotlin-scripting-common:1.7.20")
    api("org.jetbrains.kotlin:kotlin-scripting-jvm:1.7.20")
    api("org.jetbrains.kotlin:kotlin-scripting-jvm-host:1.7.20")
    implementation(kotlin("reflect", version = "1.7.20"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

//    implementation("org.jetbrains.kotlin:kotlin-main-kts:1.7.20")
}