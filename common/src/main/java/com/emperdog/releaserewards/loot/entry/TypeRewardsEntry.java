package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseHandler;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class TypeRewardsEntry extends LootPoolSingletonContainer {

    public final ElementalType type;

    public static final MapCodec<TypeRewardsEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ElementalType.getBY_STRING_CODEC().optionalFieldOf("pokemon_type", ReleaseUtils.EMPTY_TYPE).forGetter(e -> e.type))
                .and(singletonFields(inst)
            ).apply(inst, TypeRewardsEntry::new));

    protected TypeRewardsEntry(ElementalType type, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.type = type;
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        ElementalType selectedType = type;
        if(type == ReleaseUtils.EMPTY_TYPE) {
            Pokemon pokemon = lootContext.getParam(ModLootContextParams.POKEMON);
            //if type is unspecified, pick one of the mon's types at random.
            List<ElementalType> pokemonTypes = new ArrayList<>();
            pokemon.getTypes().forEach(pokemonTypes::add);
            selectedType = pokemonTypes.get(new Random().nextInt(pokemonTypes.size()));

            //pokemonTypes.forEach((monType) -> ReleaseRewardsCommon.LOGGER.info(monType.getName()));
        }

        lootContext.getResolver().get(Registries.LOOT_TABLE, ReleaseHandler.getTypeRewardTable(selectedType))
                .map(Holder::value)
                .orElse(LootTable.EMPTY)
                .getRandomItemsRaw(lootContext, stackConsumer);
    }

    @Override
    public LootPoolEntryType getType() {
        return ModLootEntries.TYPE_REWARDS.get();
    }
}
