package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record PokemonValueRangeCondition(int min, int max, String valueType) implements LootItemCondition {

    public static final MapCodec<PokemonValueRangeCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("min").fieldOf("range").forGetter(PokemonValueRangeCondition::min),
            Codec.INT.fieldOf("max").fieldOf("range").forGetter(PokemonValueRangeCondition::max),
            Codec.STRING.fieldOf("value_type").forGetter(PokemonValueRangeCondition::valueType)
    ).apply(inst, PokemonValueRangeCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.POKEMON_VALUE_RANGE.get();
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        double value = getValue(pokemon, valueType);
        return value >= min && value <= max;
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(ModLootContextParams.POKEMON);
    }

    public double getValue(Pokemon pokemon, String valueType) {
        return switch (valueType) {
            case "level" -> pokemon.getLevel(); // Level
            case "friendship" -> pokemon.getFriendship(); // Friendship
            // raw Stats
            case "hp_stat", "health_stat", "maxhp_stat", "max_hp_stat", "maxhealth_stat", "max_health_stat" ->
                    pokemon.getMaxHealth();
            case "atk_stat", "attack_stat" ->
                    pokemon.getAttack();
            case "def_stat", "defence_stat", "defense_stat" ->
                    pokemon.getDefence();
            case "spa_stat", "spatk_stat", "specialattack_stat", "special_attack_stat" ->
                    pokemon.getSpecialAttack();
            case "spd_stat", "spdef_stat", "specialdefence_stat", "specialdefense_stat", "special_defence_stat",
                 "special_defense_stat" ->
                    pokemon.getSpecialDefence();
            case "spe_stat", "speed_stat" ->
                    pokemon.getSpeed();
            // raw EVs
            case "hp_ev", "health_ev" ->
                    pokemon.getEvs().get(Stats.HP);
            case "atk_ev", "attack_ev" ->
                    pokemon.getEvs().get(Stats.ATTACK);
            case "def_ev", "defence_ev", "defense_ev" ->
                    pokemon.getEvs().get(Stats.DEFENCE);
            case "spa_ev", "spatk_ev", "specialattack_ev", "special_attack_ev" ->
                    pokemon.getEvs().get(Stats.SPECIAL_ATTACK);
            case "spd_ev", "spdef_ev", "specialdefence_ev", "specialdefense_ev", "special_defence_ev", "special_defense_ev" ->
                    pokemon.getEvs().get(Stats.SPECIAL_DEFENCE);
            case "spe_ev", "speed_ev" ->
                    pokemon.getEvs().get(Stats.SPEED);
            // raw IVs
            case "hp_iv", "health_iv" ->
                    pokemon.getIvs().get(Stats.HP);
            case "atk_iv", "attack_iv" ->
                    pokemon.getIvs().get(Stats.ATTACK);
            case "def_iv", "defence_iv", "defense_iv" ->
                    pokemon.getIvs().get(Stats.DEFENCE);
            case "spa_iv", "spatk_iv", "specialattack_iv", "special_attack_iv" ->
                    pokemon.getIvs().get(Stats.SPECIAL_ATTACK);
            case "spd_iv", "spdef_iv", "specialdefence_iv", "specialdefense_iv", "special_defence_iv", "special_defense_iv" ->
                    pokemon.getIvs().get(Stats.SPECIAL_DEFENCE);
            case "spe_iv", "speed_iv" ->
                    pokemon.getIvs().get(Stats.SPEED);
            default -> {
                ReleaseRewards.LOGGER.warn("pokemon_value type '{}' is invalid! Returning 0.", valueType);
                yield 0.0;
            }
        };
    }
}
