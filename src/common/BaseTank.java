package common;

import util.SerializingInputStream;

public class BaseTank extends Entity {
    public BaseTank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
        // Deserialize additional tank info here
    }

}
