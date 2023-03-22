package Referee;

import Common.State.RefState;

/**
 * Our implementation of an interface for observers
 */
public interface IObserver {

    /**
     * Outputs the corresponding GUI and JSON representation
     * @param state the current referee game state
     */
    void receiveState(RefState state);

    /**
     *Called by the referee if the game is over
     */
    void gameOver();
}
