package util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SerializingOutputStream extends ByteArrayOutputStream {

    public SerializingOutputStream() { super(); }
    public SerializingOutputStream(int length) { super(length); }

    public void writeInt(int val) {
        byte[] buf = new byte[4];

        for(int i = 4; i != 0; i--) {
            buf[i-1] = (byte) (val & 0xFF);
            val <<= 8;
        }
        writeBytes(buf);
    }

    public void writeLong(long val) {
        byte[] buf = new byte[8];

        for(int i = 8; i != 0; i--) {
            buf[i-1] = (byte) (val & 0xFF);
            val <<= 8;
        }
        writeBytes(buf);
    }

    public void writeDouble(double val) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putDouble(val);
        writeBytes(bb.array());
    }

    public void writeFloat(float val) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putFloat(val);
        writeBytes(bb.array());
    }

    public void writeString(String s){
        writeInt(s.length());
        writeBytes(s.getBytes());
    }


}
