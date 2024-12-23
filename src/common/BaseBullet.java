package common;

import util.SerializingInputStream;

public class BaseBullet extends Entity {
    public BaseBullet(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
        // Deserialize additional bullet data here
    }
}
