package com.emperdog.releaserewards.fabric;

import com.emperdog.releaserewards.ReleaseRewardsCommon;
import com.emperdog.releaserewards.services.ReleaseRewardsConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FabricReleaseRewardsConfig implements ReleaseRewardsConfig {

    private static Path configFilePath;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static int maxPreEvoSearchDepth = 10;
    public static int maxEvoSearchDepth = 10;

    public static void load() {
        Reader reader;
        if(getConfigFilePath().toFile().exists()) {
            try {
                reader = Files.newBufferedReader(getConfigFilePath());

                Data data = gson.fromJson(reader, Data.class);

                maxPreEvoSearchDepth = data.maxPreEvoSearchDepth;
                maxEvoSearchDepth = data.maxEvoSearchDepth;

                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        save();
    }

    public static void save() {
        try {
            Writer writer = Files.newBufferedWriter(getConfigFilePath());
            Data data = new Data(
                    maxPreEvoSearchDepth,
                    maxEvoSearchDepth
            );

            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getConfigFilePath() {
        if(configFilePath == null)
            configFilePath = FabricLoader.getInstance().getConfigDir().resolve(ReleaseRewardsCommon.MODID + ".json");
        return configFilePath;
    }

    private static class Data {

        private final int maxPreEvoSearchDepth;
        private final int maxEvoSearchDepth;

        private Data() {
            this.maxPreEvoSearchDepth = 10;
            this.maxEvoSearchDepth = 10;
        }

        private Data(int maxPreEvoSearchDepth, int maxEvoSearchDepth) {
            this.maxPreEvoSearchDepth = maxPreEvoSearchDepth;
            this.maxEvoSearchDepth = maxEvoSearchDepth;
        }
    }

    @Override
    public int getMaxPreEvoSearchDepth() {
        return maxPreEvoSearchDepth;
    }

    @Override
    public int getMaxEvoSearchDepth() {
        return maxEvoSearchDepth;
    }
}
