package Referee;

import Common.Board.Board;
import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Common.State.RefState;
import Players.IPlayer;
import Players.player;

import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Our implementation of a referee
 */
public class referee {
    private static final int TURN_TIMEOUT = 6;
    private static final int SETUP_TIMEOUT = 6;
    private static final int WON_TIMEOUT = 6;

    private final int MIN_ROW_NUM = 7;
    private final int MIN_COL_NUM = 7;

    private int rounds = 0;
    private int numPassedRoundsInARow = 0;

    private final ArrayList<IPlayer> kickedPlayers;
    private RefState state;
    private ArrayList<IPlayer> players;
    private final ArrayList<IObserver> observers;

    private final ArrayList<Coordinate> unusedGoals;

    /**
     * Our constructor for a referee; takes in no inputs and sets this.kickedPlayers and
     * this.unusedGoals to be empty lists
     */
    public referee() {
        this.kickedPlayers = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.unusedGoals = new ArrayList<>();
       // this.multipleGoals = false;
    }

    /**
     * use for milestone 9 testing, where multiple goals isn't included
     * */
    public referee(boolean multipleGoals) {
        this.kickedPlayers = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.unusedGoals = findAllUnusedGoals();
    }

    /**
     * Constructor for a referee that is given a list of unused goals to use.
     * @param unusedGoals an ArrayList of Coordinates that represents unused goals
     */
    public referee(Optional<ArrayList<Coordinate>> unusedGoals) {
        this.kickedPlayers = new ArrayList<>();
        this.observers = new ArrayList<>();
        if (unusedGoals.isPresent()) {
            validateInputedGoals(unusedGoals.get());
            this.unusedGoals = unusedGoals.get();
        } else {
            this.unusedGoals = findAllUnusedGoals();
        }
    }

    /**
     * Adds the given observer
     * @param observer an instance of IObserver to be added ot the list of observers
     */
    public void addObserver(IObserver observer){
        observers.add(observer);
    }

    /**
     * Removes the given observer
     * @param observer an instance of IObserver to be removed from the list of observers
     */
    public void removeObserver(IObserver observer){
        observers.remove(observer);
    }

    /**
     * Runs an entire game to completion and lets all players know the outcome of the game
     * @param state an instance of RefState that represents the current state of the board
     * @param players an arraylist of current players
     */
    public gameResult runEntireGame(RefState state, ArrayList<IPlayer> players) {
        if (state.getPublicPlayerInfos().size() != players.size()) {
            throw new IllegalArgumentException("amount of players given and amount of players in given state is different");
        }
        if (!this.validateNames(players)) {
            throw new IllegalArgumentException("all players must have unique names");
        }
        if (!this.validateGoalsAndHomes(state)) {
            throw new IllegalArgumentException("Goal and Home coordinates must be on unmovable tiles");
        }
        this.state = state;
        this.players = players;

        this.tellAllPlayersStateAndGoal();
        //gives observer the initial state
        for (IObserver observer: observers) {
            observer.receiveState(this.state);
        }

        while (this.players.size() > 0 && !this.isGameOver()) {
            runRound();
        }

        ArrayList<IPlayer> winners = computeOutcomeOfState(this.state, this.players);
        ArrayList<IPlayer> losers = new ArrayList<>(this.players);
        losers.removeAll(winners);
        //losers.addAll(kickedPlayers);

        for (IObserver observer: observers) {
            observer.gameOver();
        }

        return new gameResult(winners, kickedPlayers);
    }

    /**
     * Gets the goals that have not be used yet.
     * @return an ArrayList of Coordinates of tiles that haven't been assigned as goals yet
     */
    private ArrayList<Coordinate> findAllUnusedGoals() {

        ArrayList<Coordinate> usedGoals = findAllUsedGoals();
        ArrayList<Coordinate> allGoals = findAllPossibleGoals();
        ArrayList<Coordinate> unusedGoals = new ArrayList<>();

        for(Coordinate allCoord: allGoals){
            if(! usedGoals.contains(allCoord)){
                unusedGoals.add(allCoord);
            }
        }

        return unusedGoals;
    }

    /**
     * Gets the goals that are already assigned to the players in the game.
     * @return an ArrayList of Coordinates of tiles that are already used as goals
     */
    private ArrayList<Coordinate> findAllUsedGoals(){
        //get all the goals that are already assigned to the players
        ArrayList<Coordinate> alreadyAssigned = new ArrayList<>();
        for(PlayerInfoPrivate playerInfo: this.state.getPrivatePlayerInfos()){
            alreadyAssigned.add(playerInfo.getGoal());
        }
        return alreadyAssigned;
    }

    /**
     * Gets all of the possible goals of a board, i.e immovable tiles (the tiles with (odd,odd) coordinate)
     * @return an ArrayList of Coordinates that are possible goals
     */
    private ArrayList<Coordinate> findAllPossibleGoals(){
        //gather all possible goals
        ArrayList<Coordinate> allPossibleGoals = new ArrayList<>();

        for(int row = 1; row < state.getBoardState().getRowCount(); row = row + 2){
            for(int col = 1; col < state.getBoardState().getColCount(); col = col + 2){
                allPossibleGoals.add(new Coordinate(row, col));

            }
        }
        return allPossibleGoals;
    }

    /**
     * Runs a round with the current players and kicks out players if needed
     */
    private void runRound() {
        this.numPassedRoundsInARow = 0;
        int numPlayersAtRoundStart = this.players.size();
        for (int p = 0; p < numPlayersAtRoundStart; p++) {
            havePlayerDoTurn();

            for (IObserver observer: this.observers) {
                observer.receiveState(this.state);
            }

            if (isGameOver()) {
                return;
            }
        }
        rounds++;
    }

    /**
     * Returns the index of the player who made the last move
     * @return an int that is the index of the player who made the last move
     */
    private int getIndexOfMostRecentPlayer(){

        if(this.state.getIndexOfCurrentPlayer() - 1 < 0){
            return this.state.getPublicPlayerInfos().size() - 1;
        }
        else{
            return this.state.getIndexOfCurrentPlayer() - 1;
        }
    }

    /**
     * Given a RefState that represents a final game state and corresponding arraylist of players
     * Will let players know if they win or lose
     * @param state an instance of RefState that represents the current state of the board
     * @param players an arraylist of current players
     */
    private ArrayList<IPlayer> computeOutcomeOfState(RefState state, ArrayList<IPlayer> players) {
        this.state = state;
        this.players = players;
//        ArrayList<IPlayer> winners = winnersIfExistsORGINIAL();
//
//        // use for this week's testing, where multiple goals isn't included
//        if (multipleGoals) {
//            winners = winnersIfExists();
//        }

        ArrayList<IPlayer> winners = winnersIfExists();
        tellWinnersOutcome(winners);

        return winners;
    }

    /**
     * Calls setup for every player when the game has not yet started, aka no turns have happened yet
     */
    private void tellAllPlayersStateAndGoal() {
        for (int p = this.players.size() - 1; p >= 0; p--) {
            this.setup(p, false);
        }
    }

    /**
     * Validate names of players
     * @param players players whose names to validate
     */
    private boolean validateNames(ArrayList<IPlayer> players) {
        HashSet<String> names = new HashSet<>();
        for (IPlayer p : players) {
            if (names.contains(p.name())) {
                return false;
            }

            if (!p.name().matches("^[a-zA-Z0-9]+$")) {
                return false;
            }
            names.add(p.name());
        }
        return true;
    }

    /**
     * Checks if all goals and homes in given state are on unmovable tiles
     * @param state state to validate
     */
    private boolean validateGoalsAndHomes(RefState state) {
        HashSet<Coordinate> homes = new HashSet<>();
        for (PlayerInfoPublic playerInfoPublic : state.getPublicPlayerInfos()) {
            Coordinate home = playerInfoPublic.getHome();
            homes.add(home);
            if (home.getRow() % 2 == 0 || home.getCol() % 2 == 0) {
                throw new IllegalArgumentException("Home on a movable tile: " + home);
                //return false;

            }

        }
        if (homes.size() != state.getPublicPlayerInfos().size()) {
            throw new IllegalArgumentException("Some homes are on the same tile");
        }

        for (PlayerInfoPrivate playerInfoPrivate : state.getPrivatePlayerInfos()) {
            Coordinate goal = playerInfoPrivate.getGoal();
            if (goal.getRow() % 2 == 0 || goal.getCol() % 2 == 0) {
                throw new IllegalArgumentException("Goal on a movable tile: " + goal);
            }
        }
        return true;
    }


    /**
     * Kicks out a player if they throw an error or times out when they are told that they won or not
     * @param index the index of the player
     * @param winners a list of IPlayers that won the game
     */
    private void callPlayerWin(int index, ArrayList<IPlayer> winners){
        IPlayer player = players.get(index);
        FutureTask<?> future = new FutureTask<>(() -> {
            try {
                player.win(winners.contains(player));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }, null);
        Thread t = new Thread(future);
        t.start();

        try{
            future.get(WON_TIMEOUT, TimeUnit.SECONDS);
        }
        catch(Exception e){
            // will catch any error throw from invalid and illegal JSON given to the proxy player
            // as well as a timeout error
            // or logical errors
            future.cancel(true);
            kickOutPlayer(index);
        }
        t.stop();
    }

    /**
     * Calls win() for every player to tell them if they won or not
     * calls win() for every player in kickedPlayers to tell them lost
     * @param winners a boolean that is true if the player won, and false if they lost
     */
    private void tellWinnersOutcome(ArrayList<IPlayer> winners) {
        for (int i = players.size() - 1; i >= 0; i--) {
            callPlayerWin(i, winners);
        }
        // winners.removeAll(kickedPlayers);
    }

    /**
     * This method is not needed for Milestone 5 and is therefore not completed
     * This method will call proposeBoard for every player in the given arraylist of players and then
     * return one of the boards
     * @param players an arraylist of players to ask to propose a board
     * @return a board that was selected and created by a player
     */
    private Board decideOnBoard(ArrayList<player> players) {

        ArrayList<Board> proposedBoards = new ArrayList<Board>();
        for (Players.player player : players) {
            proposedBoards.add(player.proposeBoard0(MIN_ROW_NUM, MIN_COL_NUM));

        }
        //already checked within board that they are valid

        //now make a new state with a player-made board
        return proposedBoards.get(new Random().nextInt() % players.size());
    }

    /**
     * Kicks out a player if they throw an error or times out when setup is called
     * @param index the index of the player that setup is called on
     * @param state the current PlayerGameState
     * @param goal the player's goal coordinate
     */
    private void callPlayerSetUp(int index, Optional<PlayerGameState> state, Coordinate goal){
        FutureTask<?> future = new FutureTask<>(()-> {
            try {
                this.players.get(index).setup(state, goal);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }, null);
        Thread t = new Thread(future);
        t.start();

        try{
            future.get(SETUP_TIMEOUT, TimeUnit.SECONDS);
        }
        catch(Exception e) {
            // will catch any error throw from invalid and illegal JSON given to the proxy player
            // as well as a timeout error
            // or logical errors
            future.cancel(true);
            System.out.println("Kicked out because of " + e + ": " + e.getMessage());
            this.kickOutPlayer(index);
        }

        t.stop();
    }


    /**
     * Calls setup for the corresponding player in this.players, and sends them their current goal tile
     * The player is also the current PlayerGameState if no turns have been made yet
     * @param playerIndex the index of a player within this.players
     * @param turnsMade a boolean that is false if no turns have occurred yet
     */
    private void setup(int playerIndex, boolean turnsMade) {
        Optional<PlayerGameState> stateToGive = Optional.empty();

        if (!turnsMade ) {
            stateToGive = Optional.of(state.toPlayerGameState());
        }


        PlayerInfoPrivate infoPrivate = state.getPrivatePlayerInfos().get(playerIndex);
        Coordinate goal = infoPrivate.getGoal();
        if (infoPrivate.isGoingHome()){
            goal = this.state.getCurrentPublicPlayerInfo().getHome();
        }
        else{
            stateToGive = Optional.of(state.toPlayerGameState());
        }
        this.callPlayerSetUp(playerIndex, stateToGive, goal);
    }

    /**
     * Calls takeTurn on the current player and sends them the current PlayerGameState
     * Will receive back an instance of ITurnInfo that will represent the
     * information needed to take the desire turn, or void will be returned which represents that
     * the player is passing their turn
     * Throw timeout if player takes too long to respond
     */
    private void havePlayerDoTurn() {
        int index = this.state.getIndexOfCurrentPlayer();
        // get index of current player relative to referee and not state
        FutureTask<Optional<FullTurnInfo>> futureOfTurnInfo = new FutureTask<>(() ->
                this.players.get(index).takeTurn(this.state.toPlayerGameState()));

        Thread t = new Thread(futureOfTurnInfo);
        t.start();
        try {
            Optional<FullTurnInfo> turnInfo = futureOfTurnInfo.get(TURN_TIMEOUT, TimeUnit.SECONDS);
            if (!checkIfValidTurn(turnInfo)) {
                throw new IllegalArgumentException("Invalid move");
            }

            nextTurn(turnInfo);
        } catch (Exception e) {
            // will catch any error throw from invalid and illegal JSON given to the proxy player
            // as well as a timeout error
            // or logical errors
            futureOfTurnInfo.cancel(true);
            t.stop();
            kickOutPlayer();
            System.out.println("Kicked out because of " + e + ": " + e.getMessage());
            return;
        }
        t.stop();
        // if player is on treasure set up is called again
        if (this.state.isOnGoalTile(index)) {
            if(unusedGoals.isEmpty()){
                this.state = this.state.playerGotToGoalTile(index, Optional.empty());
            }
            else{
                Coordinate newGoal = unusedGoals.remove(0);
                this.state = this.state.playerGotToGoalTile(index, Optional.of(newGoal));
            }

            setup(index, true);
        }
    }

    private void nextTurn(Optional<FullTurnInfo> turnInfo) {
        if (turnInfo.isPresent()) {
            this.state = this.state.completePlayerTurnAndReadyForNextPlayer(turnInfo.get());
        }
        else {
            this.state = this.state.goToNextPlayer();
            numPassedRoundsInARow++;
        }
    }

    /**
     * Checks if a move is valid based on last move, unmovable indexes, and out of bounds
     * @param optionalInfo the Optional move to validate
     * @return boolean whether move is valid
     */
    private boolean checkIfValidTurn(Optional<FullTurnInfo> optionalInfo) {
        if (optionalInfo.isEmpty()) {
            return true;
        }
        FullTurnInfo turnInfo = optionalInfo.get();
        int index = turnInfo.getShiftInfo().getIndex();
        return this.state.isValidMoveBasedOnLastMove(turnInfo.getShiftInfo()) ||
                index % 2 == 0 || index > 0 ||
                index < this.state.getBoardState().getRowCount() ||
                index < this.state.getBoardState().getColCount();
    }

    /**
     * Checks if any of the conditions for a game being over have been met
     * @return boolean whether game is over
     */
    private Boolean isGameOver() {
        return didAPlayerWinYet() || this.numPassedRoundsInARow == this.players.size() || this.rounds >= 1000;
    }

    /**
     * Checks if a player in current state has met win conditions
     * @return boolean whether game is over
     */
    private Boolean didAPlayerWinYet() {
        for (int p = 0; p < this.players.size(); p++) {
            //go through every player within state
            //the index of the publicplayer infos is the same as the private
            PlayerInfoPublic playersPublicInfo = this.state.getPublicPlayerInfos().get(p);
            PlayerInfoPrivate playersPrivateInfo = this.state.getPrivatePlayerInfos().get(p);

            //if they have their treasure, and they're at their home
            if (playersPrivateInfo.isGoingHome() &&
                    playersPublicInfo.getCurrentCoord().equals(playersPublicInfo.getHome())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any of the game completion conditions are true
     * If so an arraylist of players who are the winners are returned
     * else an empty arraylist is returned
     *
     **
     * @return an arraylist of players that is empty if game is not complete,
     *                          has size of 1 if a player reached home after gathering their treasure,
     *                          has size of >= 1 if multiple players won when 1000 rounds are played or all players pass
     *                          resulting in the end of the game
     */
    private ArrayList<IPlayer> winnersIfExistsORGINIAL() {
        ArrayList<IPlayer> winners = getPlayerThatGotTreasureAndBackHome();

        if (!winners.isEmpty()) {
            return winners;
        }
        //the referee has run 1000 rounds or all players that survive a round opt to pass
        else if (this.rounds >= 1000 || this.numPassedRoundsInARow >= this.players.size()) {
            return getWinniestPlayers();
        }
        else {
            return winners;
        }
    }

    /**
     * Gets the list of winning players. Winning players are players who have reached the most goals
     * and are the closest to their next destination. If one of these players is at their home, they are the
     * outright winner.
     * @return an ArrayList of the winners
     */
    protected ArrayList<IPlayer> winnersIfExists() {
        ArrayList<Integer> playersWithMostGoalsReached = getPlayersWithMostGoalsReached();

        // "If the game-terminating player is one of the players with the highest number of collected treasures,
        // it is the sole winner."
        Optional<IPlayer> soleWinner = getSoleWinner(playersWithMostGoalsReached);
        if (soleWinner.isPresent()) {
            ArrayList<IPlayer> soleWinnerInList = new ArrayList<>();
            soleWinnerInList.add(soleWinner.get());
            return soleWinnerInList;
        }
        // if there is only one player with the most goals reached, no need to compare
        // just get them based on their index
        else if (playersWithMostGoalsReached.size() == 1) {
            return getPlayersListFromIndexList(playersWithMostGoalsReached);
        }
        // determine who is closest to their goal
        else {
            return minEuclidDistanceFromGoal(playersWithMostGoalsReached);
        }
    }

    /**
     * Determines if there is a sole winner in the given list. A sole winner is a someone with the most treasures
     * reached and also the person who ended the game. Alternately, the person who is at their home tile.
     *
     * @param playersWithMostGoalsReached the list of players with the most goals reached
     * @return the sole winner if they exist, if not, an empty
     */
    private Optional<IPlayer> getSoleWinner(ArrayList<Integer> playersWithMostGoalsReached) {
        for (int index = 0; index < playersWithMostGoalsReached.size(); index++) {
            IPlayer player = this.players.get(playersWithMostGoalsReached.get(index));

            // go through the info of every player within state
            PlayerInfoPublic playersPublicInfo = this.state.getPublicPlayerInfos().get(playersWithMostGoalsReached.get(index));

            Coordinate current = playersPublicInfo.getCurrentCoord();
            Coordinate home = playersPublicInfo.getHome();

            if (current.equals(home)) {
                return Optional.of(player);
            }

        }

        return Optional.empty();
    }

    /**
     * Gets the IPlayer objects from the Referee that correspond to the indices in the given list.
     * @param indices a list of indices representing players
     * @return the mirrored list of IPlayers
     */
    private ArrayList<IPlayer> getPlayersListFromIndexList(ArrayList<Integer> indices) {
        ArrayList<IPlayer> playersFromIndices = new ArrayList<>();

        for (Integer index : indices) {
            playersFromIndices.add(this.players.get(index));
        }

        return playersFromIndices;
    }

    /**
     * Gets the players with the most goals reached
     * @return an ArrayList of the indices of the players with the most goals reached
     */
    protected ArrayList<Integer> getPlayersWithMostGoalsReached() {
        ArrayList<Integer> mostGoalsReached = new ArrayList<>();

        int max = 0;
        for (int p = 0; p < this.state.getPublicPlayerInfos().size(); p++) {
            // go through the info of every player within state
            PlayerInfoPrivate playersPrivateInfo = this.state.getPrivatePlayerInfos().get(p);

            // if they have the max count, add them to the list
            // if their count is higher than the max, clear the list and add them
            if (playersPrivateInfo.getGoalsReached() >= max) {
                if (playersPrivateInfo.getGoalsReached() > max) {
                    max = playersPrivateInfo.getGoalsReached();
                    mostGoalsReached.clear();
                }

                mostGoalsReached.add(p);
            }
        }
        return mostGoalsReached;
    }

    /**
     * Given a list of player indices, gets the players who are of the same minimal distance to their goal,
     * which is either a goal tile or their home tile.
     * @param playersToCheck an ArrayList of indices that represent players
     * @return the players that are closest to their goal
     */
    protected ArrayList<IPlayer> minEuclidDistanceFromGoal(ArrayList<Integer> playersToCheck) {
        ArrayList<IPlayer> playersWithMinEuclidDistanceFromGoal = new ArrayList<>();
        double minEuclidDistance = Double.MAX_VALUE;

        for (Integer integer : playersToCheck) {
            IPlayer player = this.players.get(integer);
            // go through the info of every player within state
            PlayerInfoPublic playersPublicInfo = this.state.getPublicPlayerInfos().get(integer);
            PlayerInfoPrivate playersPrivateInfo = this.state.getPrivatePlayerInfos().get(integer);

            // if they are not going home, their next destination is their next goal
            // if they are going home, their next destination is their home
            Coordinate playersNextDestination = getPlayersNextDestination(playersPublicInfo, playersPrivateInfo);
            Coordinate playersCurrent = playersPublicInfo.getCurrentCoord();

            double euclidDistanceFromGoal = getDistance(playersNextDestination, playersCurrent);

            // if they have the min distance, add them to the list
            // if their distance is lower than the min, clear the list and add them
            if (euclidDistanceFromGoal <= minEuclidDistance) {
                if (euclidDistanceFromGoal < minEuclidDistance) {
                    minEuclidDistance = euclidDistanceFromGoal;
                    playersWithMinEuclidDistanceFromGoal.clear();
                }

                playersWithMinEuclidDistanceFromGoal.add(player);
            }
        }
        return playersWithMinEuclidDistanceFromGoal;
    }

    /**
     * Gets the player corresponding to the given info's next destination, whether that be their home tile
     * or their current goal tile.
     * @param playersPublicInfo the public info of the player to check
     * @param playersPrivateInfo the private info of the player to check
     * @return the Coordinate of the intended player's next destination
     */
    private Coordinate getPlayersNextDestination(PlayerInfoPublic playersPublicInfo, PlayerInfoPrivate playersPrivateInfo) {
        if (playersPrivateInfo.isGoingHome()) {
            return playersPublicInfo.getHome();
        }
        else {
            return playersPrivateInfo.getGoal();
        }
    }

    /**
     * Checks if any player has both gathered their treasure and their currentCoordinate is their home tile;
     * @return an arraylist of players who have gathered their treasures adn are currently on their home tile
     */
    private ArrayList<IPlayer> getPlayerThatGotTreasureAndBackHome() {
        ArrayList<IPlayer> winner = new ArrayList<>();
        for (int p = 0; p < this.state.getPublicPlayerInfos().size(); p++) {
            //go through every player within state
            //the index of the publicplayer infos is the same as the private
            PlayerInfoPublic playersPublicInfo = this.state.getPublicPlayerInfos().get(p);
            PlayerInfoPrivate playersPrivateInfo = this.state.getPrivatePlayerInfos().get(p);

            //if they have their treasure, and they're at their home
            if (playersPrivateInfo.isGoingHome() && playersPublicInfo.getCurrentCoord().equals(playersPublicInfo.getHome())) {
                winner.add(this.players.get(p));
                return winner;
            }
        }
        return winner;
    }


    /**
     * In the case where a winner must be determined there are two cases for the players who should be returned in
     * the arraylist of players:
     * 1. if no players have reached their tile and are on their way back home, the winners are players that share
     * the same minimal Euclidean distance to their respective goal tile
     * 2. winners are all those players that are on their way back home from their goal tile and share the same
     * minimal Euclidean distance to their home tile
     * @return an arraylist of the winning players
     */
    private ArrayList<IPlayer> getWinniestPlayers() {
        TreeMap<Double, ArrayList<IPlayer>> playersByEuclidFromHomeIfGotTreasure = new TreeMap<>();
        TreeMap<Double, ArrayList<IPlayer>> playersByEuclidNoTreasure = new TreeMap<>();

        for (int p = 0; p < this.state.getPublicPlayerInfos().size(); p++) {
            PlayerInfoPublic playersPublicInfo = this.state.getPublicPlayerInfos().get(p);
            PlayerInfoPrivate playersPrivateInfo = this.state.getPrivatePlayerInfos().get(p);

            if (playersPrivateInfo.isGoingHome()) {
                this.addEuclidDistanceToMap(playersByEuclidFromHomeIfGotTreasure, playersPublicInfo.getCurrentCoord(),
                        playersPublicInfo.getHome(), p);
            }
            else {
                this.addEuclidDistanceToMap(playersByEuclidNoTreasure, playersPublicInfo.getCurrentCoord(),
                        playersPrivateInfo.getGoal(), p);
            }
        }
        if (!playersByEuclidFromHomeIfGotTreasure.isEmpty()) {
            return playersByEuclidFromHomeIfGotTreasure.firstEntry().getValue();
        }
        else if (!playersByEuclidNoTreasure.isEmpty()) {
            return playersByEuclidNoTreasure.firstEntry().getValue();
        }
        else {
            // to avoid indexOutOfBounds error if no players
            return new ArrayList<>();
        }
    }

    /**
     * Updates the given map to be ordered according to player's euclidean distance from goal
     * @param map the map to feed players into
     * @param current the current coordinate of a player
     * @param relativeGoal the goal (home/treasure) of a player based on if they already got treasure
     * @param indexOfPlayer the index of player whose current coord and goal is referring to
     */
    private void addEuclidDistanceToMap(TreeMap<Double, ArrayList<IPlayer>> map, Coordinate current,
                                        Coordinate relativeGoal, int indexOfPlayer) {
        Double distance = getDistance(current, relativeGoal);
        if (map.containsKey(distance)) {
            map.get(distance).add(this.players.get(indexOfPlayer));
        }
        else {
            ArrayList<IPlayer> playersOfDistance = new ArrayList<>();
            playersOfDistance.add(this.players.get(indexOfPlayer));
            map.put(distance, playersOfDistance);
        }
    }


    /**
     * Calculates the distance between two coordinates
     * @param goal the desired goal coordinate
     * @param candidates a coordinate of a tile that we want to calculate the distance to the goal from
     * @return a double that represents the distance between the two coordinates
     */
    protected static double getDistance(Coordinate goal, Coordinate candidates) {
        return Math.sqrt(Math.pow((candidates.getCol() - goal.getCol()), 2) +
                Math.pow((candidates.getRow() - goal.getRow()), 2));
    }

    /**
     * Kicks out the current player from this.state and moves them from this.players to this.kickedPlayers
     */
    private void kickOutPlayer() {
        this.kickOutPlayer(this.state.getIndexOfCurrentPlayer());
    }

    /**
     * Kicks out the player at the given index from this.state and moves them from this.players to this.kickedPlayers
     */
    private void kickOutPlayer(int index) {

        //get the index of the current player to kick them out
        //kick the player out of the state
        this.state = this.state.kickPlayerOut(index);

        System.out.println("KICKED OUT " + this.players.get(index).name());
        //add the newly kicked out player to the kickedPlayers list
        this.kickedPlayers.add(this.players.remove(index));
    }

    /**
     * Goals need to be on immovable tiles, which are at odd rows and columns. Validates that the given
     * list of Coordinates contains all valid goal tiles. Throws an IllegalArgumentException if any are invalid.
     * @param unusedGoals a list of Coordinates that represent goal candidates.
     */
    private void validateInputedGoals(ArrayList<Coordinate> unusedGoals) {
        for (Coordinate coord : unusedGoals) {
            if (coord.getRow() % 2 == 0 || coord.getCol() % 2 == 0) {
                throw new IllegalArgumentException("Invalid goals. Must be on immovable tiles.");
            }
        }
    }

}

