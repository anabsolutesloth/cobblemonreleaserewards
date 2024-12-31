package com.emperdog.releaserewards.loot;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;
import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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
                pokemon.getIvs().forEach((stat) -> ivStore.put(stat.getKey(), stat.getValue()));
                return ivStore;
            case "effort":
            case "effort_values":
            case "ev":
            case "evs":
                HashMap<Stat, Integer> evStore = new HashMap<>();
                pokemon.getEvs().forEach((stat) -> evStore.put(stat.getKey(), stat.getValue() + 1));
                return evStore;
            case "raw":
            case "actual":
            case "final":
                HashMap<Stat, Integer> statStore = new HashMap<>();
                Stats.Companion.getPERMANENT().forEach(thisStat -> statStore.put(thisStat, pokemon.getStat(thisStat)));
                return statStore;
            default:
                ReleaseRewards.LOGGER.warn("Stat Subset name '{}' is invalid or Empty, returning an empty Subset.", subset);
                return new HashMap<>();
        }
    }

    @Nullable
    public static Stat getStatByAlias(String statName) {
        return switch (statName) {
            case "hp", "health", "maxhp", "maxhealth", "max_hp", "max_health" ->
                    Stats.HP;
            case "atk", "attack" ->
                    Stats.ATTACK;
            case "def", "defence", "defense" ->
                    Stats.DEFENCE;
            case "spa", "spatk", "specialattack", "special_attack" ->
                    Stats.SPECIAL_ATTACK;
            case "spd", "spdef", "specialdefence", "specialdefense", "special_defence", "special_defense" ->
                    Stats.SPECIAL_DEFENCE;
            case "spe", "speed" ->
                Stats.SPEED;
            case "none", "null" ->
                null;
            default -> {
                ReleaseRewards.LOGGER.warn("'{}' is not a valid alias for any Stat! Returning null.", statName);
                yield null;
            }
        };
    }

    public static boolean hasHiddenAbility(Pokemon pokemon, boolean wantsHidden) {
        for (PotentialAbility ability : pokemon.getForm().getAbilities()) {
            if(ability instanceof HiddenAbility) {
                return (pokemon.getAbility().getTemplate() == ability.getTemplate()) == wantsHidden;
            }
        }
        return false;
    }

    public static ResourceLocation getSpeciesTableLocation(ResourceLocation speciesLocation) {
        return ResourceLocation.fromNamespaceAndPath(ReleaseRewards.MODID, "rewards/species/"+ speciesLocation.getNamespace() +"/"+ speciesLocation.getPath());
    }
}
