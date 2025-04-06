package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record PokemonTypeCondition(ElementalType type, boolean invert) implements LootItemCondition {

    public static final MapCodec<PokemonTypeCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ElementalType.getBY_STRING_CODEC().fieldOf("type").forGetter(PokemonTypeCondition::type),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(PokemonTypeCondition::invert)
    ).apply(inst, PokemonTypeCondition::new));

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.POKEMON_TYPE.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        boolean pass = false;
        for (ElementalType pokemonType : pokemon.getTypes()) {
            if(pokemonType == type){
                pass = true;
                break;
            }
        }
        return pass != invert;
    }
}
