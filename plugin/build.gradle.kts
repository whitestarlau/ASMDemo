plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.7.20"
//    id("org.jetbrains.kotlin.jvm") version "1.7.20"

    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.18.0"
}



group = "com.example.asm"
version = "1.0"

gradlePlugin {
    plugins {
        create("asmEffectPlugin") {
            id = "com.example.asm"
            displayName = "<short displayable name for plugin>"
            description = "<Good human-readable description of what your plugin is about>"
            implementationClass = "com.example.asm.effectwidget.EffectWidgetPlugin"
        }
    }
}



dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:7.2.0")
    compileOnly("commons-io:commons-io:2.6")
    compileOnly("commons-codec:commons-codec:1.15")
    compileOnly("org.ow2.asm:asm-commons:9.2")
    compileOnly("org.ow2.asm:asm-tree:9.2")
}