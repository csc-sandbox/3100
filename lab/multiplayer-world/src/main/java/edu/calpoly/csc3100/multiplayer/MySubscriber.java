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
        client.connect();
        client.subscribe("csc3100/brokerverse/#");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (!Blackboard.TOPIC.equals(topic)) return;

        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);


        System.out.println("Subscriber received: " + payload);

        SwingUtilities.invokeLater(() -> Blackboard.getInstance().addPlayerFromPayload(payload));

        
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {}
    @Override public void connectionLost(Throwable cause) {
        System.err.println("Subscriber disconnected: " + cause);
    }
}
