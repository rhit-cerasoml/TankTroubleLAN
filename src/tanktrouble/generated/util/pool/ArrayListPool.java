package tanktrouble.generated.util.pool;

import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class ArrayListPool<T extends Serializable> implements PoolContent<Integer, T> {
    private ArrayList<T> content = new ArrayList<>();
    private Deserializer<T> deserializer;
    public ArrayListPool(Deserializer<T> deserializer){
        this.deserializer = deserializer;
    }

    @Override
    public void add(Integer index, T element) {
        content.add(index, element);
    }

    @Override
    public void remove(Integer index) {
        content.remove((int)index);
    }

    @Override
    public Integer find(T element) {
        return content.indexOf(element);
    }

    @Override
    public T get(Integer index) {
        return content.get(index);
    }

    @Override
    public void update(Integer index, T element) {
        content.set(index, element);
    }

    @Override
    public int size() {
        return content.size();
    }

    @Override
    public void serializeKey(Integer index, SerializingOutputStream out) {
        out.writeInt(index);
    }

    @Override
    public Integer deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return in.readInt();
    }

    @Override
    public T deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return deserializer.deserialize(in);
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeArrayList(content);
    }

    @Override
    public void deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.content = in.readArrayList(deserializer);
    }

    @Override
    public Iterator<Map.Entry<Integer, T>> iterator() {
        return new Iterator<Map.Entry<Integer, T>>() {
            int index = -1;
            @Override
            public boolean hasNext() {
                return index < content.size() - 1;
            }

            @Override
            public Map.Entry<Integer, T> next() {
                index++;
                return new Map.Entry<Integer, T>() {
                    @Override
                    public Integer getKey() {
                        return index;
                    }

                    @Override
                    public T getValue() {
                        return content.get(index);
                    }

                    @Override
                    public T setValue(T value) {
                        return content.set(index, value);
                    }
                };
            }
        };
    }
}
