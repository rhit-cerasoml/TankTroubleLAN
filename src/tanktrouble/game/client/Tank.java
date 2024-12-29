package tanktrouble.game.client;

import processing.core.PApplet;
import processing.core.PConstants;
import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

public class Tank implements Serializable {
    float x, y;
    float angle;
    String name;
    private static float speed = 2.5f;
    private static float turnSpeed = 0.05f;

    public Tank(float x, float y, String name){
        this.x = x;
        this.y = y;
        this.angle = 0;
        this.name = name;
    }


    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(angle);
        out.writeString(name);
    }

    // Deserialize
    public Tank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.angle = in.readFloat();
        this.name = in.readString();
    }

    public void draw(PApplet g){
        g.translate(x, y);
        g.rotate(angle);
        g.rect(-15, -15, 30, 30);
        g.rotate(-angle);
        g.translate(-x, -y);
        g.textAlign(PConstants.CENTER, PConstants.CENTER);
        g.text(name, x, y - 30);
    }

    public void move(boolean[] keysDown) {
        float vx, vy;
        vx = (float) (speed * Math.cos(angle));
        vy = (float) (speed * Math.sin(angle));
        if(keysDown[0]){
            x += vx;
            y += vy;
        }
        if(keysDown[1]){
            x -= vx;
            y -= vy;
        }
        if(keysDown[2]){
            angle -= turnSpeed;
        }
        if(keysDown[3]){
            angle += turnSpeed;
        }
    }
}
