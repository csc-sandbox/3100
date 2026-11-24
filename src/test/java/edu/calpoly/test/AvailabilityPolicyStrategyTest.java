package edu.calpoly.test.sprint3;

import edu.calpoly.monitor.AvailabilityPolicy;
import edu.calpoly.monitor.FixedTimeoutAvailabilityPolicy;
import edu.calpoly.monitor.SourceTimeoutAvailabilityPolicy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Tests availability decisions made by different policies.
 *
 * @author Tommy Phan
 * @version 1.0
 */
public class AvailabilityPolicyStrategyTest {
    @Test
    void policiesMakeDifferentDecisionsForSameGazeData() {
        AvailabilityPolicy fixed =
                new FixedTimeoutAvailabilityPolicy(1_000);
        AvailabilityPolicy sourceBased =
                new SourceTimeoutAvailabilityPolicy();

        assertTrue(fixed.isAvailable("GAZE", 9_400, 10_000));
        assertFalse(sourceBased.isAvailable("GAZE", 9_400, 10_000));
    }

    @Test
    void fixedPolicyKeepsOriginalOneSecondRule() {
        AvailabilityPolicy fixed =
                new FixedTimeoutAvailabilityPolicy(1_000);

        assertTrue(fixed.isAvailable("ROBOT", 9_000, 10_000));
        assertFalse(fixed.isAvailable("ROBOT", 8_999, 10_000));
    }
}