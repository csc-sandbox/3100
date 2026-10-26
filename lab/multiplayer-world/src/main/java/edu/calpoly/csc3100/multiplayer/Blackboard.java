package edu.calpoly.csc3100.multiplayer;

import java.awt.Color;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Blackboard extends PropertyChangeSupport {
    public static final String BROKER = "tcp://broker.hivemq.com:1883";
    public static final String TOPIC = "csc3100/brokerverse/world";
    private static final Blackboard instance = new Blackboard();
    private final Map<String, Player> players = new LinkedHashMap<>();
    private Player me;

    private Blackboard() { super(new Object()); }
    public static Blackboard getInstance() { return instance; }
    public Collection<Player> getPlayers() { return players.values(); }
    public Player getMe() { return me; }

    public void setMe(Player player) {
        me = player;
        players.put(player.getId(), player);
        firePropertyChange("players", null, player);
    }

    // TODO 5: Split payload into name, x, y. Create or update a player,
    // then call firePropertyChange("players", null, player).
    public void addPlayerFromPayload(String payload) {
        // Implement in the lab.
    }

    public void up() { move(0, -5); }
    public void down() { move(0, 5); }
    public void left() { move(-5, 0); }
    public void right() { move(5, 0); }

    private void move(int dx, int dy) {
        if (me == null) return;
        me.setX(Math.max(10, Math.min(790, me.getX() + dx)));
        me.setY(Math.max(25, Math.min(590, me.getY() + dy)));
        firePropertyChange("me", null, me);
        firePropertyChange("players", null, me);
    }

    public static Color colorFor(String name) {
        return Color.getHSBColor((name.hashCode() & 0x7fffffff) / (float) Integer.MAX_VALUE, .75f, .8f);
    }
}
