package edu.calpoly.csc3100.multiplayer;

import javax.swing.*;

public class Main extends JFrame {
    // TODO 0: Replace YOUR_ID with your unique student ID (or instructor name).
    private static final String PLAYER_NAME = "YOUR_ID";

    public Main() {
        MyPanel panel = new MyPanel();
        add(panel);
        addKeyListener(new MyKeyListener());
        Blackboard.getInstance().addPropertyChangeListener(panel);
        setTitle("Brokerverse Swing Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocusInWindow();
    }

    public static void main(String[] args) throws Exception {
        String name = PLAYER_NAME;
        if (name == null || !name.matches("[A-Za-z0-9_-]{1,24}")) {
            System.err.println("Set PLAYER_NAME in Main.java to 1–24 letters, digits, underscores, or hyphens.");
            return;
        }
        SwingUtilities.invokeAndWait(Main::new);
        Blackboard blackboard = Blackboard.getInstance();
        SwingUtilities.invokeAndWait(() -> blackboard.setMe(
            new Player(name, 400, 300, Blackboard.colorFor(name))));
        MySubscriber subscriber = new MySubscriber();
        MyPublisher publisher = new MyPublisher();
        blackboard.addPropertyChangeListener(publisher);
        publisher.publishInitialPosition();
    }
}
