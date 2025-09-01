package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record PokemonGenderCondition(String gender) implements LootItemCondition {

    public static final MapCodec<PokemonGenderCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("gender").forGetter(PokemonGenderCondition::gender)
    ).apply(inst, PokemonGenderCondition::new));

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.POKEMON_GENDER;
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        return pokemon.getGender().equals(getGenderFromAlias(gender));
    }

    private Gender getGenderFromAlias(String gender) {
        return switch (gender.toLowerCase()) {
            case "m", "male" -> Gender.MALE;
            case "f", "female" -> Gender.FEMALE;
            case "n", "none", "genderless" -> Gender.GENDERLESS;
            default -> {
                ReleaseRewards.LOGGER.warn("pokemon_gender condition with Gender '{}' does not reference a valid gender.", gender);
                yield Gender.GENDERLESS;
            }
        };
    }
}
