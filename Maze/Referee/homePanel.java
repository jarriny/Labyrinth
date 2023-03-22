package Referee;

import Common.PlayerInfo.GameColors;

import javax.swing.*;
import java.awt.*;

/**
 * Panel of a player's home for the GUI
 */
public class homePanel extends JPanel {
    private final Polygon poly;
    GameColors color;


    public homePanel(GameColors color, Dimension dims) {
        this.color = color;
        int size = dims.height / 2;
        setOpaque(false);

        poly = new Polygon(
                new int[]{size / 2, size / 4, size * 3 / 4},
                new int[]{size / 4,  size * 3 / 4,  size * 3 / 4},
                3);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(color.getColor());
        g2d.fill(poly);
    }
}
