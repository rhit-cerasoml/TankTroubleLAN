package tanktrouble.game.client;

import tanktrouble.game.client.util.Synchronizers;
import tanktrouble.game.client.util.actions.ActionManager;
import tanktrouble.game.client.util.actions.ActionManagerClient;
import tanktrouble.game.client.util.actions.ActionManagerServer;
import tanktrouble.game.client.util.connection.NamedConnection;
import tanktrouble.game.client.util.connection.NamedConnectionGroup;
import tanktrouble.game.client.util.connection.SingleNamedConnection;
import tanktrouble.game.client.util.serial.Synchronizer;
import tanktrouble.game.client.util.sharedhashmap.SharedHashMap;
import tanktrouble.game.client.util.sharedhashmap.SharedHashMapClient;
import tanktrouble.game.client.util.sharedhashmap.SharedHashMapServer;
import tanktrouble.game.client.util.sharedhashmap.TargetedAction;
import tanktrouble.generated.util.net.connection.SocketConnection;
import tanktrouble.generated.util.net.host.TCPHost;
import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.SerializingInputStream;

import java.net.Socket;

public class Game {
    TCPHost hostAcceptor;
    NamedConnection connection;
    ActionManager actionManager;
    SharedHashMap<String, Tank> tanks;
    SharedHashMap<Integer, Bullet> bullets;
    public Game(boolean host, String name, String ip, int port){
        Synchronizer<Tank> tankSynchronizer = new Synchronizer<>(Tank::serialize, Tank::new);
        Synchronizer<Bullet> bulletSynchronizer = new Synchronizer<>(Bullet::serialize, Bullet::new);
        if(host){
            this.connection = new NamedConnectionGroup();
            actionManager = new ActionManagerServer(this.connection);
            tanks = new SharedHashMapServer<>(actionManager, Synchronizers.STRING_SYNCHRONIZER, tankSynchronizer);
            bullets = new SharedHashMapServer<>(actionManager, Synchronizers.INTEGER_SYNCHRONIZER, bulletSynchronizer);
            tanks.registerElementAction(Tank.UpdateTankAction::new, Tank.UpdateTankAction.class);
            bullets.registerElementAction(Bullet.UpdateBulletAction::new, Bullet.UpdateBulletAction.class);
            try {
                hostAcceptor = new TCPHost(c -> ((NamedConnectionGroup)connection).addConnection(new SingleNamedConnection(c)), port);
            }catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            try {
                Socket s = new Socket(ip, port);
                SocketConnection sc = new SocketConnection(s);
                connection = new SingleNamedConnection(sc);
                actionManager = new ActionManagerClient(this.connection);
                tanks = new SharedHashMapClient<>(actionManager, Synchronizers.STRING_SYNCHRONIZER, tankSynchronizer);
                bullets = new SharedHashMapClient<>(actionManager, Synchronizers.INTEGER_SYNCHRONIZER, bulletSynchronizer);
                tanks.registerElementAction(Tank.UpdateTankAction::new, Tank.UpdateTankAction.class);
                bullets.registerElementAction(Bullet.UpdateBulletAction::new, Bullet.UpdateBulletAction.class);
                sc.start();
            }catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        }


        if(!name.isEmpty()) {
            try {
                tanks.put(name, new Tank(250, 250, name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if(!host) {
//            try {
//                Random r = new Random();
//                bullets.add(0, new Bullet(r.nextFloat() * 500, r.nextFloat() * 500));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else{
//        }
    }
}
