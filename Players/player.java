package Players;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.State.PlayerGameState;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Our implementation of a player
 */
public class player implements IPlayer{
    private static final String NAME_PATTERN = "^[a-zA-Z0-9]+$";
    private final String name;
    private final Strategy strategy;
    private PlayerInfoPrivate privateInfo;
    private boolean won;

    /**
     * Constructor for a player
     * @param strategy a string that corresponds with a strategy from our strategy interface
     *             that the player will use to make their turns
     */
    public player(Strategy strategy, String name) {
        this.strategy = strategy;
        this.won = false;
        if (!name.matches(NAME_PATTERN) && name.length() > 0 && name.length() <= 20) {
            throw new IllegalArgumentException("Invalid Name");
        }
        this.name = name;
    }


    public player(String strategy, String name) {
        if(strategy.equals("Euclid")){
            this.strategy = new Euclid();
        } else if (strategy.equals("Riemann")) {
            this.strategy = new Riemann();
        }
        else{
            throw new IllegalArgumentException("Illegal strategy given");
        }

        this.won = false;
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public boolean isWon() {
        return won;
    }

    public PlayerInfoPrivate getPrivateInfo() {
        return privateInfo;
    }

    /**
     * The player proposes a board given a minimum number of rows and columns
     * @param rows an int representing the minimum rows
     * @param columns an int representing the minimum columns
     * @return an instance of our Board class
     */
    public Board proposeBoard0(int rows, int columns) {
        Tile[] arrayOfTiles = new Tile[rows * columns];
        ArrayList<GemPair> madePairs = new ArrayList<>();
        int index = 0;

        for (Gem g1: Gem.values()) {
            for (Gem g2: Gem.values()) {
                GemPair gp = new GemPair(g1, g2);
                if (!g1.isAlphabeticallyBefore(g2)) {
                    gp = new GemPair(g2, g1);
                }
                if (!madePairs.contains(gp)) {
                    madePairs.add(gp);
                    arrayOfTiles[index] = new Tile(new boolean[]{true,false,false,true}, gp);
                    index++;
                }
                if (index == rows * columns) {
                    return new Board(rows, columns, arrayOfTiles);
                }
            }
        }
        throw new IllegalArgumentException("should never reach");
    }

    /**
     * The player is given a (private) goal that it must visit next and optionally
     * the current initial state of the game
     * If state0 is None, setup is used to tell the player go-home and goal is just a reminder where home is
     * @param state0 an instance PlayerGameState that represents the current initial state of the game
     * @param goal a coordinate that represents a (private) goal that it must visit next
     */
    public void setup(Optional<PlayerGameState> state0, Coordinate goal) {
        if (state0.isEmpty()){
            //the state is not present
            //the player reaches the assigned treasures-target with this turn
            this.privateInfo.onWayHome();
        }
        else {
            if(this.privateInfo != null){
                this.privateInfo.setNewGoal(goal);
                this.privateInfo.incrementGoalsReached();
            }
            else{
                this.privateInfo = new PlayerInfoPrivate(goal);
            }
        }
    }

    /**
     *The player receives the current PlayerGameState and uses this and this.strat to determine
     * if they want to take a turn or pass
     * @param state the current PlayerGameState
     * @return an instance of ITurnInfo that will contain all the information needed to take a turn or
     * void is returned if no best turn was found
     */
    public Optional<FullTurnInfo> takeTurn(PlayerGameState state) {
        this.strategy.setInfo(state, state.getCurrentPublicPlayerInfo(), this.privateInfo);
        return strategy.findBestTurn();
    }

    /**
     * The player is told that if they won the game, and outputs a corresponding message
     * @param b a boolean that is true if the player won the game
     */
    public String win(boolean b) {
        // temp
        if (b) {
            won = true;
            return ":)";
        }
        else {
            return ":(";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        player player = (player) o;
        return Objects.equals(strategy, player.strategy) && Objects.equals(privateInfo, player.privateInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strategy, privateInfo);
    }

    @Override
    public String toString() {
        return "player{" +
                "strat='" + strategy + '\'' +
                ", privateInfo=" + privateInfo +
                '}';
    }
}
