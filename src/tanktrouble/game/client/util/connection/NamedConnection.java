package tanktrouble.game.client.util.connection;

import tanktrouble.game.client.util.destination.Destination;

import java.io.IOException;

public interface NamedConnection {
    void send(byte[] data, Destination destination) throws IOException;
    void setListener(NamedListener listener);
    void close() throws IOException;
    boolean isClosed();
}
