package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public record PokemonValueRangeCondition(int min, int max, String valueType) implements LootItemCondition {

    public static final MapCodec<PokemonValueRangeCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("min").fieldOf("range").forGetter(PokemonValueRangeCondition::min),
            Codec.INT.fieldOf("max").fieldOf("range").forGetter(PokemonValueRangeCondition::max),
            Codec.STRING.fieldOf("value_type").forGetter(PokemonValueRangeCondition::valueType)
    ).apply(inst, PokemonValueRangeCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.POKEMON_VALUE_RANGE;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        String[] valueDetails = valueType.split("_");
        double value = getValue(pokemon, valueDetails[0],
                valueDetails.length > 1 ? valueDetails[1] : "");
        return value >= min && value <= max;
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(ModLootContextParams.POKEMON);
    }

    public double getValue(Pokemon pokemon, String valueType, String suffix) {
        boolean forStat = false;
        double value;

        switch (valueType) {
            case "level" -> value = pokemon.getLevel(); // Level
            case "friendship" -> value = pokemon.getFriendship(); // Friendship
            default -> {
                if(valueType.endsWith("stat") || valueType.endsWith("iv") || valueType.endsWith("ev"))
                    forStat = true;
                value = 0.0;
            }
        }

        if(!forStat) return value;

        return switch (Objects.requireNonNull(ReleaseUtils.getStatByAlias(valueType)).getShowdownId() +"_"+ suffix) {
            //raw Stats
            case "hp_stat" -> pokemon.getMaxHealth();
            case "atk_stat" -> pokemon.getAttack();
            case "def_stat" -> pokemon.getDefence();
            case "spa_stat" -> pokemon.getSpecialAttack();
            case "spd_stat" -> pokemon.getSpecialDefence();
            case "spe_stat" -> pokemon.getSpeed();

            // raw EVs
            case "hp_ev" -> pokemon.getEvs().get(Stats.HP);
            case "atk_ev" -> pokemon.getEvs().get(Stats.ATTACK);
            case "def_ev" -> pokemon.getEvs().get(Stats.DEFENCE);
            case "spa_ev" -> pokemon.getEvs().get(Stats.SPECIAL_ATTACK);
            case "spd_ev" -> pokemon.getEvs().get(Stats.SPECIAL_DEFENCE);
            case "spe_ev" -> pokemon.getEvs().get(Stats.SPEED);

            // raw IVs
            case "hp_iv" -> pokemon.getIvs().get(Stats.HP);
            case "atk_iv" -> pokemon.getIvs().get(Stats.ATTACK);
            case "def_iv" -> pokemon.getIvs().get(Stats.DEFENCE);
            case "spa_iv" -> pokemon.getIvs().get(Stats.SPECIAL_ATTACK);
            case "spd_iv" -> pokemon.getIvs().get(Stats.SPECIAL_DEFENCE);
            case "spe_iv" -> pokemon.getIvs().get(Stats.SPEED);

            default -> throw new IllegalStateException("Unexpected value: " + ReleaseUtils.getStatByAlias(valueType));
        };
    }
}
