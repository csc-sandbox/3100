package edu.calpoly.test.sprint2;

import edu.calpoly.messages.StoreMessages;
import edu.calpoly.storage.RetrieveMessages;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Sprint 2 test program for Apache Commons CSV integration.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class TestMessageCSV {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("Sprint 2 - Commons CSV Test");

        testProvidedFile();
        testWriteAndReadComma();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Commons CSV tests failed.");
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
            fail("write/read test: " + e.getMessage());
        } finally {
            if (file != null) {
                try {
                    Files.deleteIfExists(file);
                } catch (Exception ignored) {
                    // Temporary test file cleanup only.
                }
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
