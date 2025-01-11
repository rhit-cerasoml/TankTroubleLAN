package tanktrouble.game.client.util.sharedhashmap;

import tanktrouble.game.client.util.actions.Action;
import tanktrouble.game.client.util.actions.ActionManager;
import tanktrouble.game.client.util.serial.Synchronizer;
import tanktrouble.generated.util.net.connection.Listener;
import tanktrouble.generated.util.net.connection.NullListener;
import tanktrouble.generated.util.serial.Deserializer;
import tanktrouble.generated.util.serial.SerializingInputStream;
import tanktrouble.generated.util.serial.SerializingOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SharedHashMap<K, V> {
    protected final HashMap<K, V> content;
    protected final Synchronizer<K> keySynchronizer;
    protected final Synchronizer<V> valueSynchronizer;

    protected ActionManager actionManager;

    public SharedHashMap(ActionManager actionManager, Synchronizer<K> keySynchronizer, Synchronizer<V> valueSynchronizer){
        this.actionManager = actionManager;
        this.keySynchronizer = keySynchronizer;
        this.valueSynchronizer = valueSynchronizer;
        this.content = new HashMap<>();
        registerCollectionActions();
    }

    protected int syncRequestHandle;
    protected int syncDataHandle;
    protected int putActionHandle;
    protected int removeActionHandle;
    protected int elementActionHandle;

    private void registerCollectionActions(){
        syncRequestHandle = actionManager.registerAction(SyncRequest::new);
        syncDataHandle = actionManager.registerAction(SyncData::new);
        putActionHandle = actionManager.registerAction(PutAction::new);
        removeActionHandle = actionManager.registerAction(RemoveAction::new);
        elementActionHandle = actionManager.registerAction(ElementAction::new);
    }

    private final ArrayList<Deserializer<TargetedAction<V>>> elementActions = new ArrayList<>();
    private final HashMap<Class<?>, Integer> elementActionMap = new HashMap<>();
    public void registerElementAction(Deserializer<TargetedAction<V>> deserializer, Class<?> type){
        elementActionMap.put(type, elementActions.size());
        elementActions.add(deserializer);
    }

    public void sendAction(K key, TargetedAction<V> action) throws IOException {
        actionManager.sendActionRequest(new ElementAction(key, action), elementActionHandle);
    }

    public boolean put(K key, V value){
        try{
            return actionManager.sendActionRequest(new PutAction(key, value), putActionHandle);
        }catch (Exception e){
            return false;
        }
    }

    public boolean remove(K key){
        try {
            return actionManager.sendActionRequest(new RemoveAction(key), removeActionHandle);
        }catch (Exception e){
            return false;
        }
    }

    public V get(K key){
        return content.get(key);
    }

    public Set<Map.Entry<K, V>> entrySet(){
        return content.entrySet();
    }

    protected ChangeListener changeListener = () -> {
        // Do nothing
    };
    public void setSyncListener(ChangeListener listener){
        this.changeListener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }

    private void packageTargetedAction(SerializingOutputStream out, TargetedAction<V> action){
        Integer id = elementActionMap.get(action.getClass());
        if(id == null){
            throw new RuntimeException("Unregistered element action: " + action.getClass().getName());
        }
        out.writeInt(id);
        action.serialize(out);
    }

    private TargetedAction<V> unpackageTargetedAction(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return elementActions.get(in.readInt()).deserialize(in);
    }

    private class ElementAction implements Action {
        private final K key;
        private final TargetedAction<V> action;

        public ElementAction(K key, TargetedAction<V> action){
            this.key = key;
            this.action = action;
        }

        @Override
        public boolean apply() {
            if(!content.containsKey(key)){
                return false;
            }
            return action.apply(content.get(key));
        }

        @Override
        public void revert() {
            action.revert(content.get(key));
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            keySynchronizer.serializer.serialize(key, out);
            packageTargetedAction(out, action);
        }

        // Deserializer
        public ElementAction(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            this.key = keySynchronizer.deserializer.deserialize(in);
            this.action = unpackageTargetedAction(in);
        }
    }

    private class RemoveAction implements Action {
        private final K key;
        private V value = null; // Local - only used for revert
        public RemoveAction(K key){
            this.key = key;
        }

        @Override
        public boolean apply() {
            if(content.containsKey(key)){
                value = content.remove(key);
                return true;
            }
            changeListener.onChange();
            return false;
        }

        @Override
        public void revert() {
            content.put(key, value);
            changeListener.onChange();
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            keySynchronizer.serializer.serialize(key, out);
            changeListener.onChange();
        }

        // Deserializer
        public RemoveAction(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            this.key = keySynchronizer.deserializer.deserialize(in);
        }
    }

    private class PutAction implements Action {
        private final K key;
        private final V value;
        public PutAction(K key, V value){
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean apply() {
            if(content.containsKey(key)){
                return false;
            }
            content.put(key, value);
            changeListener.onChange();
            return true;
        }

        @Override
        public void revert() {
            content.remove(key);
            changeListener.onChange();
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            keySynchronizer.serializer.serialize(key, out);
            valueSynchronizer.serializer.serialize(value, out);
        }

        // Deserializer
        public PutAction(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            this.key = keySynchronizer.deserializer.deserialize(in);
            this.value = valueSynchronizer.deserializer.deserialize(in);
        }
    }

    // Actions with different server/client behavior:

    protected class SyncRequest implements Action {
        public SyncRequest(){
            // No data
        }

        @Override
        public boolean apply() {
            applySyncRequest(this);
            changeListener.onChange();
            return true;
        }

        @Override
        public void revert() {
            // Do nothing
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            // No data
        }

        // Deserializer
        public SyncRequest(SerializingInputStream in) {
            // No data
        }
    }

    protected class SyncData implements Action {
        protected SerializingInputStream incompleteStream;
        public SyncData(){
            // No data - data gets pushed when the object is serialized
        }

        @Override
        public boolean apply() {
            applySyncData(this);
            System.out.println("apply sync response");
            return true;
        }

        @Override
        public void revert() {
            System.out.println("revert sync response");
            // Do nothing
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            out.writeInt(content.entrySet().size());
            for(Map.Entry<K, V> entry : content.entrySet()){
                keySynchronizer.serializer.serialize(entry.getKey(), out);
                valueSynchronizer.serializer.serialize(entry.getValue(), out);
            }
        }

        // Deserializer
        public SyncData(SerializingInputStream in){
            this.incompleteStream = in;
        }
    }

    protected abstract void applySyncRequest(SyncRequest syncRequest);
    protected abstract void applySyncData(SyncData syncData);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<K, V> entry : content.entrySet()){
            sb.append(entry.getKey().toString());
            sb.append("\t:\t");
            sb.append(entry.getValue().toString());
            sb.append("\n");
        }
        if(sb.length() == 0){
            return "";
        }
        return sb.substring(0, sb.length() - 1);
    }
}
