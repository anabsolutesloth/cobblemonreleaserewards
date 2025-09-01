package com.emperdog.releaserewards;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.emperdog.releaserewards.loot.ModLootContextParams;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ReleaseRewards {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "releaserewards";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // basically ResourceLocation.CODEC except it defaults to "cobblemon" namespace
    public static Codec<ResourceLocation> CODEC_RESOURCELOCATION = Codec.STRING.comapFlatMap(ReleaseRewards::readLocation, ResourceLocation::toString);

    public static DataResult<ResourceLocation> readLocation(String input) {
        try {
            return DataResult.success(CobblemonHelperKt.Companion.defaultCobblemonIdentifier(input));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid resource location "+ input +" "+ e.getMessage());
        }
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static void preInit(ReleaseRewardsMod impl) {
        ModLootContextParams.init();

        impl.registerLootConditions();
        impl.registerLootFunctions();
        impl.registerLootEntries();

        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, ReleaseHandler::handleReleaseEvent);
    }
}
