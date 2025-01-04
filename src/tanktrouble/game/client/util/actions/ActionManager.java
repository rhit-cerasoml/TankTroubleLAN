package tanktrouble.game.client.util.actions;

import tanktrouble.game.client.util.connection.NamedConnection;
import tanktrouble.game.client.util.destination.Destination;
import tanktrouble.game.client.util.destination.Source;
import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.Serializable;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ActionManager {
    private final ArrayList<Deserializer<Action>> actions = new ArrayList<>();
    private final HashMap<Class<?>, Integer> actionMap = new HashMap<>();
    private final NamedConnection connection;
    public ActionManager(NamedConnection connection){
        this.connection = connection;
        connection.setListener((data, source) -> processPacket(new SerializingInputStream(data), source));
    }

    public void registerAction(Deserializer<Action> deserializer, Class<?> type){
        actionMap.put(type, actions.size());
        actions.add(deserializer);
    }

    public void processPacket(SerializingInputStream in, Source source) throws SerializingInputStream.InvalidStreamLengthException {
        int type = in.readInt();
        switch (type) {
            case 0 -> processActionRequest(unpackageAction(in), source);
            case 1 -> processActionAcknowledgement(new ActionAcknowledgement(in), source);
            case 2 -> processRollbackAcknowledgement(source);
        }
    }

    protected void emitAction(Action action, Destination destination) throws IOException {
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(0);
        packageAction(out, action);
        connection.send(out.toByteArray(), destination);
    }

    protected void emitActionAcknowledgement(ActionAcknowledgement actionAcknowledgement, Destination destination) throws IOException {
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(1);
        actionAcknowledgement.serialize(out);
        connection.send(out.toByteArray(), destination);
    }

    protected void emitRollbackAcknowledgement(Destination destination) throws IOException {
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(2);
        connection.send(out.toByteArray(), destination);
    }

    public abstract boolean sendActionRequest(Action action) throws IOException;

    protected abstract void processRollbackAcknowledgement(Source source);

    protected abstract void processActionRequest(Action action, Source source);

    protected abstract void processActionAcknowledgement(ActionAcknowledgement actionAcknowledgement, Source source);

    protected static class ActionAcknowledgement implements Serializable {
        boolean result;
        public ActionAcknowledgement(boolean result){
            this.result = result;
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            out.writeBoolean(result);
        }

        // Deserializer
        public ActionAcknowledgement(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            this.result = in.readBoolean();
        }
    }

    private void packageAction(SerializingOutputStream out, Action action){
        Integer actionID = actionMap.get(action.getClass());
        if(actionID == null){
            throw new RuntimeException("Unregistered action: " + action.getClass().getName());
        }
        out.writeInt(actionID);
        action.serialize(out);
    }

    private Action unpackageAction(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        int actionID = in.readInt();
        return actions.get(actionID).deserialize(in);
    }
}
