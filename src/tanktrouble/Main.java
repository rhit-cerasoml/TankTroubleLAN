package tanktrouble;

import tanktrouble.game.client.Bullet;
import tanktrouble.game.client.Tank;
import tanktrouble.game.client.TankTrouble;
import tanktrouble.generated.util.net.connection.ConnectionGroup;
import tanktrouble.generated.util.net.protocol.ProtocolManager;
import tanktrouble.generated.util.pool.ArrayListPool;
import tanktrouble.generated.util.pool.Pool;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankTrouble.isHost = args[0].equals("host");
        if(args.length < 2){
            Random r = new Random();
            TankTrouble.name = "Unknown" + r.nextInt(9) + r.nextInt(9) + r.nextInt(9) + r.nextInt(9);
        }else{
            TankTrouble.name = args[1];
        }
        if(!TankTrouble.isHost) Thread.sleep(1000);
        TankTrouble.main("tanktrouble.game.client.TankTrouble", args);
    }
}
