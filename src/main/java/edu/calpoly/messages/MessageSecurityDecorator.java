package edu.calpoly.messages;

import java.util.Objects;

/**
 * Base decorator that forwards message security processing to a wrapped processor.
 *
 * @author Anthony Soto
 * @version 1.0
 */
public abstract class MessageSecurityDecorator implements MessageSecurityProcessor {
    private final MessageSecurityProcessor delegate;


    protected MessageSecurityDecorator(MessageSecurityProcessor delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate cannot be null");
    }

   // Forwards processing to the wrapped processor.
  
    @Override
    public String process(String message) {
        return delegate.process(message);
    }
}
