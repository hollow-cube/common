import net.hollowcube.WorldManager;
import net.hollowcube.world.World;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        server.start("0.0.0.0", 25565);

        WorldManager.init();
    }
}
