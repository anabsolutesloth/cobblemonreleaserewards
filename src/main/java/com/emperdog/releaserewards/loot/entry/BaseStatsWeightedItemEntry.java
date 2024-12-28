package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.SimpleMapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BaseStatsWeightedItemEntry extends LootPoolSingletonContainer {

    private final Map<Stat, ItemStack> options;

    public static final MapCodec<BaseStatsWeightedItemEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            statMapCodec(ItemStack.CODEC).fieldOf("items").forGetter(e -> e.options))
            .and(singletonFields(inst))
            .apply(inst, BaseStatsWeightedItemEntry::new)
    );

    public static final Codec<Stat> COBBLEMON_STAT_CODEC = Stat.Companion.getPERMANENT_ONLY_CODEC();

    public static <T> SimpleMapCodec<Stat, T> statMapCodec(Codec<T> elementCodec) {
        return Codec.simpleMap(COBBLEMON_STAT_CODEC, elementCodec,
                Keyable.forStrings(() -> Arrays.stream(Stats.values()).map(Stat::getShowdownId)));
    }

    private BaseStatsWeightedItemEntry(Map<Stat, ItemStack> options, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);

        this.options = options;
    }

    public static Builder<?> pickBstWeightedLoot(Map<Stat, ItemStack> options) {
        return simpleBuilder((weight, quality, conditions, functions) -> new BaseStatsWeightedItemEntry(options, weight, quality, conditions, functions));
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        Pokemon pokemon = lootContext.getParam(ModLootContextParams.POKEMON);

        LootPool.Builder builder = new LootPool.Builder();
        pokemon.getSpecies().getBaseStats().forEach((stat, value) -> {

        });

    }

    @Override
    public LootPoolEntryType getType() {
        return ModLootEntries.BST_WEIGHTED.get();
    }
}
