package tanktrouble.game.client;

public class HeadlessServer {
    public static void main(String[] args){

        Game game = new Game(true, "", "127.0.0.1", 1234);
        while(true){}
    }
}
