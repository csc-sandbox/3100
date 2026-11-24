package edu.calpoly.test.sprint3;

import edu.calpoly.messages.StoreMessages;
import edu.calpoly.storage.MessageRetrievalInterface;
import edu.calpoly.storage.RetrieveMessages;
import edu.calpoly.storage.RetrieveMessagesDiagnostics;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Test program for CSV retrieval and the diagnostics Decorator.
 *
 * @author Anay Nagar (ReeledWarrior14)
 * @version 1.0 (10/23/2026)
 */
public class RetrieveMessagesDiagnosticsTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("Storage Decorator Test");

        testProvidedFile();
        testWriteAndReadComma();
        testDiagnosticsDecorator();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Storage Decorator tests failed.");
        }
    }

    private static void testProvidedFile() {
        try {
            Path file = Path.of("src", "test", "resources", "sprint2", "messages-test.csv");
            List<String> messages = new RetrieveMessages().readMessages(file);

            check(messages.size() == 3,
                    "provided file contains three messages");
            check(messages.contains("Hello"),
                    "reads a simple message");
            check(messages.contains("Robot position: 1,2,3"),
                    "reads a quoted message containing commas");
            check(messages.contains("Status, normal"),
                    "preserves comma inside message");
        } catch (Exception e) {
            fail("provided-file test: " + e.getMessage());
        }
    }

    private static void testWriteAndReadComma() {
        Path file = null;
        try {
            file = Files.createTempFile("csc3100-csv-", ".csv");

            StoreMessages.writeRecord(
                    file,
                    "2026-09-25T08:10:00Z",
                    "GAZE,0.42,0.71");

            List<String> messages = new RetrieveMessages().readMessages(file);

            check(messages.size() == 1,
                    "writes one CSV record");
            check("GAZE,0.42,0.71".equals(messages.get(0)),
                    "write/read round trip preserves commas");
        } catch (Exception e) {
            fail("read test: " + e.getMessage());
        } finally {
            delete(file);
        }
    }

    private static void testDiagnosticsDecorator() {
        Path file = null;
        PrintStream originalOut = System.out;
        try {
            file = Files.createTempFile("csc3100-diagnostics-", ".csv");
            Files.writeString(file,
                    "2026-10-23T08:00:00Z,Hello\n",
                    StandardCharsets.UTF_8);

            RetrieveMessages retriever = new RetrieveMessages();
            MessageRetrievalInterface decorated = new RetrieveMessagesDiagnostics(retriever);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            List<String> messages = decorated.readMessages(file);
            decorated.readFile(file);
            System.setOut(originalOut);

            String log = output.toString(StandardCharsets.UTF_8);
            check(messages.equals(List.of("Hello")),
                    "Decorator preserves retrieved messages");
            check(log.contains("Read messages in "),
                    "Decorator logs read timing");
            check(log.contains("Finished reading file in "),
                    "Decorator logs file-read timing");
        } catch (Exception e) {
            System.setOut(originalOut);
            fail("Decorator test: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
            delete(file);
        }
    }

    private static void delete(Path file) {
        if (file != null) {
            try {
                Files.deleteIfExists(file);
            } catch (Exception ignored) {
                // Temporary test file cleanup only.
            }
        }
    }

    private static void check(boolean condition, String message) {
        if (condition) {
            pass(message);
        } else {
            fail(message);
        }
    }

    private static void pass(String message) {
        passed++;
        System.out.println("[PASS] " + message);
    }

    private static void fail(String message) {
        failed++;
        System.out.println("[FAIL] " + message);
    }
}
