package edu.calpoly.messages;

import static org.junit.jupiter.api.Assertions.*;

import edu.calpoly.provided.Encryption;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.junit.jupiter.api.Test;

/**
 * Tests message encryption, processing, decoration, and diagnostics.
 *
 * @author Anthony Soto
 * @version 1.0
 */
class MessageSecurityTest {

    @Test
    void messageDigestReturnsExpectedSha256Value() {
        assertEquals(
                "88a956fd147e7fd81b6e4f23dbd9726542ccd9ca31c37d88344ba02213adf52d",
                EncryptMessage.messageDigest("CSC 3100"));
    }

    @Test
    void encryptMessageEncryptsAndDisplaysDigestThenExits() {
        String message = "ROBOT,1,2,3";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        InputStream originalInput = System.in;
        PrintStream testOutput = new PrintStream(output, true, StandardCharsets.UTF_8);

        try {
            System.setIn(new ByteArrayInputStream(
                    (message + System.lineSeparator() + "exit" + System.lineSeparator())
                            .getBytes(StandardCharsets.UTF_8)));
            System.setOut(testOutput);

            EncryptMessage.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
            testOutput.close();
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Encrypted message: "));
        assertTrue(consoleOutput.contains(EncryptMessage.messageDigest(message)));
        assertTrue(consoleOutput.contains("Exiting the program."));
        assertFalse(consoleOutput.contains("Encrypted message: " + message));
    }

    @Test
    void encryptionProcessorEncryptsMessagesThatCanBeDecrypted() {
        String message = "ROBOT,1,2,3";
        MessageSecurityProcessor processor = new EncryptionMessageSecurity();

        String encryptedMessage = processor.process(message);

        assertNotEquals(message, encryptedMessage);
        assertEquals(message, Encryption.decrypt(encryptedMessage));
    }

    @Test
    void plainAndDiagnosticProcessorsPreserveEncryptionSemanticsButOnlyDiagnosticsEmitEvent() {
        String message = "ROBOT,1,2,3";
        List<String> diagnosticEvents = new ArrayList<>();
        MessageSecurityProcessor plainProcessor =
                EncryptMessage.createSecurityProcessor(false, diagnosticEvents::add);
        MessageSecurityProcessor diagnosticProcessor =
                EncryptMessage.createSecurityProcessor(true, diagnosticEvents::add);

        String plainEncryptedMessage = plainProcessor.process(message);
        assertNotEquals(message, plainEncryptedMessage);
        assertEquals(message, Encryption.decrypt(plainEncryptedMessage));
        assertTrue(diagnosticEvents.isEmpty());

        String diagnosticEncryptedMessage = diagnosticProcessor.process(message);
        assertNotEquals(message, diagnosticEncryptedMessage);
        assertEquals(message, Encryption.decrypt(diagnosticEncryptedMessage));
        assertEquals(
                List.of("Message security processing completed."),
                diagnosticEvents);
    }

    @Test
    void messageSecurityDecoratorForwardsProcessingToItsDelegate() {
        MessageSecurityProcessor delegate = message -> "secured:" + message;
        MessageSecurityDecorator decorator = new MessageSecurityDecorator(delegate) { };

        assertEquals("secured:hello", decorator.process("hello"));
    }

    @Test
    void diagnosticsDecoratorEmitsCompletionEventWithoutMessageContent() {
        String message = "sensitive message";
        List<String> diagnosticEvents = new ArrayList<>();
        MessageSecurityProcessor delegate = value -> "secured:" + value;
        MessageSecurityProcessor decorator =
                new DiagnosticsMessageSecurityDecorator(delegate, diagnosticEvents::add);

        assertEquals("secured:" + message, decorator.process(message));
        assertEquals(1, diagnosticEvents.size());
        assertEquals("Message security processing completed.", diagnosticEvents.get(0));
        assertFalse(diagnosticEvents.get(0).contains(message));
    }

    @Test
    void diagnosticsDecoratorDoesNotEmitCompletionWhenDelegateFails() {
        List<String> diagnosticEvents = new ArrayList<>();
        MessageSecurityProcessor failingDelegate = message -> {
            throw new IllegalStateException("processing failed");
        };
        MessageSecurityProcessor decorator =
                new DiagnosticsMessageSecurityDecorator(failingDelegate, diagnosticEvents::add);

        assertThrows(IllegalStateException.class, () -> decorator.process("hello"));
        assertTrue(diagnosticEvents.isEmpty());
    }
}
