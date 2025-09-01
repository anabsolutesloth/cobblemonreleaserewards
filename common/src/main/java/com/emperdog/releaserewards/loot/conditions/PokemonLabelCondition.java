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

import java.util.List;
import java.util.Set;

public record PokemonLabelCondition(List<String> labels, boolean invert) implements LootItemCondition {

    public static final MapCodec<PokemonLabelCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.listOf().fieldOf("labels").forGetter(PokemonLabelCondition::labels),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(PokemonLabelCondition::invert)
    ).apply(inst, PokemonLabelCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.POKEMON_LABEL;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        boolean hasLabel = false;
        for (String label : labels) {
            if (pokemon.hasLabels(label)) {
                hasLabel = true;
                break;
            }
        }
        return hasLabel != invert;
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(ModLootContextParams.POKEMON);
    }
}
