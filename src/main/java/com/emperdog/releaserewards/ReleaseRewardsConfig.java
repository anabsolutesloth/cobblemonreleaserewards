package com.emperdog.releaserewards;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ReleaseRewards.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ReleaseRewardsConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_PREEVO_SEARCH_DEPTH = BUILDER
            .comment("Max search depth for EvolutionStageCondition finding Pre-Evolutions of species.",
                    "Prevents crashes from recursive Pre-Evolutions.",
                    "Why? why anyone would do that is beyond me, but it exists somwhere.",
                    "If this activates, the Species will be considered Unevolving")
            .defineInRange("maxPreEvoSearchDepth", 10, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_EVO_SEARCH_DEPTH = BUILDER
            .comment("Max search depth for EvolutionStageCondition finding Evolutions of species.",
                    "Prevents crashes from recursive Evolutions.",
                    "Why? Some addons implement Form Changes for species such as Deoxys, as Evolutions into an alternate Form.",
                    "If this activates, the Species will be considered Unevolving")
            .defineInRange("maxEvoSearchDepth", 10, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int maxPreEvoSearchDepth;
    public static int maxEvoSearchDepth;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        assignConfigValues();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Reloading event) {
        assignConfigValues();
    }

    private static void assignConfigValues() {
        maxPreEvoSearchDepth = MAX_PREEVO_SEARCH_DEPTH.get();
        maxEvoSearchDepth = MAX_EVO_SEARCH_DEPTH.get();
    }
}
