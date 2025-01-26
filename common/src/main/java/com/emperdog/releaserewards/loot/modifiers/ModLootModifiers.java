package com.emperdog.releaserewards.loot.modifiers;

import com.emperdog.releaserewards.ReleaseRewardsCommon;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES =
            DeferredRegister.create(ReleaseRewardsCommon.MODID, Registries.LOOT_FUNCTION_TYPE);

    public static final Supplier<LootItemFunctionType<? extends LootItemConditionalFunction>> LEVEL_BONUS =
        LOOT_FUNCTION_TYPES.register("level_bonus", () -> new LootItemFunctionType<>(LevelBonusModifier.CODEC));
}
