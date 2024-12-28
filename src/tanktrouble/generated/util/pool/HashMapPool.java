package tanktrouble.generated.util.pool;

import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

import java.util.HashMap;
import java.util.Map;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class HashMapPool<Key extends Serializable, Value extends Serializable> implements PoolContent<Key, Value> {
    HashMap<Key, Value> content = new HashMap<>();
    Deserializer<Key> keyDeserializer;
    Deserializer<Value> valueDeserializer;

    public HashMapPool(Deserializer<Key> keyDeserializer, Deserializer<Value> valueDeserializer){
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public void add(Key key, Value value) {
        content.put(key, value);
    }

    @Override
    public void remove(Key key) {
        content.remove(key);
    }

    @Override
    public Key find(Value value) {
        for(Map.Entry<Key, Value> entry : content.entrySet()){
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Value get(Key key) {
        return content.get(key);
    }

    @Override
    public void serializeKey(Key key, SerializingOutputStream out) {
        key.serialize(out);
    }

    @Override
    public Key deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return keyDeserializer.deserialize(in);
    }

    @Override
    public Value deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return valueDeserializer.deserialize(in);
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeInt(content.size());
        for(Map.Entry<Key, Value> entry : content.entrySet()){
            entry.getKey().serialize(out);
            entry.getValue().serialize(out);
        }
    }

    @Override
    public void deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        int len = in.readInt();
        for(int i = 0; i < len; i++){
            this.content.put(deserializeKey(in), valueDeserializer.deserialize(in));
        }
    }
}
