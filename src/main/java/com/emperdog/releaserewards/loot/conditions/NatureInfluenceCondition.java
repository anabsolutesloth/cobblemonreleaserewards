package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record NatureInfluenceCondition(String influencedStat, boolean positive) implements LootItemCondition {

    public static final MapCodec<NatureInfluenceCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("stat").forGetter(NatureInfluenceCondition::influencedStat),
            Codec.BOOL.optionalFieldOf("positive", true).forGetter(NatureInfluenceCondition::positive)
    ).apply(inst, NatureInfluenceCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.NATURE_INFLUENCE.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Stat influenced = ReleaseUtils.getStatByAlias(influencedStat);
        Nature nature = pokemon.getNature();

        if(Objects.isNull(influenced) // Neutral provides null
                && Objects.isNull(nature.getIncreasedStat())
                && Objects.isNull(nature.getDecreasedStat()))
                    return true;

        return positive ?
                nature.getIncreasedStat() == influenced:
                nature.getDecreasedStat() == influenced;
    }
}
