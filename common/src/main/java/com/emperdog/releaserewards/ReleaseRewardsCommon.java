package com.emperdog.releaserewards;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ReleaseRewardsCommon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "releaserewards";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // basically ResourceLocation.CODEC except it defaults to "cobblemon" namespace
    public static Codec<ResourceLocation> CODEC_RESOURCELOCATION = Codec.STRING.comapFlatMap(ReleaseRewardsCommon::readLocation, ResourceLocation::toString);

    public static DataResult<ResourceLocation> readLocation(String input) {
        try {
            return DataResult.success(CobblemonHelperKt.Companion.defaultCobblemonIdentifier(input));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid resource location "+ input +" "+ e.getMessage());
        }
    }
}
