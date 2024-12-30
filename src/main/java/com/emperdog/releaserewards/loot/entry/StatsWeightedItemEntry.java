package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.EnumPokemonStats;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.SimpleMapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class StatsWeightedItemEntry extends LootPoolSingletonContainer {

    private final Map<EnumPokemonStats, ItemStack> options;
    private final String subset;
    private final int addedWeight;

    public static final MapCodec<StatsWeightedItemEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    statMapCodec(ItemStack.CODEC).codec().fieldOf("items").forGetter(e -> e.options),
                            Codec.STRING.fieldOf("subset").forGetter(e -> e.subset),
                            Codec.INT.optionalFieldOf("added_weight", 0).forGetter(e -> e.addedWeight))
                        .and(singletonFields(inst))
                        .apply(inst, StatsWeightedItemEntry::new)
            );

    public static <T> SimpleMapCodec<EnumPokemonStats, T> statMapCodec(Codec<T> elementCodec) {
        return Codec.simpleMap(EnumPokemonStats.CODEC, elementCodec,
                Keyable.forStrings(() ->
                    Arrays.stream(EnumPokemonStats.values()).map(EnumPokemonStats::getSerializedName)
                ));
    }


    public StatsWeightedItemEntry(Map<EnumPokemonStats, ItemStack> options, String subset, int addedWeight, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);

        this.options = options;
        this.subset = subset;
        this.addedWeight = addedWeight;
    }

    public static ItemStack pickStatValueWeightedLoot(Map<EnumPokemonStats, ItemStack> options, Map<Stat, Integer> stats, LootContext context) {
        RandomSource random = context.getRandom();
        MutableInt totalWeight = new MutableInt();
        List<ItemStack> stacks = Lists.newArrayList();

        stats.forEach((stat, value) -> {
            ItemStack current = options.get(EnumPokemonStats.valueOf(stat.getShowdownId().toUpperCase()));
            stacks.add(current);
            totalWeight.add(value);
        });

        //weight selection based on LootPool#addRandomItem(Consumer<ItemStack>, LootContext)

        int size = stacks.size();
        if(size == 1) {
            return stacks.get(0);
        } else if(totalWeight.intValue() != 0 && size != 0) {
            int weight = random.nextInt(totalWeight.intValue());

            for (EnumPokemonStats stat: options.keySet()) {
                ReleaseRewards.LOGGER.info(Stats.Companion.getStat(stat.getSerializedName()).getShowdownId());
                weight -= stats.get(Stats.Companion.getStat(stat.getSerializedName()));
                if(weight < 0) {
                    return options.get(stat);
                }
            }
        }

        ReleaseRewards.LOGGER.warn("A StatsWeightedItemEntry '{}' was invalid, returning Empty Stack.", options.toString());
        return ItemStack.EMPTY;
    }

    public Map<EnumPokemonStats, ItemStack> getOptions() {
        return options;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, LootContext context) {
        ReleaseRewards.LOGGER.info("Generating stats_weighted ItemStack!");
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Map<Stat, Integer> stats = ReleaseUtils.getStatSubset(subset, pokemon);

        consumer.accept(pickStatValueWeightedLoot(options, stats, context));
    }

    @Override
    public @NotNull LootPoolEntryType getType() {
        return ModLootEntries.STATS_WEIGHTED.get();
    }
}
