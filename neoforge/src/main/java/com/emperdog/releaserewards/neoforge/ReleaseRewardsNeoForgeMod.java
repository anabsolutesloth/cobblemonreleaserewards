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
public class ReleaseRewardsNeoForgeMod
{
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ReleaseRewardsNeoForgeMod(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        //NeoForge.EVENT_BUS.register(this);

        ModLootConditions.LOOT_CONDITION_TYPES.register();
        ModLootEntries.LOOT_POOL_ENTRY_TYPES.register();
        ModLootModifiers.LOOT_FUNCTION_TYPES.register();

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, NeoForgeReleaseRewardsConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, ReleaseHandler::handleReleaseEvent);
    }
}
