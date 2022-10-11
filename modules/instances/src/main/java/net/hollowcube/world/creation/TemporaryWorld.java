package net.hollowcube.world.creation;

import net.hollowcube.api.ITemporaryWorld;
import net.hollowcube.world.WorldInitInformation;
import net.hollowcube.world.creation.chunkgenerator.ChunkGeneratorType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

public class TemporaryWorld implements ITemporaryWorld {
    private final Instance instance;
    private final int minecraftVersion; //We might not need this if we just force latest version
    private final ChunkGeneratorType chunkGeneratorType;
    // We can implement this for specifically map maker worlds (or other world types which extend this)
    //private double worldBorderSize;
    private Pos spawnPosition;

    public TemporaryWorld(WorldInitInformation worldInitInformation) {
        this(worldInitInformation.getInstance(), worldInitInformation.getMinecraftVersion(), worldInitInformation.getChunkGeneratorType(), worldInitInformation.getWorldBorderSize(), worldInitInformation.getSpawnPosition());
    }

    public TemporaryWorld(Instance instance, int minecraftVersion, ChunkGeneratorType chunkGeneratorType, Pos spawnPosition) {
        this.instance = instance;
        this.minecraftVersion = minecraftVersion;
        this.chunkGeneratorType = chunkGeneratorType;
        this.spawnPosition = spawnPosition;
    }

    @Override
    public void unloadWorld() {
        this.unloadWorld(null);
    }

    @Override
    public void unloadWorld(Runnable runnable) {
        //TODO unload world:
        // * save if necessary
        // * remove from mem
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public Instance getInstance() {
        return this.instance;
    }

    @Override
    public ChunkGeneratorType getChunkGeneratorType() {
        return this.chunkGeneratorType;
    }

    @Override
    public Pos getSpawnPosition() {
        return this.spawnPosition;
    }

    @Override
    public void setSpawnPosition(Pos spawnPosition) {
        this.spawnPosition = spawnPosition;
    }
}
