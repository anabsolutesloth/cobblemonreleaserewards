package com.emperdog.releaserewards;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum EnumPokemonStats implements StringRepresentable {
    HP("hp"),
    ATK("atk"),
    DEF("def"),
    SPA("spa"),
    SPD("spd"),
    SPE("spe");

    public static final Codec<EnumPokemonStats> CODEC = StringRepresentable.fromValues(EnumPokemonStats::values);

    private final String stat;

    EnumPokemonStats(String stat) {
        this.stat = stat;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.stat;
    }
}
