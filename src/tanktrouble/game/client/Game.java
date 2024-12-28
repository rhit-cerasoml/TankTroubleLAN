package tanktrouble.game.client;

import tanktrouble.generated.util.net.connection.Connection;
import tanktrouble.generated.util.net.connection.ConnectionGroup;
import tanktrouble.generated.util.net.connection.SocketConnection;
import tanktrouble.generated.util.net.host.ConnectionAcceptor;
import tanktrouble.generated.util.net.host.TCPHost;
import tanktrouble.generated.util.net.protocol.ProtocolManager;
import tanktrouble.generated.util.pool.ArrayListPool;
import tanktrouble.generated.util.pool.Pool;

import java.net.Socket;
import java.util.Random;

public class Game {
    TCPHost hostAcceptor;
    Connection connection;
    ProtocolManager protocolManager;

    Pool<Integer, Bullet> bullets;
    public Game(boolean host){
        if(host){
            this.connection = new ConnectionGroup();
            try {
                hostAcceptor = new TCPHost(((ConnectionGroup)this.connection)::addConnection, 1234);
            }catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            try {
                Socket s = new Socket("127.0.0.1", 1234);
                connection = new SocketConnection(s);
            }catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
        protocolManager = new ProtocolManager(connection);
        bullets = new Pool<>(new ArrayListPool<>(Bullet::new), protocolManager, host);

        if(!host) {
            try {
                Random r = new Random();
                bullets.add(0, new Bullet(r.nextFloat() * 500, r.nextFloat() * 500));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
