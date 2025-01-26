package com.emperdog.releaserewards.data;

import com.cobblemon.mod.common.api.moves.categories.DamageCategory;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.battles.MoveTarget;
import com.emperdog.releaserewards.loot.ReleaseUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

//import javax.annotation.Nullable;

public class MovePredicate {

    private final double power;
    private final double accuracy;
    private final ElementalType type;
    private final String category;
    private final int priority;
    private final String target;
    private final int maxpp;

    public static final MapCodec<MovePredicate> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("power", -1).forGetter(e -> (int) e.power),
            Codec.INT.optionalFieldOf("accuracy", 0).forGetter(e -> (int) e.accuracy),
            ElementalType.getBY_STRING_CODEC().optionalFieldOf("type", ReleaseUtils.EMPTY_TYPE).forGetter(e -> e.type),
            Codec.STRING.optionalFieldOf("category", "any").forGetter(e -> e.category),
            Codec.INT.optionalFieldOf("priority", -99).forGetter(e -> e.priority),
            Codec.STRING.optionalFieldOf("target", "").forGetter(e -> e.target),
            Codec.INT.optionalFieldOf("max_pp", 0).forGetter(e -> e.maxpp)
    ).apply(inst, MovePredicate::new));

    public MovePredicate(double power, double accuracy, ElementalType type, String category, int priority, String target, int maxpp) {
        this.power = power;
        this.accuracy = accuracy;
        this.type = type;
        this.category = category;
        this.priority = priority;
        this.target = target;
        this.maxpp = maxpp;
    }

    public DamageCategory getDamageCategory() {
        return ReleaseUtils.getDamageCategoryFromAlias(this.category);
    }

    public double getPower() {
        return this.power;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public ElementalType getType() {
        return this.type;
    }

    public MoveTarget getTarget() {
        return this.target.isEmpty() ? null : ReleaseUtils.getMoveTargetFromAlias(this.target);
    }

    public int getPriority() {
        return this.priority;
    }

    public int getMaxpp() {
        return this.maxpp;
    }
}
