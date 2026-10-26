# CSC 3100 — Multiplayer World (MQTT Lab)

## Objective

Complete the MQTT communication for a multiplayer world. Every student runs the **same** application; the instructor runs it too. Arrow keys move your circle. Each circle displays its player's name, and MQTT broadcasts positions to the class.

The GUI, keyboard handling, player drawing, and application startup are provided. **Do not rewrite the GUI.** Preserve the lecture architecture, especially `Blackboard extends PropertyChangeSupport`, `MyPublisher implements PropertyChangeListener`, and `MySubscriber implements MqttCallback`.


## Protocol

- Broker: `tcp://broker.hivemq.com:1883`
- Publish topic: `csc3100/brokerverse/world`
- Subscribe with `#` (all broker-accessible topics); **filter** received messages to the publish topic.
- Message: `NAME:X:Y` — for example, `Alice:120:200`.
- Use UTF-8. Use a distinct MQTT client ID for each publisher and subscriber.
- No JOIN, LEAVE, heartbeat, or timeout. A disconnected player remains at the last received position.
- Players joining late only see previously connected players **after those players move again**. No retained messages.

**Use a unique player name.** This starter uses the name as the player key; duplicate names will refer to the same circle. This is a public broker: do not send personal or confidential information.

## Tasks (50 minutes)

### 1. Connect 

In `MyPublisher.java`, complete **TODO 1** using `client.connect()`. In `MySubscriber.java`, complete **TODO 4**: connect, then subscribe to `"#"`.

**Checkpoint:** Two application instances start without an MQTT exception.

### 2. Publish 

In `MyPublisher.propertyChange`, complete **TODO 2**. Convert `payload` to UTF-8 bytes, construct an `MqttMessage`, and publish to `Blackboard.TOPIC`. Update `lastX` and `lastY` after success.

Complete **TODO 3** so the initial position is also published. The existing `"me"` property-change event is the trigger for movement.

**Checkpoint:** Pressing an arrow key publishes `NAME:X:Y`.

### 3. Receive 

In `MySubscriber.messageArrived`, complete **TODO 6**. Filter by topic, decode UTF-8, and schedule `addPlayerFromPayload(payload)` on the Swing event-dispatch thread using `SwingUtilities.invokeLater`.

**Checkpoint:** Print a received message temporarily to verify the subscription.

### 4. Update the world 

In `Blackboard.addPlayerFromPayload`, complete **TODO 5**:

1. Split the message at `:` into three fields.
2. Parse the x and y coordinates (ignore malformed messages).
3. Find the player by name; create it if absent, otherwise update its coordinates.
4. Call `firePropertyChange("players", null, player)` so the existing Swing panel repaints.

Do not replace `PropertyChangeSupport` inheritance with a separate field.

**Checkpoint:** Two application instances display both players and synchronize movements.


## Submission

Demonstrate your working application in class. Submit a screenshot showing at least two named circles, plus the completed `MyPublisher.java`, `MySubscriber.java`, and `Blackboard.java` files as requested by your instructor.
