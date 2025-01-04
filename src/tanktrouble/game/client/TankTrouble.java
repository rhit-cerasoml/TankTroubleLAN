package tanktrouble.game.client;

import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.Map;

public class TankTrouble extends PApplet {
    public static boolean isHost;
    public static String name;
    Game game;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void setup() {
        super.setup();
        frameRate(30);
    }

    @Override
    public void settings() {
        super.settings();
        size(500, 500);
        game = new Game(isHost, name);
        game.tanks.setSyncListener(() -> {
            self = game.tanks.get(name);
        });
        self = game.tanks.get(name);
    }

    public static Tank self = null;

    @Override
    public void draw(){
        background(0);
        try {
            if(self != null){
                self.move(keysDown);
                game.tanks.sendAction(name, new Tank.UpdateTankAction(self));
            }
//            if(isHost){
//                game.bullets.processActions();
//                game.tanks.processActions();
//                game.bullets.sync();
//            }
            fill(255, 255, 125);
            stroke(255);
//            for(Map.Entry<Integer, Bullet> entry : game.bullets) {
//                Bullet b = entry.getValue();
//                if(isHost){
//                    b.x += random(-1.0f, 1.0f);
//                    b.y += random(-1.0f, 1.0f);
//                }
//                circle(b.x, b.y, 50);
//            }
            for(Map.Entry<String, Tank> tankEntry : game.tanks.entrySet()){
                tankEntry.getValue().draw(this);
            }
        }catch (Exception ignored){}
    }

    boolean[] keysDown = {false, false, false, false};

    @Override
    public void keyPressed() {
        super.keyPressed();
        switch (key){
            case 'w':
                keysDown[0] = true;
                break;
            case 's':
                keysDown[1] = true;
                break;
            case 'a':
                keysDown[2] = true;
                break;
            case 'd':
                keysDown[3] = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);
        switch (key){
            case 'w':
                keysDown[0] = false;
                break;
            case 's':
                keysDown[1] = false;
                break;
            case 'a':
                keysDown[2] = false;
                break;
            case 'd':
                keysDown[3] = false;
                break;
        }
    }
}
