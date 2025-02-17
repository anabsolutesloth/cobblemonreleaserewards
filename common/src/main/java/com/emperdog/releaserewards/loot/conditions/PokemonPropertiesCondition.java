package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public record PokemonPropertiesCondition(String propertiesString, boolean invert) implements LootItemCondition {

    public static final MapCodec<PokemonPropertiesCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("properties").forGetter(PokemonPropertiesCondition::propertiesString),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(PokemonPropertiesCondition::invert)
    ).apply(inst, PokemonPropertiesCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.POKEMON_PROPERTIES.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        PokemonProperties pokemonProperties = pokemon.createPokemonProperties(PokemonPropertyExtractor.ALL);
        PokemonProperties argumentProperties = PokemonProperties.Companion.parse(propertiesString);
        /*
        ReleaseRewardsCommon.LOGGER.info("properties of Pokemon {}", pokemonProperties.asString(","));
        ReleaseRewardsCommon.LOGGER.info("properties of Condition {}", argumentProperties.asString(","));
        ReleaseRewardsCommon.LOGGER.info("provided properties are a subset of pokemon's properties: {}", argumentProperties.isSubSetOf(pokemonProperties));
         */

        return argumentProperties.isSubSetOf(pokemonProperties) != invert;
    }
}
