package edu.calpoly.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.calpoly.provided.Broker;

/**
 * Base decorator that forwards message retrieval operations to another component.
 *
 * @author Anay Nagar (ReeledWarrior14)
 * @version 1.0 (10/23/2026)
 */
public abstract class MessageRetrievalDecorator implements MessageRetrievalInterface {
    protected MessageRetrievalInterface messageRetriever;

    public MessageRetrievalDecorator(MessageRetrievalInterface messageRetriever) {
        this.messageRetriever = messageRetriever;
    }

    @Override
    public void readFile(Path dataFile) {
        messageRetriever.readFile(dataFile);
    }

    @Override
    public void sendMessages(Broker broker) {
        messageRetriever.sendMessages(broker);
    }

    @Override
    public List<String> readMessages(Path dataFile) throws IOException {
        return messageRetriever.readMessages(dataFile);
    }
}
