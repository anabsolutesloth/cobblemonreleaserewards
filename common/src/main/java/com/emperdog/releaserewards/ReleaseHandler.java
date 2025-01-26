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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.*;

public class ReleaseHandler {
    // global Reward Table
    public static final ResourceKey<LootTable> GLOBAL_REWARD_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "rewards/global"));

    // full list of Type tables mapped to corresponding ElementalType
    public static final Map<ElementalType, ResourceKey<LootTable>> TYPE_REWARD_TABLES = typeListToResourceKeys();

    // stores generated species table ResourceKeys because expendive to create (supposedly)
    public static HashMap<ResourceLocation, ResourceKey<LootTable>> STORED_SPECIES_REWARD_TABLES = new HashMap<>();

    public static Unit handleReleaseEvent(ReleasePokemonEvent event) {
        ServerPlayer player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();
        Level level = player.level();
        ReloadableServerRegistries.Holder reloadableRegistries = Objects.requireNonNull(player.getServer()).reloadableRegistries();

        //get Global table
        LootTable globalTable = reloadableRegistries.getLootTable(GLOBAL_REWARD_TABLE);

        //get Type tables for released mon's types
        List<ResourceKey<LootTable>> typeTables = new ArrayList<ResourceKey<LootTable>>();
        pokemon.getTypes().forEach(type -> typeTables.add(getTypeRewardTable(type)));
        LootTable chosenTypeTable = reloadableRegistries.getLootTable(typeTables.get(new Random().nextInt(typeTables.size())));

        //get Species table for released mon
        LootTable speciesTable = reloadableRegistries.getLootTable(getSpeciesRewardTable(pokemon));

        //create LootContextParams for this context
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) level);
        builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
        builder.withParameter(ModLootContextParams.POKEMON, pokemon);
        LootParams params = builder.create(ModLootContextParams.Set.PLAYER_AND_POKEMON);

        spawnLootAtPlayer(player, globalTable.getRandomItems(params));
        spawnLootAtPlayer(player, chosenTypeTable.getRandomItems(params));
        spawnLootAtPlayer(player, speciesTable.getRandomItems(params));

        return Unit.INSTANCE;
    }

    public static ResourceKey<LootTable> getGlobalRewards() {
        return GLOBAL_REWARD_TABLE;
    }

    public static ResourceKey<LootTable> getTypeRewardTable(ElementalType type) {
        return TYPE_REWARD_TABLES.get(type);
    }

    public static ResourceKey<LootTable> getSpeciesRewardTable(Pokemon pokemon) {
        ResourceLocation speciesLocation = pokemon.getSpecies().resourceIdentifier;

        // check for species table in storage
        if(STORED_SPECIES_REWARD_TABLES.containsKey(speciesLocation))
            return STORED_SPECIES_REWARD_TABLES.get(speciesLocation);

        ResourceLocation speciesTableLocation = ReleaseUtils.getSpeciesTableLocation(speciesLocation);

        ResourceKey<LootTable> speciesTableKey = ResourceKey.create(Registries.LOOT_TABLE, speciesTableLocation);
        STORED_SPECIES_REWARD_TABLES.put(speciesLocation, speciesTableKey);
        return speciesTableKey;
    }

    public static void spawnLootAtPlayer(ServerPlayer player, List<ItemStack> loot) {
        loot.forEach(stack -> {
            ItemEntity itemEntity = new ItemEntity(player.level(), player.position().x, player.position().y, player.position().z, stack);
            player.level().addFreshEntity(itemEntity);
        });
    }

    private static HashMap<ElementalType, ResourceKey<LootTable>> typeListToResourceKeys() {
        HashMap<ElementalType, ResourceKey<LootTable>> tableKeys = new HashMap<>();
        ElementalTypes.INSTANCE.all().forEach(type ->
                tableKeys.put(type, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewardsCommon.MODID, "rewards/types/"+ type.getName())))
        );
        return tableKeys;
    }
}
