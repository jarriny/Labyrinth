package Referee;

import Common.PlayerInfo.GameColors;

import javax.swing.*;
import java.awt.*;

/**
 * Panel of a player's home for the GUI
 */
public class goalPanel extends JPanel {
    private final Polygon poly;
    GameColors color;


    public goalPanel(GameColors color, Dimension dims) {
        this.color = color;
        setOpaque(false);
        this.setPreferredSize(dims);
        int size = dims.height / 4;
        if (size % 2 != 0) {
            size++;
        }

        int mid1 = (int)(size / 1.625);
        int mid2 = (int)(size / 2.6);
        int half = size / 2;

        poly = new Polygon(
                new int[]{half, mid2, 0, mid2, half, mid1, size, mid1},
                new int[]{0, mid2, half, mid1, size, mid1, half, mid2},
                8);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(color.getColor());
        g2d.fill(poly);
    }
}
