package edu.calpoly.messages;

/**
 * Defines a security-processing operation for a message.
 *
 * @author Anthony Soto
 * @version 1.0
 */
public interface MessageSecurityProcessor {
    // Processes a message and returns its secure representation.
   
    String process(String message);
}
