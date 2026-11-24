package edu.calpoly.monitor;

/**
 * Decides whether a data source is currently available.
 *
 * @author Tommy Phan
 * @version 1.0
 */
public interface AvailabilityPolicy {
    boolean isAvailable(String source, long lastReceived, long currentTime);
}