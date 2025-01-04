package tanktrouble.game.client.util.sharedhashmap;

import tanktrouble.game.client.util.actions.ActionManager;
import tanktrouble.game.client.util.serial.Synchronizer;

public class SharedHashMapServer<K, V> extends SharedHashMap<K, V> {

    public SharedHashMapServer(ActionManager actionManager, Synchronizer<K> keySynchronizer, Synchronizer<V> valueSynchronizer) {
        super(actionManager, keySynchronizer, valueSynchronizer);
    }

    @Override
    protected void applySyncRequest(SyncRequest syncRequest) {
        SyncData syncData = new SyncData();
        try {
            actionManager.sendActionRequest(syncData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void applySyncData(SyncData syncData) {
        // Do nothing - this isn't a supported request on server
    }
}
