package edu.calpoly.csc3100.multiplayer;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class Main extends JFrame {
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
        String name = args.length > 0 ? args[0] : JOptionPane.showInputDialog(null, "Your player name:");
        if (name == null || !name.matches("[A-Za-z0-9_-]{1,24}")) {
            System.err.println("Use a name of 1–24 letters, digits, underscores, or hyphens.");
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
