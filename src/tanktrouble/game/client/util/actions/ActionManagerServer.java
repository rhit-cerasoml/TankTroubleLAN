package tanktrouble.game.client.util.actions;

import tanktrouble.game.client.util.connection.NamedConnection;
import tanktrouble.game.client.util.destination.Destination;
import tanktrouble.game.client.util.destination.Source;

import java.io.IOException;
import java.util.HashMap;

public class ActionManagerServer extends ActionManager {

    HashMap<Source, Boolean> stateValid = new HashMap<>();

    public ActionManagerServer(NamedConnection connection) {
        super(connection);
    }

    @Override
    public boolean sendActionRequest(Action action, int actionID) throws IOException {
        boolean result = action.apply();
        if(result){
            emitAction(action, actionID, new Destination(Destination.Policy.ALL));
        }
        return result;
    }

    @Override
    protected void processRollbackAcknowledgement(Source source) {
        stateValid.put(source, true);
    }

    @Override
    public void processActionRequest(Action action, int actionID, Source source) {
        boolean accept;
        if(!stateValid.containsKey(source)){
            stateValid.put(source, true);
            accept = true;
        }else{
            accept = stateValid.get(source);
        }
        if(accept) {
            boolean result = action.apply();
            if (result) {
                try {
                    emitAction(action, actionID, new Destination(Destination.Policy.ALL_EXCEPT, source));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try {
                emitActionAcknowledgement(new ActionAcknowledgement(result), new Destination(Destination.Policy.ONLY, source));
            } catch (Exception e){
                e.printStackTrace();
            }
            if(!result){
                stateValid.put(source, false);
            }
        }else{
            System.out.println("Rejected request");
        }
    }

    @Override
    public void processActionAcknowledgement(ActionAcknowledgement actionAcknowledgement, Source source) {
        // Do nothing - this is not a valid operation on server
    }
}
