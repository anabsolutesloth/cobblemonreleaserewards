package com.emperdog.releaserewards.loot.entry;

import com.emperdog.releaserewards.ReleaseRewards;
import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModLootEntries {
    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES =
            DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, ReleaseRewards.MODID);

    public static final Supplier<LootPoolEntryType> BST_WEIGHTED =
            LOOT_POOL_ENTRY_TYPES.register("bst_weighted", () -> new LootPoolEntryType(BaseStatsWeightedItemEntry.CODEC));
}
