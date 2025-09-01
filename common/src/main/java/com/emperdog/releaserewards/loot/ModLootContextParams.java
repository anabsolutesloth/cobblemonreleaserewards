package com.emperdog.releaserewards.loot;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

public class ModLootContextParams {

    public static void init() {
        Sets.init();
    }

    public static final LootContextParam<Pokemon> POKEMON = create("pokemon");

    private static <T> LootContextParam<T> create(String id) {
        return new LootContextParam<>(ReleaseRewards.resource(id));
    }

    public static class Sets {

        public static void init() {}

        public static final LootContextParamSet PLAYER_AND_POKEMON = register("player_and_pokemon",
                b -> b
                        .required(LootContextParams.LAST_DAMAGE_PLAYER)
                        .required(ModLootContextParams.POKEMON)
        );

        private static LootContextParamSet register(String name, Consumer<LootContextParamSet.Builder> builderConsumer){
            LootContextParamSet.Builder paramBuilder = new LootContextParamSet.Builder();
            builderConsumer.accept(paramBuilder);
            LootContextParamSet paramSet = paramBuilder.build();
            ResourceLocation location = ReleaseRewards.resource(name);
            LootContextParamSets.REGISTRY.put(location, paramSet);
            //ReleaseRewards.LOGGER.info("registered new LootContextParamSet under ID {}. Instance: {}", location, paramSet);
            return paramSet;
        }
    }

}
