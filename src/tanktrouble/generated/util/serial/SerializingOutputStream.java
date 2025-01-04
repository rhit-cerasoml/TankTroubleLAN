package tanktrouble.generated.util.serial;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class SerializingOutputStream extends ByteArrayOutputStream {

    public SerializingOutputStream() { super(); }
    public SerializingOutputStream(int length) { super(length); }

    public void writeInt(int val) {
        byte[] buf = new byte[4];

        for(int i = 4; i != 0; i--) {
            buf[i-1] = (byte) (val & 0xFF);
            val <<= 8;
        }
        try {
            write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeLong(long val) {
        byte[] buf = new byte[8];

        for(int i = 8; i != 0; i--) {
            buf[i-1] = (byte) (val & 0xFF);
            val <<= 8;
        }
        try {
            write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeDouble(double val) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putDouble(val);
        try {
            write(bb.array());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeFloat(float val) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putFloat(val);
        try {
            write(bb.array());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeString(String s){
        writeInt(s.length());
        try {
            write(s.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeBoolean(boolean b){
        write(b ? 0x01 : 0x00);
    }

    public void writeByteArray(byte[] b){
        writeInt(b.length);
        try {
            write(b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public <T extends Serializable> void writeArrayList(ArrayList<T> array){
        writeInt(array.size());
        for(Serializable element : array){
            element.serialize(this);
        }
    }

}
