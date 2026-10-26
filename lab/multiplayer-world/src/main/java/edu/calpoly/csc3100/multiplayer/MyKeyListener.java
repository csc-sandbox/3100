package edu.calpoly.csc3100.multiplayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) Blackboard.getInstance().up();
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) Blackboard.getInstance().down();
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) Blackboard.getInstance().left();
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) Blackboard.getInstance().right();
    }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
