package tanktrouble.game.client.util.connection;

import tanktrouble.game.client.util.destination.Destination;

import java.io.IOException;
import java.util.ArrayList;

public class NamedConnectionGroup implements NamedConnection {
    private ArrayList<SingleNamedConnection> connections = new ArrayList<>();
    private NamedListener listener = (data, source) -> {};
    private boolean open = true;

    public void addConnection(SingleNamedConnection connection){
        if(!open) return;
        connection.setListener(listener);
        connections.add(connection);
    }

    public void send(byte[] data, Destination destination) throws IOException {
        if(!open) throw new IOException("Connection Closed");
        if(destination.policy == Destination.Policy.ALL) {
            for (int i = connections.size() - 1; i >= 0; i--) {
                if (connections.get(i).isClosed()) {
                    connections.remove(i);
                    continue;
                }
                connections.get(i).send(data, new Destination(Destination.Policy.ALL));
            }
        }else if(destination.policy == Destination.Policy.ALL_EXCEPT){
            for (int i = connections.size() - 1; i >= 0; i--) {
                if(connections.get(i).source != destination.source) {
                    if (connections.get(i).isClosed()) {
                        connections.remove(i);
                        continue;
                    }
                    connections.get(i).send(data, new Destination(Destination.Policy.ALL));
                }
            }
        }else if(destination.policy == Destination.Policy.ONLY){
            for (int i = connections.size() - 1; i >= 0; i--) {
                if(connections.get(i).source == destination.source) {
                    if (connections.get(i).isClosed()) {
                        connections.remove(i);
                        break;
                    }
                    connections.get(i).send(data, new Destination(Destination.Policy.ALL));
                    break;
                }
            }
        }
    }

    public void setListener(NamedListener listener) {
        this.listener = listener;
        for(SingleNamedConnection connection : connections){
            connection.setListener(listener);
        }
    }

    public void close() throws IOException {
        open = false;
        for(SingleNamedConnection connection : connections){
            connection.close();
        }
    }

    public boolean isClosed() {
        return !open;
    }
}
