package tanktrouble.game.client.util.connection;

import tanktrouble.game.client.util.destination.Source;
import tanktrouble.generated.util.serial.SerializingInputStream;

public interface NamedListener {
    void accept(byte[] data, Source source) throws SerializingInputStream.InvalidStreamLengthException;
}
