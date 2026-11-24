package edu.calpoly.messages;

import edu.calpoly.provided.Encryption;

/**
 * Adapts the provided AES/GCM encryption utility to the message security interface.
 *
 * @author Anthony Soto
 * @version 1.0
 */
public final class EncryptionMessageSecurity implements MessageSecurityProcessor {
    
    // Encrypts a message using the provided AES/GCM implementation.

    @Override
    public String process(String message) {
        return Encryption.encrypt(message);
    }
}
