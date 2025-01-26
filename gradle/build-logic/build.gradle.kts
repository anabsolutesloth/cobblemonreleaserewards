plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://maven.architectury.dev/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.minecraftforge.net/")

    gradlePluginPortal()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.shadow)
    implementation(libs.gradle.loom)
    implementation(libs.gradle.architectury)
}