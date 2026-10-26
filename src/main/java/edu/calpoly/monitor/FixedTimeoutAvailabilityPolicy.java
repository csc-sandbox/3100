package edu.calpoly.monitor;

/**
 * Uses one fixed timeout for every data source.
 *
 * @author Tommy Phan
 * @version 1.0
 */
public final class FixedTimeoutAvailabilityPolicy
        implements AvailabilityPolicy {
    private final long timeoutMillis;

    public FixedTimeoutAvailabilityPolicy(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public boolean isAvailable(
            String source, long lastReceived, long currentTime) {
        return currentTime - lastReceived <= timeoutMillis;
    }
}