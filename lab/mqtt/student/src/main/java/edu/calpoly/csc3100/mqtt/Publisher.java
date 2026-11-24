package edu.calpoly.csc3100.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** Publishes READY and ACK messages to the instructor. Complete the TODOs. */
public final class Publisher {
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static final int QOS = 1;
    private final String prefix;
    private final String studentId;
    private MqttClient client;

    public Publisher(String prefix, String studentId) {
        this.prefix = prefix;
        this.studentId = studentId;
    }

    public void connect() throws MqttException {
        // TODO 1: Create an MqttClient with a unique ID (UUID) and MemoryPersistence.
        // TODO 2: Connect with a clean session.
	String clientId = UUID.randomUUID().toString();
	client = new MqttClient(BROKER, clientId, new MemoryPersistence());

	MqttConnectOptions options = new MqttConnectOptions();
	options.setCleanSession(true);
	client.connect(options);
    }

    public void sendReady() throws MqttException {
        String message = studentId + ":READY";
        // TODO 3: Publish UTF-8 message to prefix + "ready", QoS 1, non-retained.
        // Hint: client.publish(topic, mqttMessage);
	MqttMessage mqttMessage = new MqttMessage(
		message.getBytes(StandardCharsets.UTF_8)
	);
	mqttMessage.setQos(QOS);
	mqttMessage.setRetained(false);

	client.publish(prefix + "ready", mqttMessage);
    }

    public void sendAck(String key) throws MqttException {
        String message = studentId + ":ACK:" + key;
        // TODO 4: Publish UTF-8 message to prefix + "ack", QoS 1, non-retained.
	MqttMessage mqttMessage = new MqttMessage(
		message.getBytes(StandardCharsets.UTF_8)
	);
	mqttMessage.setQos(QOS);
	mqttMessage.setRetained(false);

	client.publish(prefix + "ack", mqttMessage);
    }
}
