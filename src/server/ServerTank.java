package server;

import common.BaseTank;
import util.SerializingInputStream;

public class ServerTank extends BaseTank {
    public ServerTank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }
}
