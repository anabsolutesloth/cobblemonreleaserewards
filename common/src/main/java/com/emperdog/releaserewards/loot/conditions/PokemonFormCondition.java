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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PokemonFormCondition(List<String> aspects, boolean invert) implements LootItemCondition {

    public static final MapCodec<PokemonFormCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.listOf().fieldOf("aspects").forGetter(PokemonFormCondition::aspects),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(PokemonFormCondition::invert)
    ).apply(inst, PokemonFormCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.POKEMON_FORM.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        return new HashSet<>(pokemon.getForm().getAspects()).containsAll(aspects) != invert;
    }

    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(ModLootContextParams.POKEMON);
    }
}
