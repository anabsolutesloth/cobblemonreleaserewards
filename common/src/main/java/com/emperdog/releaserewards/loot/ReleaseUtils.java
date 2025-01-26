package com.emperdog.releaserewards.loot;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.api.moves.categories.DamageCategory;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.battles.MoveTarget;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ReleaseUtils {

    public static final ElementalType EMPTY_TYPE =
            new ElementalType("bird", Component.empty(), 0, 0, ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "empty"));
    /*
        Returns the specified Stat Subset of a Pokémon as a Map.
        can return an Empty map.
    */
    public static Map<Stat, Integer> getStatSubset(String subset, Pokemon pokemon) {
        switch (subset) {
            case "base", "bst", "base_stats":
                return pokemon.getForm().getBaseStats();
            case "effort_yield", "effort_value_yield", "ev_yield":
                return pokemon.getForm().getEvYield();
            case "individual", "individual_values", "iv", "ivs":
                HashMap<Stat, Integer> ivStore = new HashMap<>();
                pokemon.getIvs().forEach((stat) -> ivStore.put(stat.getKey(), stat.getValue()));
                return ivStore;
            case "effort", "effort_values", "ev", "evs":
                HashMap<Stat, Integer> evStore = new HashMap<>();
                pokemon.getEvs().forEach((stat) -> evStore.put(stat.getKey(), stat.getValue() + 1));
                return evStore;
            case "raw", "actual", "final":
                HashMap<Stat, Integer> statStore = new HashMap<>();
                Stats.Companion.getPERMANENT().forEach(thisStat -> statStore.put(thisStat, pokemon.getStat(thisStat)));
                return statStore;
            default:
                ReleaseRewardsCommon.LOGGER.warn("Stat Subset name '{}' is invalid or Empty, returning an empty Subset.", subset);
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
            default -> throw new IllegalArgumentException("'"+ statName +"' is not a valid alias for any Stat!");
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
        return ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "rewards/species/"+ speciesLocation.getNamespace() +"/"+ speciesLocation.getPath());
    }

    public static DamageCategory getDamageCategoryFromAlias(String category) {
        return switch (category) {
            case "phys", "physical" -> DamageCategories.INSTANCE.getPHYSICAL();
            case "spec", "special" -> DamageCategories.INSTANCE.getSPECIAL();
            case "stat", "status" -> DamageCategories.INSTANCE.getSTATUS();
            case "any" -> null;
            default -> throw new IllegalArgumentException("DamageCategory '"+ category +"' is not an alias for any category!");
        };
    }

    public static MoveTarget getMoveTargetFromAlias(String target) {
        return switch (target) {
            case "any" -> MoveTarget.any;
            case "all", "everyone" -> MoveTarget.all;
            case "allAdjacent", "all_adjacent", "everyoneAdjacent", "everyone_adjacent" -> MoveTarget.allAdjacent;
            case "self", "this" -> MoveTarget.self;
            case "normal", "any_adjacent", "anyAdjacent" -> MoveTarget.normal;
            case "randomNormal", "random_normal", "random_adjacent", "randomAdjacent" -> MoveTarget.randomNormal;
            case "allies", "allAllies" -> MoveTarget.allies;
            case "allySide", "ally_side", "allyField", "ally_field" -> MoveTarget.allySide;
            case "allyTeam", "ally_team" -> MoveTarget.allyTeam;
            case "adjacentAlly", "adjacent_ally" -> MoveTarget.adjacentAlly;
            case "adjacentAllyOrSelf", "adjacent_ally_or_self" -> MoveTarget.adjacentAllyOrSelf;
            case "adjacentFoe", "adjacent_foe", "adjacentEnemy", "adjacent_enemy" -> MoveTarget.adjacentFoe;
            case "foeSide", "foe_side", "foeField", "foe_field" -> MoveTarget.foeSide;
            case "scripted", "other" -> MoveTarget.scripted;
            default -> throw new IllegalArgumentException("MoveTarget '"+ target +"' is not an alias for any target!");
        };
    }
}
