package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.data.MovePredicate;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Objects.isNull;

public record MovePredicateCondition(MovePredicate move, String powerCalc, String accuracyCalc, String priorityCalc, String maxppCalc) implements LootItemCondition {

    public static final MapCodec<MovePredicateCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            MovePredicate.CODEC.fieldOf("move_predicate").forGetter(MovePredicateCondition::move),
            Codec.STRING.optionalFieldOf("power_calc", "=").forGetter(MovePredicateCondition::powerCalc),
            Codec.STRING.optionalFieldOf("accuracy_calc", "=").forGetter(MovePredicateCondition::accuracyCalc),
            Codec.STRING.optionalFieldOf("priority_calc", "=").forGetter(MovePredicateCondition::priorityCalc),
            Codec.STRING.optionalFieldOf("max_pp_calc", "=").forGetter(MovePredicateCondition::maxppCalc)
    ).apply(inst, MovePredicateCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.MOVE_PREDICATE.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Set<MoveTemplate> pokemonMoves = pokemon.getAllAccessibleMoves();

        for (MoveTemplate knownMove : pokemonMoves) {
            // Power
            if(!powerCalc.isEmpty()
                    && move.getPower() > -1.0
                    && !testPropertyCalc(powerCalc, knownMove.getPower(), move.getPower()))
                continue;

            // Accuracy
            if(!accuracyCalc.isEmpty()
                    && move.getAccuracy() > 0
                    && !testPropertyCalc(accuracyCalc, knownMove.getAccuracy(), move.getAccuracy()))
                continue;

            // Category
            if(!isNull(move.getDamageCategory())
                    && (knownMove.getDamageCategory() != move.getDamageCategory()
                    || knownMove.getDamageCategory() != move.getDamageCategory()))
                continue;

            // Target
            if(!isNull(move.getTarget())
                    &&!knownMove.getTarget().equals(move.getTarget()))
                continue;

            // Type
            ReleaseRewardsCommon.LOGGER.info(String.valueOf(knownMove.getElementalType() != move.getType()));
            if(move.getType() != ReleaseUtils.EMPTY_TYPE
                    && knownMove.getElementalType() != move.getType())
                continue;

            // Priority
            if(move.getPriority() > -99
                    && !testPropertyCalc(priorityCalc, knownMove.getPriority(), move.getPriority()))
                continue;

            // Max PP
            if(move.getMaxpp() > 0
                    && !testPropertyCalc(maxppCalc, knownMove.getPp(), move.getMaxpp()))
                continue;

            ReleaseRewardsCommon.LOGGER.info("move '{}' passed!", knownMove.getName());
            return true;
        }
        return false;
    }

    public boolean testPropertyCalc(String calcType, double knownMoveValue, double moveValue) {
        if(!calcType.isEmpty())
            switch (calcType) {
                case "greater":
                case "greater_than":
                case ">":
                    return knownMoveValue > moveValue;
                case "less":
                case "less_than":
                case "<":
                    return knownMoveValue < moveValue;
                case "equal":
                case "equals":
                case "=":
                case "==":
                    return knownMoveValue == moveValue;
                default:
                    ReleaseRewardsCommon.LOGGER.warn("MovePredicateCondition with move '{}' has invalid calc type '{}'", move, calcType);
                    break;
            }
        return false;
    }
}
