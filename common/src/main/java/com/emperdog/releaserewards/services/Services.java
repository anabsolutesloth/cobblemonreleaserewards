package com.emperdog.releaserewards.services;

import com.emperdog.releaserewards.ReleaseRewardsCommon;

import java.util.ServiceLoader;

public class Services {

    public static final ReleaseRewardsConfig CONFIG = load(ReleaseRewardsConfig.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ReleaseRewardsCommon.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
