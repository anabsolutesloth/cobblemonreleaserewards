package com.emperdog.releaserewards;

import com.cobblemon.mod.common.api.events.storage.ReleasePokemonEvent;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import kotlin.Unit;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

public class ReleaseHandler {
    private static final Logger log = LoggerFactory.getLogger(ReleaseHandler.class);

    public static ResourceKey<LootTable> globalRewardTable =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewards.MODID, "rewards/global"));

    public static Map<ElementalType, ResourceKey<LootTable>> typeRewardTable = typeListToResourceKeys();

    public static Function<Species, ResourceKey<LootTable>> speciesRewardTable = (species) ->
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewards.MODID, "rewards/species/"+ species.getName().toLowerCase()));

    public static Unit handleReleaseEvent(ReleasePokemonEvent event) {
        ServerPlayer player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();
        Level level = player.level();
        ReloadableServerRegistries.Holder reloadableRegistries = Objects.requireNonNull(player.getServer()).reloadableRegistries();

        log.info("providing Release Rewards to player "+ player.getName().getString() +", who released "+ pokemon.getSpecies().getName() +"!");

        LootTable globalTable = reloadableRegistries.getLootTable(globalRewardTable);

        //get Type tables for released mon's types
        List<ResourceKey<LootTable>> typeTables = new ArrayList<ResourceKey<LootTable>>();
        pokemon.getTypes().forEach(type -> typeTables.add(getTypeRewardTable(type)));
        LootTable chosenTypeTable = reloadableRegistries.getLootTable(typeTables.get(new Random().nextInt(typeTables.size())));

        //get Species table for released mon
        LootTable speciesTable = reloadableRegistries.getLootTable(getSpeciesRewardTable(pokemon.getSpecies()));

        //create LootContextParams for this context
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) level);
        builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
        builder.withParameter(ModLootContextParams.POKEMON, pokemon);
        LootParams params = builder.create(ModLootContextParams.Set.PLAYER_AND_POKEMON);

        globalTable.getRandomItems(params).forEach((stack) -> {
            ItemEntity itemEntity = new ItemEntity(level, player.position().x, player.position().y, player.position().z, stack);
            level.addFreshEntity(itemEntity);
        });

        chosenTypeTable.getRandomItems(params).forEach(stack -> {
            ItemEntity itemEntity = new ItemEntity(level, player.position().x, player.position().y, player.position().z, stack);
            level.addFreshEntity(itemEntity);
        });

        speciesTable.getRandomItems(params).forEach(stack -> {
            ItemEntity itemEntity = new ItemEntity(level, player.position().x, player.position().y, player.position().z, stack);
            level.addFreshEntity(itemEntity);
        });

        return Unit.INSTANCE;
    }

    public static ResourceKey<LootTable> getGlobalRewardTable() {
        return globalRewardTable;
    }

    public static ResourceKey<LootTable> getTypeRewardTable(ElementalType type) {
        return typeRewardTable.get(type);
    }

    public static ResourceKey<LootTable> getSpeciesRewardTable(Species species) {
        return speciesRewardTable.apply(species);
    }

    private static HashMap<ElementalType, ResourceKey<LootTable>> typeListToResourceKeys() {
        HashMap<ElementalType, ResourceKey<LootTable>> tableKeys = new HashMap<>();
        ElementalTypes.INSTANCE.all().forEach(type ->
                tableKeys.put(type, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ReleaseRewards.MODID, "rewards/types/"+ type.getName())))
        );
        return tableKeys;
    }
}
