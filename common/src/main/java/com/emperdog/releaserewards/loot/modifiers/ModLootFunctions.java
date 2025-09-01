package com.emperdog.releaserewards.loot.modifiers;

import com.cobblemon.mod.common.platform.PlatformRegistry;
import com.emperdog.releaserewards.ReleaseRewards;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.jetbrains.annotations.NotNull;

public class ModLootFunctions extends PlatformRegistry<Registry<LootItemFunctionType<?>>, ResourceKey<Registry<LootItemFunctionType<?>>>, LootItemFunctionType<?>> {

    public static final ModLootFunctions INSTANCE = new ModLootFunctions();

    private ModLootFunctions() {}

    public static final LootItemFunctionType<? extends LootItemConditionalFunction> LEVEL_BONUS =
        INSTANCE.create("level_bonus", new LootItemFunctionType<>(LevelBonusModifier.CODEC));


    @Override
    public <E extends LootItemFunctionType<?>> E create(@NotNull String name, E entry) {
        return super.create(ReleaseRewards.resource(name), entry);
    }

    @Override
    public @NotNull Registry<LootItemFunctionType<?>> getRegistry() {
        return BuiltInRegistries.LOOT_FUNCTION_TYPE;
    }

    @Override
    public @NotNull ResourceKey<Registry<LootItemFunctionType<?>>> getResourceKey() {
        return Registries.LOOT_FUNCTION_TYPE;
    }
}
