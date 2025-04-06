package com.emperdog.releaserewards;

import com.cobblemon.mod.common.api.events.storage.ReleasePokemonEvent;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import kotlin.Unit;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.*;
import java.util.stream.Collectors;

public class ReleaseHandler {
    // global Reward Table
    public static final ResourceKey<LootTable> GLOBAL_REWARD_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "rewards/global"));

    // full list of Type tables mapped to corresponding ElementalType
    public static final Map<ElementalType, ResourceKey<LootTable>> TYPE_REWARD_TABLES = ElementalTypes.INSTANCE.all().stream()
            .collect(Collectors.toMap(
                    type -> type,
                    type -> ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "rewards/types/" + type.getName()))
            ));

    // stores generated species table ResourceKeys because expendive to create (supposedly)
    public static HashMap<ResourceLocation, ResourceKey<LootTable>> STORED_SPECIES_REWARD_TABLES = new HashMap<>();

    public static Unit handleReleaseEvent(ReleasePokemonEvent event) {
        ServerPlayer player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();
        Level level = player.level();
        ReloadableServerRegistries.Holder reloadableRegistries = Objects.requireNonNull(player.getServer()).reloadableRegistries();

        //get Global table
        LootTable globalTable = reloadableRegistries.getLootTable(GLOBAL_REWARD_TABLE);

        //create LootContextParams for this context
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) level);
        builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
        builder.withParameter(ModLootContextParams.POKEMON, pokemon);
        LootParams params = builder.create(ModLootContextParams.Set.PLAYER_AND_POKEMON);

        giveLootToPlayer(player, globalTable.getRandomItems(params));

        return Unit.INSTANCE;
    }

    public static ResourceKey<LootTable> getGlobalRewards() {
        return GLOBAL_REWARD_TABLE;
    }

    public static ResourceKey<LootTable> getTypeRewardTable(ElementalType type) {
        return TYPE_REWARD_TABLES.get(type);
    }

    public static ResourceKey<LootTable> getSpeciesRewardTable(Pokemon pokemon) {
        return getSpeciesRewardTable(pokemon.getSpecies().resourceIdentifier);
    }

    public static ResourceKey<LootTable> getSpeciesRewardTable(ResourceLocation speciesLocation) {
        // check for species table in storage
        if(STORED_SPECIES_REWARD_TABLES.containsKey(speciesLocation))
            return STORED_SPECIES_REWARD_TABLES.get(speciesLocation);

        ResourceLocation speciesTableLocation = ReleaseUtils.getSpeciesTableLocation(speciesLocation);

        ResourceKey<LootTable> speciesTableKey = ResourceKey.create(Registries.LOOT_TABLE, speciesTableLocation);
        STORED_SPECIES_REWARD_TABLES.put(speciesLocation, speciesTableKey);
        return speciesTableKey;
    }

    public static void giveLootToPlayer(ServerPlayer player, List<ItemStack> loot) {
        loot.forEach(stack -> {
            //ReleaseRewardsCommon.LOGGER.info("adding '{}'", stack.getItem().getName(stack));
            boolean canFit = player.addItem(stack); //&& player.getInventory().getFreeSlot() != -1
            //ReleaseRewardsCommon.LOGGER.info("item fits into inventory?: {}", canFit);
            if(!canFit) {
                //ReleaseRewardsCommon.LOGGER.info("player inventory is full, dropping item '{}' as entity", stack.getDisplayName());
                ItemEntity itemEntity = new ItemEntity(player.level(), player.position().x, player.position().y, player.position().z, stack);
                itemEntity.setPickUpDelay(40);
                player.level().addFreshEntity(itemEntity);
            } else {
                //TODO adjust sound levels
                player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, 1.0f);
            }

        });
    }
}
