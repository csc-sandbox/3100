package edu.calpoly.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.calpoly.provided.Broker;

/**
 * Decorator that logs message retrieval operations and their elapsed time.
 *
 * @author Anay Nagar (ReeledWarrior14)
 * @version 1.0 (10/23/2026)
 */
public class RetrieveMessagesDiagnostics extends MessageRetrievalDecorator {

    public RetrieveMessagesDiagnostics(MessageRetrievalInterface messageRetriever) {
        super(messageRetriever);
    }

    @Override
    public void readFile(Path dataFile) {
        long start = System.nanoTime();
        System.out.println("Reading file: " + dataFile);
        try {
            super.readFile(dataFile);
        } finally {
            System.out.println("Finished reading file in " + elapsedMillis(start) + " ms.");
        }
    }

    @Override
    public void sendMessages(Broker broker) {
        long start = System.nanoTime();
        System.out.println("Sending messages through broker: " + broker);
        try {
            super.sendMessages(broker);
        } finally {
            System.out.println("Finished sending messages in " + elapsedMillis(start) + " ms.");
        }
    }

    @Override
    public List<String> readMessages(Path dataFile) throws IOException {
        long start = System.nanoTime();
        try {
            return super.readMessages(dataFile);
        } finally {
            System.out.println("Read messages in " + elapsedMillis(start) + " ms.");
        }
    }

    private long elapsedMillis(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
