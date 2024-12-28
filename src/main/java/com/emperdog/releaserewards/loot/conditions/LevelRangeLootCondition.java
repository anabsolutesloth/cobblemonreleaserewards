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

public record LevelRangeLootCondition(int min, int max) implements LootItemCondition {

    public static final MapCodec<LevelRangeLootCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("min").fieldOf("range").forGetter(LevelRangeLootCondition::min),
            Codec.INT.fieldOf("max").fieldOf("range").forGetter(LevelRangeLootCondition::max)
    ).apply(inst, LevelRangeLootCondition::new));

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
