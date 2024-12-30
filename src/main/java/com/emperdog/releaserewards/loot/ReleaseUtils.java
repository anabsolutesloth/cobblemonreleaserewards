package com.emperdog.releaserewards.loot;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewards;

import java.util.HashMap;
import java.util.Map;

public class ReleaseUtils {
    /*
        Returns the specified Stat Subset of a Pokémon as a Map.
        can return an Empty map.
     */
    public static Map<Stat, Integer> getStatSubset(String subset, Pokemon pokemon) {
        switch (subset) {
            case "base":
            case "bst":
            case "base_stats":
                return pokemon.getForm().getBaseStats();
            case "effort_yield":
            case "effort_value_yield":
            case "ev_yield":
                return pokemon.getForm().getEvYield();
            case "individual":
            case "individual_values":
            case "iv":
            case "ivs":
                HashMap<Stat, Integer> ivStore = new HashMap<>();
                pokemon.getIvs().forEach((stat) -> {
                    ivStore.put(stat.getKey(), stat.getValue());
                });
                return ivStore;
            case "effort":
            case "effort_values":
            case "ev":
            case "evs":
                HashMap<Stat, Integer> evStore = new HashMap<>();
                pokemon.getEvs().forEach((stat) -> {
                    evStore.put(stat.getKey(), stat.getValue() + 1);
                });
                return evStore;
            case "raw":
            case "actual":
            case "final":
                HashMap<Stat, Integer> statStore = new HashMap<>();
                Stats.Companion.getPERMANENT().forEach(thisStat -> {
                    statStore.put(thisStat, pokemon.getStat(thisStat));
                });
                return statStore;
            default:
                ReleaseRewards.LOGGER.warn("Stat Subset name '{}' is invalid or Empty, returning an empty Subset.", subset);
                return new HashMap<>();
        }
    }
}
