package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.Moves;
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
import java.util.Set;

public record KnowsMoveCondition(List<String> moves, boolean invert) implements LootItemCondition {

    public static MapCodec<KnowsMoveCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.listOf().fieldOf("moves").forGetter(KnowsMoveCondition::moves),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(KnowsMoveCondition::invert)
    ).apply(inst, KnowsMoveCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.KNOWS_MOVE;
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Set<MoveTemplate> knownMoves = pokemon.getAllAccessibleMoves();
        for (String move : moves) {
            if (knownMoves.contains(Moves.INSTANCE.getByName(move)) != invert)
                return true;
        }
        return false;
    }
}
