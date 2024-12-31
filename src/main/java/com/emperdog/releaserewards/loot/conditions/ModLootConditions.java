package com.emperdog.releaserewards.loot.conditions;

import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES =
            DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, ReleaseRewards.MODID);

    public static final Supplier<LootItemConditionType> POKEMON_VALUE_RANGE =
            LOOT_CONDITION_TYPES.register("value_range", () -> new LootItemConditionType(PokemonValueRangeCondition.CODEC));

    public static final Supplier<LootItemConditionType> POKEMON_LABEL =
            LOOT_CONDITION_TYPES.register("pokemon_label", () -> new LootItemConditionType(PokemonLabelCondition.CODEC));

    public static final Supplier<LootItemConditionType> POKEMON_FORM =
            LOOT_CONDITION_TYPES.register("form", () -> new LootItemConditionType(PokemonFormCondition.CODEC));
}