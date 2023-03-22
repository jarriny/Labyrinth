package Players;

import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Our representation of the Riemann strategy
 */
public class Riemann extends AStrategy implements Strategy {


    /**
     * Constructor for a new Riemann
     * @param stateForSinglePlayer the ref game state
     * @param playerInfoPublic the public information about the player
     * @param playerInfoPrivate the private information of the player
     */
    public Riemann(PlayerGameState stateForSinglePlayer, PlayerInfoPublic playerInfoPublic,
                   PlayerInfoPrivate playerInfoPrivate) {
        super(stateForSinglePlayer, playerInfoPublic, playerInfoPrivate);
    }

    public Riemann() {
        super();
    }

    public void setInfo(PlayerGameState playerGameState, PlayerInfoPublic playerInfoPublic,
                           PlayerInfoPrivate playerInfoPrivate) {
        super.setPlayerGameState(playerGameState);
        super.setPlayerInfoPublic(playerInfoPublic);
        super.setPlayerInfoPrivate(playerInfoPrivate);
    }

    /**
     * Finds the best turn possible for a specific player
     * @return an instance of IFullTurnInfo that represents all the information needed to execute the best turn
     * that was found; void is returned if no best turn was found
     */
    @Override
    public Optional<FullTurnInfo> findBestTurn() {

        Coordinate desiredGoal = getGoalEitherHomeOrTreasure();

        ArrayList<Coordinate> mostDesiredInOrder = getRealGoalAndAltGoals(desiredGoal);

        for (Coordinate c : mostDesiredInOrder) {
            Optional<FullTurnInfo> reachedDesired = super.tryAllTurnsOnCoord(c);
            if (reachedDesired.isPresent()) {
                return reachedDesired;
            }
        }
        //at this point we have tried to get to every possible tile, and none have worked
        return Optional.empty();
    }

    /**
     * A helper method that returns an ArrayList of coordinates that is an ordered list of goals that the tile
     * may wish to reach next if they cannot reach their desired goal
     * @param realGoal a coordinate representation of the player's most desired goal
     * @return an ordered arraylist of coordinates that represents the possible goals of the tile
     */
    private ArrayList<Coordinate> getRealGoalAndAltGoals(Coordinate realGoal) {
        ArrayList<Coordinate> altGoals = new ArrayList<>();

        for (int r = 0; r < this.getPlayerGameState().getBoardState().getRowCount(); r++) {
            for (int c = 0; c < this.getPlayerGameState().getBoardState().getColCount(); c++) {
                altGoals.add(new Coordinate(r,c));
            }
        }

        altGoals.remove(realGoal);
        altGoals.add(0, realGoal);

        return altGoals;
    }
}