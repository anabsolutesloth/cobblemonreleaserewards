package com.emperdog.releaserewards.loot;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

public class ModLootContextParams {

    public static final LootContextParam<Pokemon> POKEMON = create("pokemon");

    private static <T> LootContextParam<T> create(String id) {
        return new LootContextParam<>(ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, id));
    }

    public static class Set {
        public static final LootContextParamSet PLAYER_AND_POKEMON = register("player_and_pokemon",
                builder -> builder
                        .required(LootContextParams.LAST_DAMAGE_PLAYER)
                        .required(ModLootContextParams.POKEMON)
        );
    }

    private static LootContextParamSet register(String name, Consumer<LootContextParamSet.Builder> builderConsumer){
        LootContextParamSet.Builder paramBuilder = new LootContextParamSet.Builder();
        builderConsumer.accept(paramBuilder);
        LootContextParamSet paramSet = paramBuilder.build();
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, name);
        return paramSet;
    }

}
