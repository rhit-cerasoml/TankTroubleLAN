package tanktrouble.game.client;

import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

public class Tank implements Serializable {
    float x, y;

    public Tank(float x, float y){
        this.x = x;
        this.y = y;
    }


    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeFloat(x);
        out.writeFloat(y);
    }

    // Deserialize
    public Tank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }
}
