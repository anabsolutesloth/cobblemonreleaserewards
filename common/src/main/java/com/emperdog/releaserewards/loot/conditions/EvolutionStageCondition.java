package com.emperdog.releaserewards.loot.conditions;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.evolution.Evolution;
import com.cobblemon.mod.common.api.pokemon.evolution.PreEvolution;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.services.Services;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Objects.isNull;

public record EvolutionStageCondition(List<Integer> stages, boolean invert) implements LootItemCondition {

    public static final MapCodec<EvolutionStageCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.listOf().fieldOf("stages").forGetter(EvolutionStageCondition::stages),
            Codec.BOOL.optionalFieldOf("invert", false).forGetter(EvolutionStageCondition::invert)
    ).apply(inst, EvolutionStageCondition::new));

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModLootConditions.EVOLUTION_STAGE.get();
    }

    @Override
    public boolean test(LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        int estimatedEvoStage = 1;
        boolean hasPreOrEvo = false;
        int iterations = 0;

        if(!isNull(pokemon.getForm().getPreEvolution())) {
            PreEvolution currentPreEvo = pokemon.getForm().getPreEvolution();
            while (!isNull(currentPreEvo)) {
                if(iterations == Services.CONFIG.getMaxPreEvoSearchDepth()) {
                    estimatedEvoStage = 0;
                    break;
                }
                ++estimatedEvoStage;

                if (isNull(currentPreEvo.getForm().getPreEvolution()))
                    break;

                currentPreEvo = currentPreEvo.getForm().getPreEvolution();
                ++iterations;
            }
            hasPreOrEvo = true;
        }

        Set<Evolution> evos = pokemon.getForm().getEvolutions();
        iterations = 0;
        while (!evos.isEmpty()) {
            //break condition for Addons implementing Form Changes as Pokemon that evolve into Forms of themselves
            if(iterations == Services.CONFIG.getMaxEvoSearchDepth()) {
                estimatedEvoStage = 0;
                break;
            }
            // create storage for possible evos
            Set<Evolution> nextEvos = new HashSet<>();
            // iterate through evos
            for (Evolution evo : evos) {
                // get species of evo
                String evoSpeciesName = evo.getResult().getSpecies();
                assert evoSpeciesName != null;

                ResourceLocation evoSpecies = ResourceLocation.tryParse(evoSpeciesName);
                if(isNull(PokemonSpecies.INSTANCE.getByIdentifier(evoSpecies)))
                    evoSpecies = ResourceLocation.fromNamespaceAndPath(Cobblemon.MODID, evoSpeciesName);

                // get evos of evolved form
                Set<Evolution> evoEvos = PokemonSpecies.INSTANCE.getByIdentifier(evoSpecies).getForm(evo.getResult().getAspects()).getEvolutions();
                if(!evoEvos.isEmpty())
                    nextEvos.addAll(evoEvos);
            }
            evos = nextEvos;
            hasPreOrEvo = true;
            ++iterations;
        }

        if(!hasPreOrEvo)
            --estimatedEvoStage;
        //ReleaseRewardsCommon.LOGGER.info("{} = {}", pokemon.getSpecies().resourceIdentifier, estimatedEvoStage);
        return stages.contains(estimatedEvoStage) != invert;
    }
}
