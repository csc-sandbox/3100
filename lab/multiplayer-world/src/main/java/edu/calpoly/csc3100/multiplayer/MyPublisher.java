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
        client.connect();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"me".equals(evt.getPropertyName())) return;
        Player player = Blackboard.getInstance().getMe();
        if (player == null || (player.getX() == lastX && player.getY() == lastY)) return;
        String payload = player.getId() + ":" + player.getX() + ":" + player.getY();
            try {
            MqttMessage message =
                    new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));

            message.setRetained(false);
            client.publish(Blackboard.TOPIC, message);

            lastX = player.getX();
            lastY = player.getY();
        } catch (MqttException exception) {
            System.err.println(
                    "Could not publish position: " + exception.getMessage());
        }
    }

    public void publishInitialPosition() {
        propertyChange(new PropertyChangeEvent(Blackboard.getInstance(), "me", null, Blackboard.getInstance().getMe()));
    }
}
