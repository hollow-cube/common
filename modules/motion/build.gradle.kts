plugins {
    `java-library`
}

dependencies {
    implementation("com.github.mworzala.mc_debug_renderer:minestom:1.19.2-rv1")

    testImplementation(project(":tools:canary:api"))
    testRuntimeOnly(project(":tools:canary:host"))
}
