package io.github.willqi.pizzaserver.packs;

import io.github.willqi.pizzaserver.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataPackManager {

    private final Map<UUID, DataPack> packs;
    private final Server server;
    private boolean required;

    public DataPackManager(Server server) {
        this.packs = new HashMap<>();
        this.server = server;
    }

    public boolean arePacksRequired() {
        return this.required;
    }

    public Map<UUID, DataPack> getPacks() {
        return Collections.unmodifiableMap(this.packs);
    }

    public void setPacksRequired(boolean required) {
        this.required = required;
    }

    public void loadResourcePacks() {
        try {
            Files.list(Paths.get(this.server.getRootDirectory() + "/resourcepacks"))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(file -> {
                        try {
                            DataPack pack = new ZipDataPack(file);
                            this.packs.put(pack.getUuid(), pack);
                            Server.getInstance().getLogger().info("Loaded resource pack: " + file.getName());
                        } catch (IOException exception) {
                            Server.getInstance().getLogger().error("Failed to load resource pack: " + file.getName());
                            Server.getInstance().getLogger().error(exception);
                        }
                    });
        } catch (IOException exception) {
            Server.getInstance().getLogger().error("Failed to read resourcepacks directory");
            Server.getInstance().getLogger().error(exception);
        }
    }

    public void loadBehaviorPacks() {
        try {
            Files.list(Paths.get(this.server.getRootDirectory() + "/behaviorpacks"))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(file -> {
                        try {
                            DataPack pack = new ZipDataPack(file);
                            this.packs.put(pack.getUuid(), pack);
                            Server.getInstance().getLogger().info("Loaded behavior pack: " + file.getName());
                        } catch (IOException exception) {
                            Server.getInstance().getLogger().error("Failed to load behavior pack: " + file.getName());
                            Server.getInstance().getLogger().error(exception);
                        }
                    });
        } catch (IOException exception) {
            Server.getInstance().getLogger().error("Failed to read behaviorpacks directory");
            Server.getInstance().getLogger().error(exception);
        }
    }

}