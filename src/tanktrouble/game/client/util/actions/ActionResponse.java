package tanktrouble.game.client.util.actions;

import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

public class ActionResponse implements Serializable {
    boolean success;
    public ActionResponse(boolean success){
        this.success = success;
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeBoolean(success);
    }

    public ActionResponse(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.success = in.readBoolean();
    }
}
