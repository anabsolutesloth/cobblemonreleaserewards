configurations.all {
    resolutionStrategy {
        force(libs.fabric.loader)
    }
}

plugins {
    id("cobblemonreleaserewards.platform-conventions")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

dependencies {
    "developmentFabric"(project(":common", configuration = "namedElements")) { isTransitive = false }
    bundle(project(":common", configuration = "transformProductionFabric")) { isTransitive = false }

    modApi(libs.architectury.fabric)

    modImplementation(libs.fabric.loader)
    modApi(libs.fabric.api)
    modApi(libs.fabric.kotlin)

//	Uncomment the following line to enable the deprecated Fabric API modules.
//	These are included in the Fabric API production distribution and allow you to update your mod to the latest modules at a later more convenient time.
//    modImplementation(libs.fabric.apiDeprecated)

    modImplementation(libs.cobblemon.fabric)

    modImplementation("com.terraformersmc:modmenu:11.0.3")
}

tasks {
    processResources {
        val architecturyVersion = libs.architectury.fabric.get().version?.split('.')?.get(0)
        val cobblemonVersion = libs.cobblemon.fabric.get().version?.split('+')?.get(0)

        inputs.property("minecraft_version", libs.minecraft.get().version)
        inputs.property("loader_version", libs.fabric.loader.get().version)
        inputs.property("architectury_version", architecturyVersion)
        inputs.property("cobblemon_version", cobblemonVersion)
        inputs.property("version", rootProject.version)
        inputs.property("mod_id", rootProject.property("mod_id"))
        inputs.property("mod_name", rootProject.property("mod_name"))
        inputs.property("mod_authors", rootProject.property("mod_authors"))
        inputs.property("mod_description", rootProject.property("mod_description"))
        inputs.property("mod_license", rootProject.property("mod_license"))

        filesMatching("fabric.mod.json") {
            expand(
                "version" to rootProject.version,
                "architectury_version" to architecturyVersion,
                "loader_version" to libs.fabric.loader.get().version,
                "minecraft_version" to libs.minecraft.get().version,
                "cobblemon_version" to cobblemonVersion,
                "mod_id" to rootProject.property("mod_id"),
                "mod_name" to rootProject.property("mod_name"),
                "mod_authors" to rootProject.property("mod_authors"),
                "mod_description" to rootProject.property("mod_description"),
                "mod_license" to rootProject.property("mod_license")
            )
        }
    }

}
