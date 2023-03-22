package Referee;

import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Our implementation of a tilePanel that extends JLayeredPanel
 */
public class tilePanel extends JLayeredPane {
    private final static int TILE_SIZE = 100;
    private final static int TILE_GRID_COUNT = 5;
    private final static int PATH_THICKNESS = TILE_SIZE / TILE_GRID_COUNT;
    private final static int GEM_SIZE = 20;
    private final static int BORDER_SIZE = 2;
    private final static int SPACE_FOR_GEMS = 5;
    private final static Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
    private final static Color PATH_COLOR = Color.GREEN;

    Tile tile;
    JPanel tilePath;

    /**
     * Create a new instance of a tilePanel from the given tile and dimensions
     * @param tile an instance of our tile class
     * @param dims dimensions corresponding to the given tile
     */
    public tilePanel(Tile tile, Dimension dims) {
        this.tile = tile;
        this.setPreferredSize(dims);
        this.setMinimumSize(dims);

        tilePath = new JPanel();
        tilePath.setPreferredSize(dims);
        tilePath.setMinimumSize(dims);
        tilePath.setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_SIZE));
        tilePath.setLayout(new GridLayout(TILE_GRID_COUNT, TILE_GRID_COUNT));

        this.setTile();
    }

    /**
     * Creates a complete tilePanel will all the needed attributes
     */
    private void setTile() {
        GemPair gems = tile.getGems();

        JLabel gem1 = this.getGemPic(gems.getFirstGem());
        JLabel gem2 = this.getGemPic(gems.getSecondGem());

        gem1.setBounds(SPACE_FOR_GEMS,SPACE_FOR_GEMS,GEM_SIZE,GEM_SIZE);
        this.add(gem1);
        int bufferForGem2 = TILE_SIZE - GEM_SIZE - SPACE_FOR_GEMS;
        gem2.setBounds(bufferForGem2,bufferForGem2,GEM_SIZE,GEM_SIZE);
        this.add(gem2);

        this.addPathToTilePath();
        tilePath.setBounds(0,0,TILE_SIZE,TILE_SIZE);
        this.add(tilePath);

    }

    /**
     * Draws a path on the panel
     */
    private void addPathToTilePath() {

        ArrayList<Integer> listOfPaths = getGridPositionsOfPath(tile.getShape());

        for (int i = 0; i < TILE_GRID_COUNT * TILE_GRID_COUNT; i++) {
            JPanel path = new JPanel();
            if (listOfPaths.contains(i)) {
                path.setOpaque(true);
                path.setSize(new Dimension(PATH_THICKNESS, PATH_THICKNESS));
                path.setBackground(PATH_COLOR);
            }
            else {
                path.setBackground(BACKGROUND_COLOR);
            }
            tilePath.add(path);
        }
    }

    /**
     * Matches the given gem to its JLabel
     * @param g an instance of our Gem Enum
     * @returna corresponding JLabel that represents the given gem
     */
    private JLabel getGemPic(Gem g) {
        BufferedImage gemBufImage;
        try {
            gemBufImage = ImageIO.read(new File("Maze/Common/Board/gems/" + g.getGemFile()));
        } catch (IOException e) {
            throw new IllegalArgumentException("somehow gem does not exist" + g.getGemString());

        }

        Image gemPic = gemBufImage.getScaledInstance(GEM_SIZE, -1, java.awt.Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(gemPic));
    }

    /**
     * Finds the grid positions of a path from a given shape
     * @param shape a shape represented by its boolean array
     * @return an ArrayList<Integer> that represents the grid positions on the path
     */
    private ArrayList<Integer> getGridPositionsOfPath(boolean[] shape) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < shape.length; i++) {
            if (shape[i]) {
                switch (i) {
                    case 0: {
                        list.add(2);
                        list.add(7);
                        list.add(12);
                        break;
                    }
                    case 1: {
                        list.add(12);
                        list.add(13);
                        list.add(14);
                        break;
                    }
                    case 2: {
                        list.add(12);
                        list.add(17);
                        list.add(22);
                        break;
                    }
                    case 3: {
                        list.add(10);
                        list.add(11);
                        list.add(12);
                        break;
                    }
                }
            }
        }
        return list;
    }
}
