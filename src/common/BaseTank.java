package common;

import util.SerializingInputStream;

public class BaseTank extends Entity {
    protected int ammoCount, health;
    // String item; powerup?
    public BaseTank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
        // Deserialize additional tank info here
        this.ammoCount = in.readInt();
        this.health = in.readInt();
    }

}
