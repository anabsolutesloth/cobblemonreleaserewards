package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseHandler;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.Consumer;

public class SpeciesRewardsEntry extends LootPoolSingletonContainer {

    private static final ResourceLocation INVALID_SPECIES = ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "not_real_pokemon");

    public final ResourceLocation species;

    public static final MapCodec<SpeciesRewardsEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.optionalFieldOf("species", INVALID_SPECIES).forGetter(e -> e.species)) //TODO figure out how to make this assume cobblemon namespace if unspecified
                .and(singletonFields(inst)
            ).apply(inst, SpeciesRewardsEntry::new));

    protected SpeciesRewardsEntry(ResourceLocation species, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.species = species;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        ResourceLocation selectedSpecies = species;
        if(species == INVALID_SPECIES) {
            Pokemon pokemon = lootContext.getParam(ModLootContextParams.POKEMON);
            selectedSpecies = pokemon.getSpecies().resourceIdentifier;
        }

        lootContext.getResolver().get(Registries.LOOT_TABLE, ReleaseHandler.getSpeciesRewardTable(selectedSpecies))
                .map(Holder::value)
                .orElse(LootTable.EMPTY)
                .getRandomItemsRaw(lootContext, stackConsumer);
    }

    @Override
    public LootPoolEntryType getType() {
        return ModLootEntries.SPECIES_REWARDS.get();
    }
}
