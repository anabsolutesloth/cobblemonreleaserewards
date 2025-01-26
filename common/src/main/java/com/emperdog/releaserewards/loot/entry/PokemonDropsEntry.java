package com.emperdog.releaserewards.loot.entry;

import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kotlin.ranges.IntRange;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class PokemonDropsEntry extends LootPoolSingletonContainer {

    private final int minAmount;
    private final int maxAmount;

    public static final MapCodec<PokemonDropsEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("min_amount", 1).forGetter(e -> e.minAmount),
            Codec.INT.optionalFieldOf("max_amount", 1).forGetter(e -> e.maxAmount))
            .and(singletonFields(inst)).apply(inst, PokemonDropsEntry::new));

    protected PokemonDropsEntry(int minAmount, int maxAmount, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    protected void createItemStack(@NotNull Consumer<ItemStack> consumer, @NotNull LootContext context) {
        Pokemon pokemon = context.getParam(ModLootContextParams.POKEMON);
        Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
        
        if(player instanceof ServerPlayer serverPlayer) {

            List<DropEntry> drops = pokemon.getForm().getDrops().getDrops(new IntRange(minAmount, maxAmount));

            drops.forEach(drop -> drop.drop(serverPlayer, serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer));
        } else {
            ReleaseRewardsCommon.LOGGER.warn("Player '{}' is not instanceof ServerPlayer, PokemonDropsEntry for {} cannot be created.", player.getName(), pokemon.getSpecies().resourceIdentifier);
        }
    }

    @Override
    public @NotNull LootPoolEntryType getType() {
        return ModLootEntries.POKEMON_DROPS.get();
    }
}
