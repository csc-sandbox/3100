package edu.calpoly.csc3100.multiplayer;

import java.awt.*;

public class Player {
    private final String id;
    private int x, y;
    private final Color color;

    public Player(String id, int x, int y, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public String getId() { return id; }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - 9, y - 9, 18, 18);
        g.setColor(Color.BLACK);
        g.drawString(id, x - 10, y - 15);
    }
}
