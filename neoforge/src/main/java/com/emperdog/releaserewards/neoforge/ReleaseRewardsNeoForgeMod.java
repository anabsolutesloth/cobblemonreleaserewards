package com.emperdog.releaserewards.neoforge;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.emperdog.releaserewards.ReleaseHandler;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.loot.conditions.ModLootConditions;
import com.emperdog.releaserewards.loot.entry.ModLootEntries;
import com.emperdog.releaserewards.loot.modifiers.ModLootModifiers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ReleaseRewardsCommon.MODID)
public class ReleaseRewardsNeoForgeMod {

    public ReleaseRewardsNeoForgeMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModLootConditions.LOOT_CONDITION_TYPES.register();
        ModLootEntries.LOOT_POOL_ENTRY_TYPES.register();
        ModLootModifiers.LOOT_FUNCTION_TYPES.register();

        modContainer.registerConfig(ModConfig.Type.SERVER, NeoForgeReleaseRewardsConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, ReleaseHandler::handleReleaseEvent);
    }
}
