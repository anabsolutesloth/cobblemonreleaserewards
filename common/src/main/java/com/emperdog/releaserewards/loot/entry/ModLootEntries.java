package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.platform.PlatformRegistry;
import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import org.jetbrains.annotations.NotNull;

public class ModLootEntries extends PlatformRegistry<Registry<LootPoolEntryType>, ResourceKey<Registry<LootPoolEntryType>>, LootPoolEntryType> {

    public static final ModLootEntries INSTANCE = new ModLootEntries();

    private ModLootEntries() {}

    public static final LootPoolEntryType STATS_WEIGHTED =
            INSTANCE.create("stats_weighted", new LootPoolEntryType(StatsWeightedItemEntry.CODEC));

    public static final LootPoolEntryType POKEMON_DROPS =
            INSTANCE.create("pokemon_drops", new LootPoolEntryType(PokemonDropsEntry.CODEC));

    public static final LootPoolEntryType TYPE_REWARDS =
            INSTANCE.create("type_rewards", new LootPoolEntryType(TypeRewardsEntry.CODEC));

    public static final LootPoolEntryType SPECIES_REWARDS =
            INSTANCE.create("species_rewards", new LootPoolEntryType(SpeciesRewardsEntry.CODEC));


    @Override
    public <E extends LootPoolEntryType> E create(@NotNull String name, E entry) {
        return super.create(ReleaseRewards.resource(name), entry);
    }

    @Override
    public @NotNull Registry<LootPoolEntryType> getRegistry() {
        return BuiltInRegistries.LOOT_POOL_ENTRY_TYPE;
    }

    @Override
    public @NotNull ResourceKey<Registry<LootPoolEntryType>> getResourceKey() {
        return Registries.LOOT_POOL_ENTRY_TYPE;
    }
}
