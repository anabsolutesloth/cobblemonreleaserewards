package com.emperdog.releaserewards.loot.entry;

import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootEntries {
    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES =
            DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, ReleaseRewards.MODID);

    public static final Supplier<LootPoolEntryType> STATS_WEIGHTED =
            LOOT_POOL_ENTRY_TYPES.register("stats_weighted", () -> new LootPoolEntryType(StatsWeightedItemEntry.CODEC));
}
