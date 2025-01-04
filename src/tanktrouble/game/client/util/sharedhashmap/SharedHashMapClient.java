package tanktrouble.game.client.util.sharedhashmap;

import tanktrouble.game.client.util.actions.ActionManager;
import tanktrouble.game.client.util.serial.Synchronizer;

public class SharedHashMapClient<K, V> extends SharedHashMap<K, V> {

    public SharedHashMapClient(ActionManager actionManager, Synchronizer<K> keySynchronizer, Synchronizer<V> valueSynchronizer) {
        super(actionManager, keySynchronizer, valueSynchronizer);
        try {
            actionManager.sendActionRequest(new SyncRequest());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void applySyncRequest(SyncRequest syncRequest) {
        // Do nothing - this isn't a supported request on client
    }

    @Override
    protected void applySyncData(SyncData syncData) {
        try {
            content.clear();
            int len = syncData.incompleteStream.readInt();
            for(int i = 0; i < len; i++){
                content.put(
                    keySynchronizer.deserializer.deserialize(syncData.incompleteStream),
                    valueSynchronizer.deserializer.deserialize(syncData.incompleteStream)
                );
            }
        }catch (Exception e){
            content.clear();
            e.printStackTrace();
        }
    }
}
