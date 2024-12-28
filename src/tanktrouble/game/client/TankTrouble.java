package tanktrouble.game.client;

import processing.core.PApplet;
import tanktrouble.generated.util.net.connection.ConnectionGroup;
import tanktrouble.generated.util.net.protocol.ProtocolManager;
import tanktrouble.generated.util.pool.ArrayListPool;
import tanktrouble.generated.util.pool.Pool;

public class TankTrouble extends PApplet {
    public static boolean isHost;
    Game game;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void settings() {
        super.settings();
        size(500, 500);
        game = new Game(isHost);
    }

    @Override
    public void draw(){
        background(0);
        try {
            if(isHost){
                game.bullets.processActions();
                game.bullets.sync();
            }
            fill(255, 255, 125);
            stroke(255);
            for(int i = 0; true; i++) {
                Bullet b = game.bullets.get(i);
                if(isHost){
                    b.x += random(-1.0f, 1.0f);
                    b.y += random(-1.0f, 1.0f);
                }
                circle(b.x, b.y, 50);
            }
        }catch (Exception ignored){}
    }
}
