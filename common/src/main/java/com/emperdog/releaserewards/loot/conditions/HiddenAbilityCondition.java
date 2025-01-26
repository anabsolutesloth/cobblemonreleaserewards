package com.emperdog.releaserewards.loot.conditions;

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

public record HiddenAbilityCondition(boolean hiddenAbility) implements LootItemCondition {

    public static final MapCodec<HiddenAbilityCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.BOOL.fieldOf("has_hidden_ability").forGetter(HiddenAbilityCondition::hiddenAbility)
    ).apply(inst, HiddenAbilityCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.HIDDEN_ABILITY.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        return ReleaseUtils.hasHiddenAbility(pokemon, hiddenAbility);
    }
}
