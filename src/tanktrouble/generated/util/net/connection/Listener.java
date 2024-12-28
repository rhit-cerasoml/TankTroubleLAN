package tanktrouble.generated.util.net.connection;

import tanktrouble.generated.util.serial.SerializingInputStream;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public interface Listener {
    void accept(byte[] data) throws SerializingInputStream.InvalidStreamLengthException;
}
