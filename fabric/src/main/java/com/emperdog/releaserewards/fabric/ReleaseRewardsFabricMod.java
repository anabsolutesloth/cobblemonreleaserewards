package com.emperdog.releaserewards.fabric;

import com.cobblemon.mod.common.platform.PlatformRegistry;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.ReleaseRewardsMod;
import com.emperdog.releaserewards.loot.conditions.ModLootConditions;
import com.emperdog.releaserewards.loot.entry.ModLootEntries;
import com.emperdog.releaserewards.loot.modifiers.ModLootFunctions;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ReleaseRewardsFabricMod implements ModInitializer, ReleaseRewardsMod {

    @Override
    public void onInitialize() {

        ReleaseRewards.preInit(this);

        ServerWorldEvents.LOAD.register(((server, world) -> FabricReleaseRewardsConfig.load()));
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
        platformRegistry.register((id, object) -> {
            //ReleaseRewards.LOGGER.info("Registering {} as {}", object, id);
            Registry.register(platformRegistry.getRegistry(), id, object);
            return Unit.INSTANCE;
        });
    }
}
