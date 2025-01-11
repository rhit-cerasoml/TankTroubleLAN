package tanktrouble.game.client;

import processing.core.PApplet;
import processing.core.PConstants;
import tanktrouble.game.client.util.sharedhashmap.TargetedAction;
import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

public class Bullet implements Serializable {
    float x, y;
    float angle;

    private static final float moveSpeed = 3.0f;

    public Bullet(float x, float y, float angle){
        this.x = x + PApplet.cos(angle) * (Tank.barrelLength + 5);
        this.y = y + PApplet.sin(angle) * (Tank.barrelLength + 5);
        this.angle = angle;
    }


    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(angle);
    }

    // Deserialize
    public Bullet(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.angle = in.readFloat();
    }

    public void draw(PApplet g){
        g.stroke(0);
        g.fill(255, 0, 0);
        g.translate(x, y);
        g.rotate(angle);
        g.rect(-5, -3, 10, 6);
        g.rotate(-angle);
        g.translate(-x, -y);
    }

    public void move(){
        x += moveSpeed * PApplet.cos(angle);
        y += moveSpeed * PApplet.sin(angle);
    }

    public static class UpdateBulletAction implements TargetedAction<Bullet> {
        float x, y;
        float angle;
        public UpdateBulletAction(Bullet target){
            this.x = target.x;
            this.y = target.y;
            this.angle = target.angle;
        }
        @Override
        public boolean apply(Bullet target) {
            float temp = target.x;
            target.x = x;
            x = temp;
            temp = target.y;
            target.y = y;
            y = temp;
            temp = target.angle;
            target.angle = angle;
            angle = temp;
            return true;
        }

        @Override
        public boolean revert(Bullet target) {
            float temp = target.x;
            target.x = x;
            x = temp;
            temp = target.y;
            target.y = y;
            y = temp;
            temp = target.angle;
            target.angle = angle;
            angle = temp;
            return true;
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            out.writeFloat(x);
            out.writeFloat(y);
            out.writeFloat(angle);
        }

        // Deserializer
        public UpdateBulletAction(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            this.x = in.readFloat();
            this.y = in.readFloat();
            this.angle = in.readFloat();
        }
    }
}
