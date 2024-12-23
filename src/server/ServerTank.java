package server;

import common.BaseTank;
import util.SerializingInputStream;

public class ServerTank extends BaseTank {
    float reloadSpeed;
    public ServerTank(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        super(in);
    }

    //
    public boolean damage(){
        this.health = this.health-1;
        return true;
    }
}
