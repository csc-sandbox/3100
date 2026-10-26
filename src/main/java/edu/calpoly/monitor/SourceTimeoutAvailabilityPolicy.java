package edu.calpoly.monitor;

/**
 * Uses a timeout based on how often each data source normally sends data.
 *
 * @author Tommy Phan
 * @version 1.0
 */
public final class SourceTimeoutAvailabilityPolicy
        implements AvailabilityPolicy {
    private static final long DEFAULT_TIMEOUT_MS = 1_000;

    @Override
    public boolean isAvailable(
            String source, long lastReceived, long currentTime) {
        return currentTime - lastReceived <= timeoutFor(source);
    }

    private long timeoutFor(String source) {
        return switch (source) {
            case "GAZE" -> 200;
            case "LIDAR" -> 400;
            case "ROBOT" -> 500;
            case "AFFECT" -> 1_000;
            default -> DEFAULT_TIMEOUT_MS;
        };
    }
}