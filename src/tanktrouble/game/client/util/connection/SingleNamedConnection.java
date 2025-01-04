package tanktrouble.game.client.util.connection;

import tanktrouble.game.client.util.destination.Destination;
import tanktrouble.game.client.util.destination.Source;
import tanktrouble.generated.util.net.connection.Connection;

import java.io.IOException;

public class SingleNamedConnection implements NamedConnection {
    public final Source source = new Source();
    private final Connection connection;
    private NamedListener listener;
    public SingleNamedConnection(Connection connection){
        this.connection = connection;
    }

    public void send(byte[] data, Destination destination) throws IOException {
        if(      destination.policy == Destination.Policy.ALL ||
                (destination.policy == Destination.Policy.ONLY          && destination.source == source) ||
                (destination.policy == Destination.Policy.ALL_EXCEPT    && destination.source != source)) {
            connection.send(data);
        }
    }

    public void setListener(NamedListener listener) {
        this.listener = listener;
        connection.setListener(data -> this.listener.accept(data, source));
    }

    public void close() throws IOException {
        connection.close();
    }

    public boolean isClosed() {
        return connection.isClosed();
    }
}
