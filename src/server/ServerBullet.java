package server;

import common.BaseBullet;
import util.SerializingInputStream;

public class ServerBullet extends BaseBullet {
    public ServerBullet(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }
}
