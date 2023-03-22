package Players;

import Common.PartsOfTurn.FullTurnInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;

import java.util.Optional;

/**
 * Our interface implementation of a Strategy that a player will use to find the best turn they can take.
 */
public interface Strategy {

    /**
     * Finds the best turn possible for a specific player
     * @return an instance of IFullTurnInfo that represents all the information needed to execute the best turn
     * that was found, or void if no best turn was found
     */
    Optional<FullTurnInfo> findBestTurn();

    /**
     * Sets the strategy with needed information
     * @param playerGameState the public game state
     * @param playerInfoPublic  the public information about the player
     * @param playerInfoPrivate the private information of the player
     */
    void setInfo(PlayerGameState playerGameState, PlayerInfoPublic playerInfoPublic, PlayerInfoPrivate playerInfoPrivate);
}
