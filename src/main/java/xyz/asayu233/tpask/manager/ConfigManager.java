package xyz.asayu233.tpask.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import xyz.asayu233.tpask.TeleportRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager extends BaseManager {
    public static final ConfigManager INSTANCE = new ConfigManager();
    private final Path configFile = FabricLoader.getInstance().getConfigDir().resolve(TeleportRequest.MOD_ID + ".json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private ConfigData config = new ConfigData();

    private ConfigManager() {}

    @NotNull
    public ConfigData getConfig() {
        return this.config;
    }

    public void reload() {
        try {
            if (!Files.exists(this.configFile))
                Files.createDirectories(this.configFile.getParent());
            else {
                try (var reader = Files.newBufferedReader(this.configFile)) {
                    this.config = this.gson.fromJson(reader, ConfigData.class);
                } catch (JsonParseException e) {
                    TeleportRequest.LOGGER.error("invalid config file", e);
                    this.config = new ConfigData();
                }
            }

            try (var writer = Files.newBufferedWriter(this.configFile, StandardCharsets.UTF_8)) {
                this.gson.toJson(this.config, writer);
            }
        } catch (IOException e) {
            TeleportRequest.LOGGER.error("Failed to reload config file!", e);
        }
    }

    @Override
    public void init() {
        this.reload();
    }

    public record ConfigData(
        boolean isRequestEnabled,
        boolean isBackEnabled,
        boolean isHomeEnabled,
        boolean isSpawnEnabled
    ) {
        public ConfigData() {
            this(true, true, true, true);
        }
    }
}
