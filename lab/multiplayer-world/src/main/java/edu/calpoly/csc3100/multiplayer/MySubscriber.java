package edu.calpoly.csc3100.multiplayer;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import javax.swing.SwingUtilities;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MySubscriber implements MqttCallback {
    private final MqttClient client;

    public MySubscriber() throws MqttException {
        client = new MqttClient(Blackboard.BROKER, "brokerverse-sub-" + UUID.randomUUID(), new MemoryPersistence());
        client.setCallback(this);
        // TODO 4: Connect and subscribe to "csc3100/brokerverse/#" using the MQTT wildcard.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // TODO 6: Ignore messages not on Blackboard.TOPIC.
        // Decode UTF-8 payload and pass it to
        // Blackboard.getInstance().addPlayerFromPayload(payload).
        // Schedule the Blackboard update on Swing's event-dispatch thread:
        // SwingUtilities.invokeLater(() -> ...);
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {}
    @Override public void connectionLost(Throwable cause) {
        System.err.println("Subscriber disconnected: " + cause);
    }
}
