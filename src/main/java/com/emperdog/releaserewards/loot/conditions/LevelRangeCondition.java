package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.pokemon.Pokemon;
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

public record LevelRangeCondition(int min, int max) implements LootItemCondition {

    public static final MapCodec<LevelRangeCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("min").fieldOf("range").forGetter(LevelRangeCondition::min),
            Codec.INT.fieldOf("max").fieldOf("range").forGetter(LevelRangeCondition::max)
    ).apply(inst, LevelRangeCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.LEVEL_RANGE.get();
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        return pokemon.getLevel() >= min && pokemon.getLevel() <= max;
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(ModLootContextParams.POKEMON);
    }
}
