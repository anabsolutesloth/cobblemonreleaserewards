package com.emperdog.releaserewards.neoforge;

import com.cobblemon.mod.common.platform.PlatformRegistry;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.ReleaseRewardsMod;
import com.emperdog.releaserewards.loot.conditions.ModLootConditions;
import com.emperdog.releaserewards.loot.entry.ModLootEntries;
import com.emperdog.releaserewards.loot.modifiers.ModLootFunctions;

import kotlin.Unit;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ReleaseRewards.MODID)
public class ReleaseRewardsNeoForgeMod implements ReleaseRewardsMod {

    private final IEventBus modBus;

    public ReleaseRewardsNeoForgeMod(IEventBus modEventBus, ModContainer modContainer) {
        this.modBus = modEventBus;

        ReleaseRewards.preInit(this);

        modContainer.registerConfig(ModConfig.Type.SERVER, NeoForgeReleaseRewardsConfig.SPEC);
    }

    @Override
    public void registerLootConditions() {
        registerEntries(ModLootConditions.INSTANCE);
    }

    @Override
    public void registerLootFunctions() {
        registerEntries(ModLootFunctions.INSTANCE);
    }

    @Override
    public void registerLootEntries() {
        registerEntries(ModLootEntries.INSTANCE);
    }

    private <T> void registerEntries(PlatformRegistry<Registry<T>, ResourceKey<Registry<T>>, T> platformRegistry) {
        modBus.addListener(RegisterEvent.class, event ->
                event.register(platformRegistry.getResourceKey(), helper ->
                        platformRegistry.register((id, object) -> {
                            //ReleaseRewards.LOGGER.info("Registering {} as {}", object, id);
                            helper.register(id, object);
                            return Unit.INSTANCE;
                        })));
    }
}
