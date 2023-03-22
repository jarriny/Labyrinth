package Players;

import Common.Board.Board;
import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.State.PlayerGameState;

import java.util.Optional;

/**
 * Interface for players
 */
public interface IPlayer {

    String name();

    /**
     * The player proposes a board given a minimum number of rows and columns
     * @param rows an int representing the minimum rows
     * @param columns an int representing the minimum columns
     * @return an instance of our Board class
     */
    Board proposeBoard0(int rows, int columns) throws Exception;

    /**
     * The player is given a (private) goal that it must visit next and optionally
     * the current initial state of the game
     * If state0 is None, setup is used to tell the player go-home and goal is just a reminder where home is
     * @param state0 an instance PlayerGameState that represents the current initial state of the game
     * @param goal a coordinate that represents a (private) goal that it must visit next
     */
    void setup(Optional<PlayerGameState> state0, Coordinate goal) throws Exception;


    /**
     *The player receives the current PlayerGameState and responds with a turn or a pass
     * @param state the current PlayerGameState
     * @return an instance of ITurnInfo that will contain all the information needed to take a turn or
     * Optional.empty() is returned if no best turn was found
     */
    Optional<FullTurnInfo> takeTurn(PlayerGameState state) throws Exception;

    /**
     * The player is told that if they won the game, and outputs a corresponding message
     * @param b a boolean that is true if the player won the game
     */
    String win(boolean b) throws Exception;
}
