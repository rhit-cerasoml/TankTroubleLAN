package server;

import common.BaseBullet;
import util.SerializingInputStream;

public class ServerBullet extends BaseBullet {
    float xVel0city, yVel0city;
    public ServerBullet(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }
}
