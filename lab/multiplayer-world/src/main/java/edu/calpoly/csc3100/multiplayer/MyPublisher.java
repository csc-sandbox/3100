package edu.calpoly.csc3100.multiplayer;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyPublisher implements PropertyChangeListener {
    private final MqttClient client;
    private int lastX = -1, lastY = -1;

    public MyPublisher() throws MqttException {
        client = new MqttClient(Blackboard.BROKER, "brokerverse-pub-" + UUID.randomUUID(), new MemoryPersistence());
        // TODO 1: Connect the MQTT client to the broker.
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"me".equals(evt.getPropertyName())) return;
        Player player = Blackboard.getInstance().getMe();
        if (player == null || (player.getX() == lastX && player.getY() == lastY)) return;
        String payload = player.getId() + ":" + player.getX() + ":" + player.getY();
        // TODO 2: Publish payload to Blackboard.TOPIC using UTF-8.
        // Update lastX and lastY only after publishing successfully.
    }

    public void publishInitialPosition() {
        // TODO 3: Publish the local player's initial position.
        // Hint: reuse propertyChange with a "me" PropertyChangeEvent.
    }
}
