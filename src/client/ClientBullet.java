package client;

import common.BaseBullet;
import util.SerializingInputStream;

public class ClientBullet extends BaseBullet {
    public ClientBullet(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }
}
