package com.emperdog.releaserewards.fabric;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.emperdog.releaserewards.ReleaseHandler;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.loot.conditions.ModLootConditions;
import com.emperdog.releaserewards.loot.entry.ModLootEntries;
import com.emperdog.releaserewards.loot.modifiers.ModLootModifiers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class ReleaseRewardsFabricMod implements ModInitializer {

    @Override
    public void onInitialize() {

        ModLootConditions.LOOT_CONDITION_TYPES.register();
        ModLootEntries.LOOT_POOL_ENTRY_TYPES.register();
        ModLootModifiers.LOOT_FUNCTION_TYPES.register();

        ServerWorldEvents.LOAD.register(((server, world) -> FabricReleaseRewardsConfig.load()));

        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, ReleaseHandler::handleReleaseEvent);

        ReleaseRewardsCommon.LOGGER.info("ReleaseRewardsFabricMod Initialized");
    }
}
