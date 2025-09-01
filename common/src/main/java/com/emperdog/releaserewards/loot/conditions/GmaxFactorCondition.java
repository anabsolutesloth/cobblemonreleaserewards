package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public record GmaxFactorCondition(boolean gmax) implements LootItemCondition {

    public static MapCodec<GmaxFactorCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.BOOL.fieldOf("gmax").forGetter(GmaxFactorCondition::gmax)
    ).apply(inst, GmaxFactorCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.GMAX_FACTOR;
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        return pokemon.getGmaxFactor() == gmax;
    }
}
