package Referee;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.RefState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Our implementation of a statePanel that extends JPanel
 */
public class statePanel extends JPanel {
    private final static int TILE_SIZE = 100;
    private final static int BORDER_SIZE = 2;
    private final static Dimension DIMS = new Dimension(TILE_SIZE, TILE_SIZE);
    ArrayList<Coordinate> homes;
    ArrayList<Coordinate> currents;
    ArrayList<Coordinate> goals;
    ArrayList<GameColors> colors;
    Tile[][] tilesToVisualize;


    /**
     * Create a new instance of a statePanel
     * @param s a referee state that will be created into a panel
     */
    public statePanel(RefState s) {
        Board b = s.getBoardState();
        int rows = b.getRowCount();
        int cols = b.getColCount();
        tilesToVisualize = b.getBoardRep();

        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel board = new JPanel();
        board.setLayout(new GridLayout(rows, cols));

        this.setPlayerInfos(s);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLayeredPane tileWPossibleHomeAndPlayer = this.makeCompleteTile(r,c);
                board.add(tileWPossibleHomeAndPlayer);
            }
        }

        this.add(board);

        tilePanel spare = new tilePanel(s.getSpareTile(), DIMS);
        this.add(spare);
    }

    /**
     * Creates a representation of a tile as a JLayeredPane
     * @param r an int for the number of rows
     * @param c an int for the number of columns
     * @return an instance of JLayeredPane that represents a tile
     */
    private JLayeredPane makeCompleteTile(int r, int c) {
        JLayeredPane tileWPossibleHomeAndPlayer = new JLayeredPane();
        tileWPossibleHomeAndPlayer.setPreferredSize(DIMS);
        JLayeredPane tile = new tilePanel(tilesToVisualize[r][c], DIMS);

        Coordinate coord = new Coordinate(r,c);

        if (currents.contains(coord)) {
            currentPanel currentPanel = new currentPanel(colors.get(currents.indexOf(coord)), DIMS);
            currentPanel.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
            tileWPossibleHomeAndPlayer.add(currentPanel);
        }
        if (homes.contains(coord)) {
            homePanel homePanel = new homePanel(colors.get(homes.indexOf(coord)), DIMS);
            homePanel.setBounds(0, TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            tileWPossibleHomeAndPlayer.add(homePanel);
        }
        if (goals.contains(coord)) {
            goalPanel goalPanel = new goalPanel(colors.get(goals.indexOf(coord)), DIMS);
            goalPanel.setBounds(TILE_SIZE * 3 / 4, 0, TILE_SIZE - BORDER_SIZE, TILE_SIZE - BORDER_SIZE);
            tileWPossibleHomeAndPlayer.add(goalPanel);
        }

        tile.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
        tileWPossibleHomeAndPlayer.add(tile);
        return tileWPossibleHomeAndPlayer;
    }

    /**
     * Creates player information from information in the given state
     * @param s a referee state
     */
    private void setPlayerInfos(RefState s) {
        homes = new ArrayList<>();
        currents = new ArrayList<>();
        goals = new ArrayList<>();
        colors = new ArrayList<>();

        for (PlayerInfoPublic playerInfoPublic : s.getPublicPlayerInfos()) {
            homes.add(playerInfoPublic.getHome());
            currents.add(playerInfoPublic.getCurrentCoord());
            colors.add(playerInfoPublic.getColor());
        }

        for (PlayerInfoPrivate playerInfoPrivate : s.getPrivatePlayerInfos()) {
            goals.add(playerInfoPrivate.getGoal());
        }
    }


    /**
     * Used for testing purposes
     * @param args
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Board");
        f.setSize(new Dimension((TILE_SIZE + BORDER_SIZE) * 7, (TILE_SIZE + BORDER_SIZE)  * 7));
        f.setResizable(false);
        statePanel board = new statePanel(getStateForTesting());
        f.add(board);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    /**
     * Used for testing purposes
     * @return an instance of a referee state for testing
     */
    private static RefState getStateForTesting() {
        Tile[] listOfTiles;
        Board board;
        Tile spare;
        ArrayList<PlayerInfoPublic> playerInfoPublics;
        ArrayList<PlayerInfoPrivate> playerInfoPrivates;
        RefState refState;

        spare = new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE_OVAL));

        playerInfoPublics = new ArrayList<>();

        playerInfoPublics.add(new PlayerInfoPublic(new Coordinate(1,1), new Coordinate(1,1), new GameColors("black")));
        playerInfoPublics.add(new PlayerInfoPublic(new Coordinate(3,1), new Coordinate(3,1), new GameColors("red")));
        playerInfoPublics.add(new PlayerInfoPublic(new Coordinate(5,1), new Coordinate(5,1), new GameColors("blue")));

        playerInfoPrivates = new ArrayList<>();

        playerInfoPrivates.add(new PlayerInfoPrivate(new Coordinate(5,5)));
        playerInfoPrivates.add(new PlayerInfoPrivate(new Coordinate(3,5)));
        playerInfoPrivates.add(new PlayerInfoPrivate(new Coordinate(1,5)));

        refState = new RefState(fortesting(), spare, playerInfoPublics, playerInfoPrivates, 0, Optional.empty());
        return refState;
    }

    /**
     * Used for testing purposes
     * @return an instance of a board for testing
     */
    private static Board fortesting() {
        Tile[] listOfTiles = new Tile[]{
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.EMERALD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALEXANDRITE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.APLITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.APATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AZURITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.ALMANDINE_GARNET)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETHYST)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMETRINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AMMOLITE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.APRICOT_SQUARE_RADIANT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AQUAMARINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AUSTRALIAN_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.AVENTURINE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CUSHION)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_PEAR_SHAPE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BERYL)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.BULLS_EYE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_ONYX)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_OBSIDIAN)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.BLACK_SPINEL_CUSHION)),
                // row
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_CEYLON_SAPPHIRE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.BLUE_SPINEL_HEART)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CITRINE)),
                new Tile(new boolean[]{true, true, true, true}, new GemPair(Gem.DIAMOND, Gem.CARNELIAN)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CHRYSOLITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CLINOHUMITE)),
                new Tile(new boolean[]{false, true, false, true}, new GemPair(Gem.DIAMOND, Gem.CHROME_DIOPSIDE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.CORDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.CITRINE_CHECKERBOARD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.COLOR_CHANGE_OVAL)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.DUMORTIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.FANCY_SPINEL_MARQUISE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GARNET)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GOLDSTONE)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GRANDIDIERITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GRAY_AGATE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_BERYL)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.GOLDEN_DIAMOND_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_AVENTURINE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GREEN_BERYL_ANTIQUE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.GROSSULAR_GARNET)),
                // row
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HEMATITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HACKMANITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.HELIOTROPE)),
                new Tile(new boolean[]{true, false, true, false}, new GemPair(Gem.DIAMOND, Gem.IOLITE_EMERALD_CUT)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPER)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.JASPILITE)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.DIAMOND, Gem.KUNZITE)),
        };
        return new Board(7, 7, listOfTiles);
    }
}
