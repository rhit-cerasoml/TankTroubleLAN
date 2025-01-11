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
import java.util.concurrent.locks.ReentrantLock;

public abstract class ActionManager {
    private final ArrayList<Deserializer<Action>> actions = new ArrayList<>();
    private final NamedConnection connection;

    protected ReentrantLock lock = new ReentrantLock(); // TODO replace this with a better solution

    public ActionManager(NamedConnection connection){
        this.connection = connection;
        connection.setListener((data, source) -> processPacket(new SerializingInputStream(data), source));
    }

    public int registerAction(Deserializer<Action> deserializer, Class<?> type){
        actions.add(deserializer);
        return actions.size() - 1;
    }

    public void lock(){
        lock.lock();
    }

    public void unlock(){
        lock.unlock();
    }

    public void processPacket(SerializingInputStream in, Source source) throws SerializingInputStream.InvalidStreamLengthException {
        lock.lock();
        int type = in.readInt();
        switch (type) {
            case 0:
                int actionID = in.readInt();
                processActionRequest(unpackageAction(in, actionID), actionID, source);
                break;
            case 1:
                processActionAcknowledgement(new ActionAcknowledgement(in), source);
                break;
            case 2:
                processRollbackAcknowledgement(source);
                break;
        }
        lock.unlock();
    }

    protected void emitAction(Action action, int actionID, Destination destination) throws IOException {
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(0);
        packageAction(out, action, actionID);
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

    public abstract boolean sendActionRequest(Action action, int actionID) throws IOException;

    protected abstract void processRollbackAcknowledgement(Source source);

    protected abstract void processActionRequest(Action action, int actionID, Source source);

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

    private void packageAction(SerializingOutputStream out, Action action, int actionID){
        out.writeInt(actionID);
        action.serialize(out);
    }

    private Action unpackageAction(SerializingInputStream in, int actionID) throws SerializingInputStream.InvalidStreamLengthException {
        return actions.get(actionID).deserialize(in);
    }
}
