package com.emperdog.releaserewards

import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.asIdentifierDefaultingNamespace
import net.minecraft.resources.ResourceLocation

class CobblemonHelperKt {

    companion object {
        fun getPokemonDrops(pokemon: Pokemon): List<DropEntry> {
            return pokemon.form.drops.getDrops(pokemon = pokemon);
        }

        fun defaultCobblemonIdentifier(input: String): ResourceLocation {
            return input.asIdentifierDefaultingNamespace()
        }
    }
}