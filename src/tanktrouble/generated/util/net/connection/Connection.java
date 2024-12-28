package tanktrouble.generated.util.net.connection;

import java.io.IOException;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public interface Connection {
    void send(byte[] data) throws IOException;
    void setListener(Listener listener);
    void close() throws IOException;
    boolean isClosed();
}
