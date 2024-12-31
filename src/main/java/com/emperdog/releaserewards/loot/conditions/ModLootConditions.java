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

    public static final Supplier<LootItemConditionType> MOVE_PREDICATE =
            LOOT_CONDITION_TYPES.register("move_predicate", () -> new LootItemConditionType(MovePredicateCondition.CODEC));

    public static final Supplier<LootItemConditionType> POKEMON_GENDER =
            LOOT_CONDITION_TYPES.register("pokemon_gender", () -> new LootItemConditionType(PokemonGenderCondition.CODEC));

    public static final Supplier<LootItemConditionType> HIDDEN_ABILITY =
            LOOT_CONDITION_TYPES.register("hidden_ability", () -> new LootItemConditionType(HiddenAbilityCondition.CODEC));

    public static final Supplier<LootItemConditionType> NATURE_INFLUENCE =
            LOOT_CONDITION_TYPES.register("nature_influence", () -> new LootItemConditionType(NatureInfluenceCondition.CODEC));

    public static final Supplier<LootItemConditionType> KNOWS_MOVE =
            LOOT_CONDITION_TYPES.register("knows_move", () -> new LootItemConditionType(KnowsMoveCondition.CODEC));

    public static final Supplier<LootItemConditionType> TERA_TYPE =
            LOOT_CONDITION_TYPES.register("tera_type", () -> new LootItemConditionType(TeraTypeCondition.CODEC));

    public static final Supplier<LootItemConditionType> GMAX_FACTOR =
            LOOT_CONDITION_TYPES.register("gmax_factor", () -> new LootItemConditionType(GmaxFactorCondition.CODEC));
}
