package edu.calpoly.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.calpoly.provided.Broker;

/**
 * Defines the operations for retrieving and sending stored messages.
 *
 * @author Anay Nagar (ReeledWarrior14)
 * @version 1.0 (10/23/2026)
 */
public interface MessageRetrievalInterface {
    public void readFile(Path dataFile);
    public void sendMessages(Broker broker);
    public List<String> readMessages(Path dataFile) throws IOException;
}
