package tanktrouble.generated.util.net.connection;

import tanktrouble.generated.util.serial.SerializingInputStream;

import java.io.IOException;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class DirectConnection implements Connection {
    private Listener listener = new NullListener();
    private DirectConnection endpoint;
    private boolean open = true;

    public DirectConnection(){
        this.endpoint = new DirectConnection(this);
    }

    private DirectConnection(DirectConnection endpoint){
        this.endpoint = endpoint;
    }

    public DirectConnection getEndpoint() {
        return endpoint;
    }

    @Override
    public void send(byte[] data) throws IOException {
        if(!open) throw new IOException("Connection Closed");
        try {
            this.endpoint.recv(data);
        }catch (Exception e){
            throw new IOException("Direct Receive Failed");
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void close() {
        if(open) {
            endpoint.endpoint = null;
            endpoint.open = false;
            endpoint = null;
            open = false;
        }
    }

    @Override
    public boolean isClosed() {
        return !open;
    }

    private void recv(byte[] data) throws SerializingInputStream.InvalidStreamLengthException, IOException {
        listener.accept(data);
    }
}
