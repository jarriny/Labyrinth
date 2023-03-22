package Referee;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.*;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.RefState;
import Players.player;

/**
 * Our implementation for an observer
 */
public class observer extends JFrame implements ActionListener, IObserver {
    private final JPanel GUI;
    private JPanel statePanel;
    private final JPanel buttonPanel;

    private final int WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getMaximumWindowBounds().width;
    private final int HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getMaximumWindowBounds().height;

    private final ArrayList<RefState> states = new ArrayList<>();
    private int currentStateIndex = -1;
    private Boolean gameOver = false;


    /**
     * Creating an instance of an observer
     */
    public observer() {

        GUI = new JPanel();
        GUI.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        GUI.setBackground(Color.LIGHT_GRAY);
        GUI.setLayout(new BoxLayout(GUI, BoxLayout.Y_AXIS));
        this.add(GUI);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new FlowLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setVisible(true);

        run();
    }

    public ArrayList<RefState> getStates() {
        return states;
    }

    public int getCurrentStateIndex() {
        return currentStateIndex;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    /**
     * Outputs the corresponding GUI and JSON representation
     * @param state the current referee game state
     */
    public void receiveState(RefState state){
        if(this.gameOver){
            throw new IllegalArgumentException("Game has ended, cannot add new states");
        }
        this.states.add(state);
    }

    /**
     *Called by the referee if the game is over
     */
    public void gameOver(){
        this.gameOver = true;

    }

    /**
     * creates a gui that represents the given state
     * @param state the current referee state
     */
    private void createGui(RefState state) {
        GUI.removeAll();
        buttonPanel.removeAll();

        if (state != null) {
            statePanel = new statePanel(state);
        }
        else {
            statePanel = new JPanel();
            JLabel label = new JLabel("Click Next to see first state of game when available");
            statePanel.add(label);
            statePanel.setBackground(Color.LIGHT_GRAY);
        }

        GUI.add(statePanel);

        // set up the main GUI
        JButton nextButton = new JButton("Next");
        JButton saveButton = new JButton("Save");

        buttonPanel.add(nextButton);
        buttonPanel.add(saveButton);
        nextButton.addActionListener(this);
        saveButton.addActionListener(this);
        GUI.add(buttonPanel);
    }

    /**
     * Overridden method for our buttons
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //for the buttons
        String action = e.getActionCommand();
        if(action.equals("Next")){
            performNext();
        }
        else if (action.equals("Save")) {
            performSave();
        }
    }

    /**
     * Helper method for our next button that will either move the gui to the next state, or print that
     * there are no new states available
     */
    private void performNext() {
        this.currentStateIndex++;
        if (this.currentStateIndex >= this.states.size()) {
            this.currentStateIndex--;
//            System.out.println("No new states available");
        }
        else {
            createGui(this.states.get(this.currentStateIndex));
            this.pack();
            this.setMinimumSize(this.getSize());
        }
    }

    /**
     * Helper method for our save button that allows a user to save a JSON representation of the current gui
     */
    private void performSave() {
        JFileChooser savefile = new JFileChooser(new File(System.getProperty("user.dir")));
        BufferedWriter writer;
        int sf = savefile.showSaveDialog(this);
        if(sf == JFileChooser.APPROVE_OPTION){
            try {
                File file = savefile.getSelectedFile();
                if (file.exists()) {
                    writer = new BufferedWriter(new FileWriter(file));
                }
                else {
                    writer = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(
                                    file.getAbsolutePath() + ".json")));
                }
                writer.write(this.getStateJSON(this.currentStateIndex));
                writer.close();
                JOptionPane.showMessageDialog(null, "File has been saved",
                        "File Saved",JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(sf == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "File save has been canceled");
        }
    }

    /**
     * Returns a String value of the JSON representation of the specified saved state
     * @param index of a saved state
     * @return a string
     */
    private String getStateJSON(int index) {
        return this.states.get(index).toJSON().toString();
    }

    /**
     * Begins to run our observer implementation
     */
    public void run() {
        this.createGui(null);
        this.pack();
    }

    /**
     * main method created for testing purposed
     * @param args
     */
    public static void main(String[] args) {
        observer ob = new observer();
        ob.receiveState(getStateForTesting());
        ob.receiveState(getStateForTesting2());
        ob.run();
    }

    /**
     * Used for testing purposes
     * @return referee state for testing
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
     * @return referee state for testing
     */
    private static RefState getStateForTesting2() {
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

        refState = new RefState(fortesting2(), spare, playerInfoPublics, playerInfoPrivates, 0, Optional.empty());
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

    /**
     * Used for testing purposes
     * @return an instance of a board for testing
     */
    private static Board fortesting2() {
        Tile[] listOfTiles = new Tile[]{
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.EMERALD, Gem.EMERALD)),
                new Tile(new boolean[]{true, false, false, true}, new GemPair(Gem.EMERALD, Gem.ALEXANDRITE)),
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

