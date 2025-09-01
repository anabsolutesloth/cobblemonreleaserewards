package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TeraTypeCondition(List<TeraType> teraTypes, boolean invert) implements LootItemCondition {

    public static final MapCodec<TeraTypeCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            TeraType.getBY_IDENTIFIER_CODEC().listOf().fieldOf("types").forGetter(TeraTypeCondition::teraTypes),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(TeraTypeCondition::invert)
    ).apply(inst, TeraTypeCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.TERA_TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        return teraTypes.contains(pokemon.getTeraType()) != invert;
    }
}
