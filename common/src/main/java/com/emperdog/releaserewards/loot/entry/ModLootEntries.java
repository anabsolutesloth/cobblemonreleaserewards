package com.emperdog.releaserewards.loot.entry;

import com.emperdog.releaserewards.ReleaseRewardsCommon;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

import java.util.function.Supplier;

public class ModLootEntries {
    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES =
            DeferredRegister.create(ReleaseRewardsCommon.MODID, Registries.LOOT_POOL_ENTRY_TYPE);

    public static final Supplier<LootPoolEntryType> STATS_WEIGHTED =
            LOOT_POOL_ENTRY_TYPES.register("stats_weighted", () -> new LootPoolEntryType(StatsWeightedItemEntry.CODEC));

    public static final Supplier<LootPoolEntryType> POKEMON_DROPS =
            LOOT_POOL_ENTRY_TYPES.register("pokemon_drops", () -> new LootPoolEntryType(PokemonDropsEntry.CODEC));

    public static final Supplier<LootPoolEntryType> TYPE_REWARDS =
            LOOT_POOL_ENTRY_TYPES.register("type_rewards", () -> new LootPoolEntryType(TypeRewardsEntry.CODEC));

    public static final Supplier<LootPoolEntryType> SPECIES_REWARDS =
            LOOT_POOL_ENTRY_TYPES.register("species_rewards", () -> new LootPoolEntryType(SpeciesRewardsEntry.CODEC));
}
