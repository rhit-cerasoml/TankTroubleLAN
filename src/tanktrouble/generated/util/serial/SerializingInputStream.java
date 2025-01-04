package tanktrouble.generated.util.serial;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class SerializingInputStream extends ByteArrayInputStream {
    public SerializingInputStream(byte[] buf) { super(buf); }
    public SerializingInputStream(byte[] buf, int offset, int length) { super(buf, offset, length); }
//    public SerializingInputStream(FileInputStream fis) throws IOException {
//        super(fis.readAllBytes());
//    }

    public int readInt() throws InvalidStreamLengthException { // not thread safe rn
        if(available() < 4) {
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type int");
        }
        byte[] buf = new byte[4];
        read(buf, 0, 4); // since we already checked size, this shouldn't ever fail; if it does, good luck :)

        int val = 0;
        for(int i = 0; i < 4; i++) {
            val <<= 8;
            val |= Byte.toUnsignedInt(buf[i]);
        }
        return val;
    }

    public long readLong() throws InvalidStreamLengthException { // not thread safe rn
        if(available() < 8) {
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type long");
        }
        byte[] buf = new byte[8];
        read(buf, 0, 8); // since we already checked size, this shouldn't ever fail; if it does, good luck :)

        int val = 0;
        for(int i = 0; i < 8; i++) {
            val <<= 8;
            val |= Byte.toUnsignedInt(buf[i]);
        }
        return val;
    }

    public double readDouble() throws InvalidStreamLengthException { // not thread safe rn
        if(available() < 8) {
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type long");
        }
        byte[] buf = new byte[8];
        read(buf, 0, 8); // since we already checked size, this shouldn't ever fail; if it does, good luck :)
        return ByteBuffer.wrap(buf).getDouble();
    }

    public float readFloat() throws InvalidStreamLengthException { // not thread safe rn
        if(available() < 4) {
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type long");
        }
        byte[] buf = new byte[4];
        read(buf, 0, 4); // since we already checked size, this shouldn't ever fail; if it does, good luck :)
        return ByteBuffer.wrap(buf).getFloat();
    }

    public String readString() throws InvalidStreamLengthException {
        int len = readInt();
        StringBuilder sb = new StringBuilder();
        if(available() < len){
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type string with len " + len);
        }
        for(int i = 0; i < len; i++){
            sb.append((char)read());
        }
        return sb.toString();
    }

    public boolean readBoolean() throws InvalidStreamLengthException {
        if(available() < 0){
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type boolean");
        }
        return read() == 0x01;
    }

    public byte[] readByteArray() throws InvalidStreamLengthException {
        int len = readInt();
        try{
            if(len < available()){
                throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type byte[" + len + "]");
            }
            byte[] data = new byte[len];
            for(int i = 0; i < len; i++){
                data[i] = (byte)read();
            }
            return data;
        }catch (Exception e){
            throw new InvalidStreamLengthException("Issue while reading byte array: " + e);
        }
    }

    public <T> ArrayList<T> readArrayList(Deserializer<T> deserializer) throws InvalidStreamLengthException {
        ArrayList<T> arrayList = new ArrayList<>();
        int len = readInt();
        for(int i = 0; i < len; i++){
            arrayList.add(deserializer.deserialize(this));
        }
        return arrayList;
    }

    public class InvalidStreamLengthException extends Exception {
        InvalidStreamLengthException(String msg){
            super(msg);
        }
    }
}
