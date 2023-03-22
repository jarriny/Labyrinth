package Players;

import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Our representation of the Euclid strategy
 */
public class Euclid extends AStrategy implements Strategy {

    /**
     * Constructor for a new Euclid
     * @param playerGameState the public game state
     * @param playerInfoPublic  the public information about the player
     * @param playerInfoPrivate the private information of the player
     */
    public Euclid(PlayerGameState playerGameState, PlayerInfoPublic playerInfoPublic,
                  PlayerInfoPrivate playerInfoPrivate) {
        super(playerGameState, playerInfoPublic, playerInfoPrivate);
    }

    public Euclid() {}

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
        ArrayList<Coordinate> mostDesiredInOrder = orderByClosestToGoalTile(desiredGoal);
        for (Coordinate c : mostDesiredInOrder) {

            Optional<FullTurnInfo> reachedDesired = super.tryAllTurnsOnCoord(c);
            if (reachedDesired.isPresent()) {
                return reachedDesired;
            }
        }
        return Optional.empty();
    }

    /**
     * Creates an ArrayList<Coordinate> of every coordinate on the board, sorted by how close it is to
     * the preferred goal tile
     * @param goal the Coordinate of the preferred goal tile
     * @return the sorted ArrayList<Coordinate>
     */
    private ArrayList<Coordinate> orderByClosestToGoalTile(Coordinate goal) {
        TreeMap<Double, ArrayList<Coordinate>> mapInOrderByDistance = new TreeMap<>();
        for (int r = 0; r < this.getPlayerGameState().getBoardState().getRowCount(); r++) {
            for (int c = 0; c < this.getPlayerGameState().getBoardState().getColCount(); c++) {
                Coordinate coord = new Coordinate(r, c);
                Double distance = this.getDistance(goal, coord);
                if (mapInOrderByDistance.containsKey(distance)) {
                    mapInOrderByDistance.get(distance).add(coord);
                }
                else {
                    ArrayList<Coordinate> newCoordList = new ArrayList<>();
                    newCoordList.add(coord);
                    mapInOrderByDistance.put(distance, new ArrayList<>(newCoordList));
                }
            }
        }
        ArrayList<Coordinate> listInOrderByDistance = new ArrayList<>();
        for (ArrayList<Coordinate> coordList: mapInOrderByDistance.values()) {
            listInOrderByDistance.addAll(coordList);
        }
        return listInOrderByDistance;
    }

    /**
     * Calculates the distance between two coordinates
     * @param goal the desired goal coordinate
     * @param candidates a coordinate of a tile that we want to calculate the distance to the goal from
     * @return a double that represents the distance between the two coordinates
     */
    private double getDistance(Coordinate goal, Coordinate candidates) {
        return Math.sqrt(Math.pow((candidates.getCol() - goal.getCol()), 2) +
                Math.pow((candidates.getRow() - goal.getRow()), 2));
    }
}