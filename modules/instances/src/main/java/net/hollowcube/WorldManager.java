package net.hollowcube;

public class WorldManager {
    private static WorldManager instance;

    public static WorldManager getInstance() {
        return instance;
    }

    public static void init() {
        instance = new WorldManager();
    }
}
