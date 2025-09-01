package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.platform.PlatformRegistry;
import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public class ModLootConditions extends PlatformRegistry<Registry<LootItemConditionType>, ResourceKey<Registry<LootItemConditionType>>, LootItemConditionType> {

    public static final ModLootConditions INSTANCE = new ModLootConditions();

    private ModLootConditions() {}

    public static final LootItemConditionType POKEMON_VALUE_RANGE =
            INSTANCE.create("value_range",  new LootItemConditionType(PokemonValueRangeCondition.CODEC));

    public static final LootItemConditionType POKEMON_LABEL =
            INSTANCE.create("pokemon_label",  new LootItemConditionType(PokemonLabelCondition.CODEC));

    public static final LootItemConditionType POKEMON_FORM =
            INSTANCE.create("form",  new LootItemConditionType(PokemonFormCondition.CODEC));

    public static final LootItemConditionType MOVE_PREDICATE =
            INSTANCE.create("move_predicate",  new LootItemConditionType(MovePredicateCondition.CODEC));

    public static final LootItemConditionType POKEMON_GENDER =
            INSTANCE.create("pokemon_gender",  new LootItemConditionType(PokemonGenderCondition.CODEC));

    public static final LootItemConditionType HIDDEN_ABILITY =
            INSTANCE.create("hidden_ability",  new LootItemConditionType(HiddenAbilityCondition.CODEC));

    public static final LootItemConditionType NATURE_INFLUENCE =
            INSTANCE.create("nature_influence",  new LootItemConditionType(NatureInfluenceCondition.CODEC));

    public static final LootItemConditionType KNOWS_MOVE =
            INSTANCE.create("knows_move",  new LootItemConditionType(KnowsMoveCondition.CODEC));

    public static final LootItemConditionType TERA_TYPE =
            INSTANCE.create("tera_type",  new LootItemConditionType(TeraTypeCondition.CODEC));

    public static final LootItemConditionType GMAX_FACTOR =
            INSTANCE.create("gmax_factor",  new LootItemConditionType(GmaxFactorCondition.CODEC));

    public static final LootItemConditionType EVOLUTION_STAGE =
            INSTANCE.create("evolution_stage",  new LootItemConditionType(EvolutionStageCondition.CODEC));

    public static final LootItemConditionType SHINY =
            INSTANCE.create("shiny",  new LootItemConditionType(ShinyCondition.CODEC));

    public static final LootItemConditionType POKEMON_PROPERTIES =
            INSTANCE.create("pokemon_properties",  new LootItemConditionType(PokemonPropertiesCondition.CODEC));

    public static final LootItemConditionType POKEMON_TYPE =
            INSTANCE.create("type",  new LootItemConditionType(PokemonTypeCondition.CODEC));


    @Override
    public <E extends LootItemConditionType> E create(@NotNull String name, E entry) {
        return super.create(ReleaseRewards.resource(name), entry);
    }

    @Override
    public @NotNull Registry<LootItemConditionType> getRegistry() {
        return BuiltInRegistries.LOOT_CONDITION_TYPE;
    }

    @Override
    public @NotNull ResourceKey<Registry<LootItemConditionType>> getResourceKey() {
        return Registries.LOOT_CONDITION_TYPE;
    }
}
