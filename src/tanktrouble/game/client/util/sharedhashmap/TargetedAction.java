package tanktrouble.game.client.util.sharedhashmap;

import tanktrouble.generated.util.serial.Serializable;

public interface TargetedAction<V> extends Serializable {
    boolean apply(V target);
    boolean revert(V target);
}
