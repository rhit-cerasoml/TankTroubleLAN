package tanktrouble.game.client.util.actions;

import tanktrouble.generated.util.serial.Serializable;

// Actions must be fully atomic - failure to apply should mean the prior state is maintained. This is crucial for
// effective client-side prediction (Dead Reckoning)
public interface Action extends Serializable {
    boolean apply();

    // Revert can be empty if and only if apply cannot return false
    void revert();
}
