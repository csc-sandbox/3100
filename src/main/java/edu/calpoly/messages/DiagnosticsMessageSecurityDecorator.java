package edu.calpoly.messages;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Adds a non-sensitive completion event to message security processing.
 *
 * @author Anthony Soto
 * @version 1.0
 */
public final class DiagnosticsMessageSecurityDecorator extends MessageSecurityDecorator {
    private static final String COMPLETION_EVENT = "Message security processing completed.";

    private final Consumer<String> diagnosticSink;

    public DiagnosticsMessageSecurityDecorator(
            MessageSecurityProcessor delegate,
            Consumer<String> diagnosticSink) {
        super(delegate);
        this.diagnosticSink =
                Objects.requireNonNull(diagnosticSink, "diagnosticSink cannot be null");
    }

    // Processes a message, then emits a completion event without including message content.
  
    @Override
    public String process(String message) {
        String secureMessage = super.process(message);
        diagnosticSink.accept(COMPLETION_EVENT);
        return secureMessage;
    }
}
