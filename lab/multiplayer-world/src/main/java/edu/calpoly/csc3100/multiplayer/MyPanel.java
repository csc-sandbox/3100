package edu.calpoly.csc3100.multiplayer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MyPanel extends JPanel implements PropertyChangeListener {
    public MyPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(185, 245, 205));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Player player : Blackboard.getInstance().getPlayers()) {
            player.draw(g);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("players".equals(event.getPropertyName())) repaint();
    }
}
