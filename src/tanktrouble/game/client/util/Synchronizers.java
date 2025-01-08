package tanktrouble.game.client.util;

import tanktrouble.game.client.util.serial.Synchronizer;
import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.Serializer;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

public class Synchronizers {
    public static final Synchronizer<String> STRING_SYNCHRONIZER = new Synchronizer<>((item, out) -> out.writeString(item), SerializingInputStream::readString);
    public static final Synchronizer<Integer> INTEGER_SYNCHRONIZER = new Synchronizer<>((item, out) -> out.writeInt(item), SerializingInputStream::readInt);
}
