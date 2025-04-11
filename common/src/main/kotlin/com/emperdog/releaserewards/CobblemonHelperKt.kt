package com.emperdog.releaserewards

import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.pokemon.Pokemon

class CobblemonHelperKt {

    companion object {
        fun getPokemonDrops(pokemon: Pokemon): List<DropEntry> {
            return pokemon.form.drops.getDrops(pokemon = pokemon);
        }
    }
}