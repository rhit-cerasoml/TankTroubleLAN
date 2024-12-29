package tanktrouble.game.client;

import tanktrouble.generated.util.net.connection.Connection;
import tanktrouble.generated.util.net.connection.ConnectionGroup;
import tanktrouble.generated.util.net.connection.SocketConnection;
import tanktrouble.generated.util.net.host.ConnectionAcceptor;
import tanktrouble.generated.util.net.host.TCPHost;
import tanktrouble.generated.util.net.protocol.ProtocolManager;
import tanktrouble.generated.util.pool.ArrayListPool;
import tanktrouble.generated.util.pool.HashMapPool;
import tanktrouble.generated.util.pool.Pool;
import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.Serializer;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

import java.net.Socket;
import java.util.Random;

public class Game {
    TCPHost hostAcceptor;
    Connection connection;
    ProtocolManager protocolManager;

    Pool<Integer, Bullet> bullets;
    Pool<String, Tank> tanks;
    public Game(boolean host, String name){
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
        tanks = new Pool<>(new HashMapPool<>(
                (item, out) -> out.writeString(item),
                SerializingInputStream::readString,
                Tank::new), protocolManager, host);

        try{
            tanks.add(name, new Tank(250, 250, name));
            tanks.sync();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!host) {
            try {
                Random r = new Random();
                bullets.add(0, new Bullet(r.nextFloat() * 500, r.nextFloat() * 500));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
        }
    }
}
