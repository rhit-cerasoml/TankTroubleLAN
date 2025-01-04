package tanktrouble.generated.util.net.host;

import tanktrouble.generated.util.net.connection.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//----- End segment : [package] -----
// This is a partially generated file, please only modify code between a pair of start and end segments
// or above the first end segment. Please do not modify the start/end tags.
public class TCPHost extends Thread {

    private ServerSocket serverSocket;
    private boolean open = true;
    private ConnectionAcceptor acceptor;

    public TCPHost(ConnectionAcceptor acceptor, int port) throws IOException {
        this.acceptor = acceptor;
        serverSocket = new ServerSocket(port);
        start();
    }

    public void close() throws IOException {
        open = false;
        serverSocket.close();
    }

    @Override
    public void run() {
        super.run();
        while(open && !serverSocket.isClosed()){
            try {
                Socket s = serverSocket.accept();
                SocketConnection connection = new SocketConnection(s, true);
                acceptor.acceptConnection(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
