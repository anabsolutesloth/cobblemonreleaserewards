package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.EnumPokemonStats;
import com.emperdog.releaserewards.ReleaseRewards;
import com.emperdog.releaserewards.loot.ModLootContextParams;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        ReleaseRewards.LOGGER.warn("StatsWeightedItemEntry '{}' was invalid, returning Empty Stack.", options.toString());
        return ItemStack.EMPTY;
    }

    public Map<EnumPokemonStats, ItemStack> getOptions() {
        return options;
    }

    public Map<Stat, Integer> getStatSubset(Pokemon pokemon) {
        switch (subset) {
            case "base":
            case "bst":
            case "base_stats":
                return pokemon.getForm().getBaseStats();
            case "effort_yield":
            case "effort_value_yield":
            case "ev_yield":
                return new HashMap<Stat, Integer>(pokemon.getForm().getEvYield());
            case "individual":
            case "individual_values":
            case "iv":
            case "ivs":
                HashMap<Stat, Integer> ivStore = new HashMap<>();
                pokemon.getIvs().forEach((entry) -> ivStore.put(entry.getKey(), entry.getValue()));
                return ivStore;
            case "effort":
            case "effort_values":
            case "ev":
            case "evs":
                HashMap<Stat, Integer> evStore = new HashMap<>();
                pokemon.getEvs().forEach((entry) -> evStore.put(entry.getKey(), entry.getValue() + 1));
                return evStore;
            case "raw":
            case "actual":
            case "final":
                HashMap<Stat, Integer> statStore = new HashMap<>();
                options.forEach((thisStat, stack) -> {
                    Stat stat = Stats.valueOf(thisStat.name());
                    statStore.put(stat, pokemon.getStat(stat));
                });
                return statStore;
            default:
                ReleaseRewards.LOGGER.warn("Stat Subset '{}' is invalid or Empty", subset);
                return null;
        }
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, LootContext context) {
        ReleaseRewards.LOGGER.info("Generating stats_weighted ItemStack!");
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Map<Stat, Integer> stats = getStatSubset(pokemon);

        consumer.accept(pickStatValueWeightedLoot(getOptions(), stats, context));
    }

    @Override
    public LootPoolEntryType getType() {
        return ModLootEntries.STATS_WEIGHTED.get();
    }
}
