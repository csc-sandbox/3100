package edu.calpoly.storage;

import edu.calpoly.provided.Broker;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * This class is responsible for retrieving messages from storage and sending them through a Broker.
 * 
 * @author Anay Nagar (ReeledWarrior14)
 * @version 2.0 (10/23/2026)
 */
public class RetrieveMessages implements MessageRetrievalInterface {
    private static final int DEFAULT_PORT = 5000;
    private static final String DEFAULT_HOST = "localhost";
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "messages.csv");

    protected List<String> messages = new ArrayList<>();

    public static void main(String[] args) {
        RetrieveMessages retriever = new RetrieveMessages();
        MessageRetrievalInterface diagnosticRetriever = new RetrieveMessagesDiagnostics(retriever);
        diagnosticRetriever.readFile(DEFAULT_DATA_FILE);

        if (retriever.messages.isEmpty()) {
            System.err.println("No messages to retrieve.");
            return;
        }

        Broker broker = new Broker(DEFAULT_HOST, DEFAULT_PORT);
        diagnosticRetriever.sendMessages(broker);
    }

    public void readFile(Path dataFile) {
        try {
            messages = readMessages(dataFile);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void sendMessages(Broker broker) {
        try {
            for (String message : messages) {
                broker.send(message);
            }
        } catch (Exception e) {
            System.err.println("Error sending messages: " + e.getMessage());
        }
    }

    public List<String> readMessages(Path file) throws IOException {

        List<String> result = new ArrayList<>();

        if (!Files.exists(file)) {
            return result;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                file, StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            for (CSVRecord record : records) {
                if (record.size() >= 2) {
                    result.add(record.get(1));
                }
            }
        }

        return result;
    }
}
