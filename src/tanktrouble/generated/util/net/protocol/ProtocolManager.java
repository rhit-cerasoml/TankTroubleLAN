package tanktrouble.generated.util.net.protocol;

import tanktrouble.generated.util.net.connection.Connection;
import tanktrouble.generated.util.net.connection.Listener;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

import java.io.IOException;
import java.util.ArrayList;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class ProtocolManager implements Listener {
    private ArrayList<Protocol> protocols = new ArrayList<>();
    private Connection connection;

    public ProtocolManager(Connection connection){
        this.connection = connection;
        connection.setListener(this);
    }

    public int addProtocol(Protocol protocol){
        protocols.add(protocol);
        return protocols.size() - 1;
    }

    @Override
    public void accept(byte[] data) throws SerializingInputStream.InvalidStreamLengthException {
        SerializingInputStream in = new SerializingInputStream(data);
        int protocolID = in.readInt();
        protocols.get(protocolID).accept(in);
    }

    public SerializingOutputStream getPacketStub(Protocol p){
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(protocols.indexOf(p));
        return out;
    }

    public void sendPacket(SerializingOutputStream out) throws IOException {
        connection.send(out.toByteArray());
    }

}
