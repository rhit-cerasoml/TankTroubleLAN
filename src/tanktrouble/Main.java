package tanktrouble;

import tanktrouble.game.client.Bullet;
import tanktrouble.game.client.TankTrouble;
import tanktrouble.generated.util.net.connection.ConnectionGroup;
import tanktrouble.generated.util.net.protocol.ProtocolManager;
import tanktrouble.generated.util.pool.ArrayListPool;
import tanktrouble.generated.util.pool.Pool;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankTrouble.isHost = args[0].equals("host");
        if(!TankTrouble.isHost) Thread.sleep(1000);
        TankTrouble.main("tanktrouble.game.client.TankTrouble", args);
    }
}
