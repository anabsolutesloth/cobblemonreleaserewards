package com.emperdog.releaserewards.loot.modifiers;

import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LevelBonusModifier extends LootItemConditionalFunction {

    private final int startingAtLevel;
    private final int levelsPerExtra;
    private final int count;

    public static final MapCodec<LevelBonusModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            commonFields(inst)
                    .and(inst.group(
                            Codec.INT.optionalFieldOf("starting_at", 0).forGetter(e -> e.startingAtLevel),
                            Codec.INT.fieldOf("levels_per").forGetter(e -> e.levelsPerExtra),
                            Codec.INT.optionalFieldOf("count", 1).forGetter(e -> e.count)
                    )).apply(inst, LevelBonusModifier::new));

    public LevelBonusModifier(List<LootItemCondition> conditions, int startingAtLevel, int levelsPerExtra, int count) {
        super(conditions);
        this.startingAtLevel = startingAtLevel;
        this.levelsPerExtra = levelsPerExtra;
        this.count = count;
    }

    @Override
    protected @NotNull ItemStack run(@NotNull ItemStack stack, LootContext context) {
        int pokemonLevel = context.getParam(ModLootContextParams.POKEMON).getLevel();
        boolean withinRange = (pokemonLevel - startingAtLevel) >= 0;
        int bonus = withinRange ? (pokemonLevel - startingAtLevel) / levelsPerExtra: 0;
        if(bonus > 0)
            stack.setCount(stack.getCount() + (bonus * count));
        return stack;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ModLootModifiers.LEVEL_BONUS.get();
    }
}
