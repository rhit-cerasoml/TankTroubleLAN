package tanktrouble.generated.util.pool;

import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public interface PoolContent<Key, Value extends Serializable> {
    void add(Key key, Value value);
    void remove(Key key);
    Key find(Value value);
    Value get(Key key);

    void serializeKey(Key key, SerializingOutputStream out);
    Key deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
    Value deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;

    void serialize(SerializingOutputStream out);
    void deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;

}
