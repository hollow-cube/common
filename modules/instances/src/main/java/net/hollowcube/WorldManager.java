package net.hollowcube;

import net.hollowcube.database.PostgreSQLManager;
import net.hollowcube.database.SeaweedFS;

public class WorldManager {
    private static WorldManager instance;

    public static WorldManager getInstance() {
        return instance;
    }

    public static void init() {
        instance = new WorldManager();

    }

    private PostgreSQLManager PostgreSQL;
    private SeaweedFS seaweedFS;

    // TODO add implementation which links world manager instance to exactly one db and fs
    private void loadWorldManager() {
        PostgreSQLManager.init(); // TODO use that database
        PostgreSQL = PostgreSQLManager.get();
        seaweedFS = new SeaweedFS(); // TODO use that file system
        seaweedFS.connectToSeaweedFs();
        // Register commands in here? Or we can store them separately
    }
}
