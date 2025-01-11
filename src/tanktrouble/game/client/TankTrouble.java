package tanktrouble.game.client;

import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.Map;

public class TankTrouble extends PApplet {
    public static boolean isHost;
    public static String name;
    public static int port;
    public static String ip;
    Game game;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void setup() {
        super.setup();
        surface.setTitle(isHost ? "TankTrouble Server" : "TankTrouble Client");
        frameRate(30);
    }

    @Override
    public void settings() {
        super.settings();
        size(500, 500);
        game = new Game(isHost, name, ip, port);
        game.tanks.setSyncListener(() -> {
            self = game.tanks.get(name);
        });
        self = game.tanks.get(name);
    }

    public static Tank self = null;

    @Override
    public void draw(){
        background(0);
        game.actionManager.lock();
        try {
            if(self != null){
                self.move(keysDown);
                self.look(mouseX, mouseY);
                if(keysDown[0] || keysDown[1] || keysDown[2] || keysDown[3] || pmouseX != mouseX || pmouseY != mouseY) {
                    game.tanks.sendAction(name, new Tank.UpdateTankAction(self));
                }
                if(mousePressed){
                    game.bullets.put(floor(random(0, 100000)), new Bullet(self.x, self.y, self.turretAngle));
                }
            }

            if(isHost){
                for(Map.Entry<Integer, Bullet> bulletEntry : game.bullets.entrySet()) {
                    bulletEntry.getValue().move();
                    game.bullets.sendAction(bulletEntry.getKey(), new Bullet.UpdateBulletAction(bulletEntry.getValue()));
                }
            }
            for(Map.Entry<Integer, Bullet> bulletEntry : game.bullets.entrySet()){
                bulletEntry.getValue().draw(this);
            }
            for(Map.Entry<String, Tank> tankEntry : game.tanks.entrySet()){
                tankEntry.getValue().draw(this);
            }
        }catch (Exception ignored){}
        game.actionManager.unlock();
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
