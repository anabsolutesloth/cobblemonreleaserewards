rootProject.name = "cobblemonreleaserewards"

pluginManagement {
    repositories {
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")

        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("gradle/build-logic")
}


listOf(
    "common",
    "neoforge",
    "fabric"
).forEach { setupProject(it, file(it)) }

fun setupProject(name: String, projectDirectory: File) = setupProject(name) {
    projectDir = projectDirectory
}

inline fun setupProject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}