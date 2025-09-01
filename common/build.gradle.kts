plugins {
    id("cobblemonreleaserewards.base-conventions")
    alias(libs.plugins.kotlin.serialization)
}

architectury {
    common("fabric", "neoforge")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.json)

    //modApi(libs.architectury)

    modImplementation(libs.fabric.loader)

    modImplementation(libs.cobblemon.common)
}
