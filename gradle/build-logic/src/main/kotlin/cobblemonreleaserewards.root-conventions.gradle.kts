plugins {
    `java-library`
}

tasks {
    val collectJars by registering(Copy::class) {
        val tasks = subprojects.filter { it.path != ":common" }.map { it.tasks.named("remapJar") }
        dependsOn(tasks)

        from(tasks)
        into(layout.buildDirectory.asFile.get().resolve("libs"))
    }

    assemble {
        dependsOn(collectJars)
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
