package util;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

public class SerializingInputStream extends ByteArrayInputStream {
    public SerializingInputStream(byte[] buf) { super(buf); }
    public SerializingInputStream(byte[] buf, int offset, int length) { super(buf, offset, length); }

    public int readInt() throws InvalidStreamLengthException { // not thread safe rn
        if(available() < 4) {
            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type int");
        }
        byte[] buf = new byte[4];
        read(buf, 0, 4); // since we already checked size, this shouldn't ever fail; if it does, good luck :)

        int val = 0;
        for(int i = 0; i < 4; i++) {
            val <<= 8;
            val |= buf[i];
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
            val |= buf[0];
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

    public String readString() throws InvalidStreamLengthException{
        int len = readInt();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len; i++){
            sb.append((char)read());
        }
        return sb.toString();
    }

    public class InvalidStreamLengthException extends Exception {
        InvalidStreamLengthException(String msg){
            super(msg);
        }
    }
}
