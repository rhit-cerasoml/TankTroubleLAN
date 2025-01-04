package tanktrouble.game.client.util.destination;

public class Source {
    private static int idGen = 0;
    public final int ID;
    public Source(){
        ID = idGen;
        idGen++;
    }
}
