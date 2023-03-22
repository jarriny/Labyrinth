package Referee;

import Common.PlayerInfo.GameColors;

import javax.swing.*;
import java.awt.*;

/**
 * Panel of a player's avatar (a circle) for the GUI
 */
public class currentPanel extends JPanel {
    GameColors color;
    int radius;
    int midOfPanel;

    public currentPanel(GameColors color, Dimension dims) {
        this.color = color;
        this.setPreferredSize(dims);
        midOfPanel = dims.height / 2;
        radius =  dims.height / 5;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(color.getColor());
        int c = (int) (midOfPanel - (radius / 1.7));
        g2d.fillOval(c, c, radius, radius);

    }
}
