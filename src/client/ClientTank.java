package client;

import common.BaseTank;
import util.SerializingInputStream;

public class ClientTank extends BaseTank {

    public ClientTank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }
}
