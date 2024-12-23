package common;

import util.SerializingInputStream;
import util.SerializingOutputStream;

public class Entity {
    protected float x, y;
    protected float angle;

    public Entity(float x, float y, float angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public Entity(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.angle = in.readFloat();
    }

    public void getData(SerializingOutputStream out){
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(angle);
    }
}
