package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record MovePredicateCondition(MoveTemplate move, String powerCalc, String accuracyCalc) implements LootItemCondition {

    public static final Map<String, String> categoryAlias = Map.of("physical", "phys", "special", "spec", "status", "stat");

    public static final MapCodec<MovePredicateCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            MoveTemplate.getBY_STRING_CODEC().fieldOf("move_template").forGetter(MovePredicateCondition::move),
            Codec.STRING.optionalFieldOf("power_calc", "").forGetter(MovePredicateCondition::powerCalc),
            Codec.STRING.optionalFieldOf("accuracy_calc", "").forGetter(MovePredicateCondition::accuracyCalc)
    ).apply(inst, MovePredicateCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.KNOWS_MOVE.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Set<MoveTemplate> pokemonMoves = pokemon.getAllAccessibleMoves();
        ReleaseRewards.LOGGER.info("Running KnowsMoveCondition for Move '{}'", move);

        for (MoveTemplate knownMove : pokemonMoves) {
            boolean power = true;
            boolean accuracy = true;

            // Showdown ID
            if(!move.getName().isEmpty() || knownMove != move)
                continue;
            ReleaseRewards.LOGGER.info("Move name '{}' is unspecified or matches!", knownMove.getName());

            // Power
            if(!testPropertyCalc("power_calc", knownMove.getPower(), move.getPower()))
                continue;
            ReleaseRewards.LOGGER.info("Power '{}' is unspecified or within bounds!", knownMove.getPower());

            // Accuracy
            if(!testPropertyCalc("accuracy_calc", knownMove.getAccuracy(), move.getAccuracy()))
                continue;
            ReleaseRewards.LOGGER.info("Accuracy '{}' is unspecified or within bounds!", knownMove.getAccuracy());

            // Category
            if(!move.getDamageCategory().getName().isEmpty()
                    && (!knownMove.getDamageCategory().getName().equals(move.getDamageCategory().getName())
                        || !categoryAlias.get(knownMove.getDamageCategory().getName()).equals(move.getDamageCategory().getName())))
                    continue;

            ReleaseRewards.LOGGER.info("Known Move '{}' matches Move '{}'", knownMove.getName(), move);
            return true;
        }
        ReleaseRewards.LOGGER.info("No moves matched!");
        return false;
    }

    private void propertyWarning(String propSpec) {
        ReleaseRewards.LOGGER.warn("KnowsMoveCondition with move '{}' has invalid calc type '{}'", move, propSpec);
    }

    public boolean testPropertyCalc(String calcType, double knownMoveValue, double moveValue) {
        if(!calcType.isEmpty())
            switch (calcType) {
                case "greater":
                case "greater_than":
                case ">":
                    ReleaseRewards.LOGGER.info("reached Greater {} calculation", calcType);
                    return knownMoveValue > moveValue;
                case "less":
                case "less_than":
                case "<":
                    ReleaseRewards.LOGGER.info("reached Less {} calculation", calcType);
                    return knownMoveValue < moveValue;
                case "equal":
                case "equals":
                case "=":
                    ReleaseRewards.LOGGER.info("reached Equal {} calculation", calcType);
                    return knownMoveValue == moveValue;
                default:
                    propertyWarning(calcType);
                    break;
            }
        return false;
    }
}
