package Players;

import Common.Coordinate;
import Common.Direction;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;

import java.util.Optional;
import java.util.Set;

/**
 * Our abstract class to be used by our different strategy classes that implement Strategy.
 */
abstract class AStrategy {

    private PlayerGameState playerGameState;
    private PlayerInfoPublic playerInfoPublic;
    private PlayerInfoPrivate playerInfoPrivate;

    /**
     * Creates a new AStrategy
     * @param refStateForSinglePlayer an instance of PublicGameState
     * @param playerInfoPublic the PlayerInfoPublic of the player using the strategy
     * @param playerInfoPrivate the PlayerInfoPrivate of the player using the strategy
     */
    public AStrategy(PlayerGameState refStateForSinglePlayer, PlayerInfoPublic playerInfoPublic,
                     PlayerInfoPrivate playerInfoPrivate) {
        this.playerGameState = refStateForSinglePlayer;
        this.playerInfoPublic = playerInfoPublic;
        this.playerInfoPrivate = playerInfoPrivate;
    }

    public AStrategy() {}

    /**
     * Getter for the publicGameState
     * @return the publicGameState
     */
    public PlayerGameState getPlayerGameState() {
        return playerGameState;
    }

    /**
     * Getter for the playerInfoPublic
     * @return the playerInfoPublic
     */
    public PlayerInfoPublic getPlayerInfoPublic() {
        return playerInfoPublic;
    }

    /**
     * Getter for the playerInfoPrivate
     * @return the playerInfoPrivate
     */
    public PlayerInfoPrivate getPlayerInfoPrivate() {
        return playerInfoPrivate;
    }

    public void setPlayerGameState(PlayerGameState playerGameState) {
        this.playerGameState = playerGameState;
    }

    public void setPlayerInfoPublic(PlayerInfoPublic playerInfoPublic) {
        this.playerInfoPublic = playerInfoPublic;
    }

    public void setPlayerInfoPrivate(PlayerInfoPrivate playerInfoPrivate) {
        this.playerInfoPrivate = playerInfoPrivate;
    }

    protected Coordinate getGoalEitherHomeOrTreasure() {
        Coordinate desiredGoal;
        if (this.getPlayerInfoPrivate().isGoingHome()) {
            desiredGoal = this.getPlayerInfoPublic().getHome();
        } else {
            desiredGoal = this.getPlayerInfoPrivate().getGoal();
        }
    return desiredGoal;
    }

    /**
     * Try all possible turns for a player to see if they can reach their desired gaol and return the
     * corresponding IFullTurnInfo, the IFullTurnInfo will be an instance of PassTurnInfo if it is not possible
     * to reach the desiredGoal
     * @param desiredGoal a Coordiante of the players desired goal they want to reach
     * @return the IFullTurnInfo that corresponds to how to reach the desired goal, that will be an
     * instance of PassTurnInfo if it is not possible
     */
    protected Optional<FullTurnInfo> tryAllTurnsOnCoord(Coordinate desiredGoal) {
        //try rows first
        Optional<FullTurnInfo> reachedDesired = tryAllTurnsOnCoordRowOrCol(desiredGoal, this.playerGameState.getBoardState().getRowCount(),
                new Direction[]{Direction.LEFT, Direction.RIGHT});

        if(reachedDesired.isPresent()){
            return reachedDesired;
        }

        return tryAllTurnsOnCoordRowOrCol(desiredGoal,
                this.playerGameState.getBoardState().getColCount(), new Direction[]{Direction.UP, Direction.DOWN});
    }

    /**
     * Try all possible turns on either all the rows or all the columns and return a IFullTurnInfo that will contain
     * all the information on how to complete that turn, or specifically an instance of PassTurnInfo if none of the
     * turns make it possible to reach the desired goal
     * @param desiredGoal  the Coordinate of the desired goal of the player
     * @param maxRowOrCol an int that is the max number of rows or columns present in the baord
     * @param directionsForRowOrCol an array of Directions that is either [Direction.UP, Direction.DOWN]
     *                              or [Direction.LEFT, Direction.RIGHT] to be able to check eitehr all the moves
     *                              with sliding colums or all the moves with sliding rows
     * @return a IFullTurnInfo that will contain all the information on how to complete that turn,
     * or specifically an instance of PassTurnInfo if none of the turns make it possible to reach the desired goal
     */
    private Optional<FullTurnInfo> tryAllTurnsOnCoordRowOrCol(Coordinate desiredGoal,
                                                 int maxRowOrCol, Direction[] directionsForRowOrCol) {
        //loop for index
        //only go through the even columns/rows
        for (int i = 0; i < maxRowOrCol; i = i + 2) {

            //loop for direction
            for (Direction d : directionsForRowOrCol) {

                //loop for rotation
                for (int deg = 0; deg < 360; deg += 90) {
                    try {
                        PlayerGameState newGS = this.playerGameState.tryTurn(d, i, deg);
                        //this returns with the new PlayerGameState with a different board
                        Set<Coordinate> reachables = newGS.getBoardState().reachable(newGS.getCurrentPublicPlayerInfo().getCurrentCoord());
                        //run reachable on this new board

                        // TODO
                        // rather than using getCurrentPublicPlayerInfo,
                        // get with publicinfo from state with same home as this.publicplayerinfo
                        // so not just currentplayer can use this

                        if (reachables.contains(desiredGoal) && !newGS.getCurrentPublicPlayerInfo().getCurrentCoord().equals(desiredGoal)){
                            return Optional.of(new FullTurnInfo(deg, new ShiftInfo(i, d), desiredGoal));
                            //we're giving the rotation deg, the index and direction in SeptOne, and the desiredGoal
                        }
                    }
                    catch (Exception e) { }
                }
            }
        }
        return Optional.empty();
    }
}

