package com.emperdog.releaserewards;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class ReleaseRewardsCommon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "releaserewards";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
}
