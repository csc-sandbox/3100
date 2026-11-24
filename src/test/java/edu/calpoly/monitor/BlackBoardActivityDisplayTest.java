package edu.calpoly.monitor;


import edu.calpoly.provided.A03DataGenerator;
import edu.calpoly.provided.Blackboard;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies Blackboard-driven activity display updates and original count labels.
- this satisfy the requirement of testing 
 * @author Diego Martinez Parra (sp4msvwnz)
 * @version 1.0
 */
class BlackboardActivityDisplayTest {
    private ActivityDisplay display;

    @BeforeEach // creates the subscribes fresh display before each test
    void setUp() throws Exception {
        Blackboard.clearValues();

        SwingUtilities.invokeAndWait(() -> {
            display = new ActivityDisplay(false);
            display.subscribeToActivity();
        });
    }

    @AfterEach // removes the subscritions and clears out the Blackboard after each test
    void tearDown() throws Exception {
        if (display != null) {
            display.dispose();
        }

        // Wait for previously queued display updates to finish.
        SwingUtilities.invokeAndWait(() -> { });
        Blackboard.clearValues();
    }

    @Test
    void displayedStateFollowsEveryActivityTransition() throws Exception {
        publishAndAssert("IDLE");
        publishAndAssert("ACTIVE");
        publishAndAssert("IDLE");
    }

    @Test
    void instructorGeneratorLeavesDisplayIdle() throws Exception {
        A03DataGenerator.publishActivitySequence();

        assertDisplayedState("IDLE");
        assertEquals("IDLE", Blackboard.get(Blackboard.ACTIVITY_STATE));
    }

    @Test
    void unrelatedPropertyLeavesActivityStateUnchanged() throws Exception {
        publishAndAssert("ACTIVE");

        A03DataGenerator.publishGazeSequence();

        assertDisplayedState("ACTIVE");
    }

    @Test
    void unsupportedValuesLeaveLastValidStateUnchanged() throws Exception {
        publishAndAssert("ACTIVE");

        Blackboard.put(Blackboard.ACTIVITY_STATE, "UNKNOWN");
        assertDisplayedState("ACTIVE");

        Blackboard.put(Blackboard.ACTIVITY_STATE, 42);
        assertDisplayedState("ACTIVE");

        Blackboard.put(Blackboard.ACTIVITY_STATE, "");
        assertDisplayedState("ACTIVE");
    }

    @Test
    void nullPublicationIsRejectedWithoutChangingDisplay() throws Exception {
        publishAndAssert("ACTIVE");

        assertThrows(
                NullPointerException.class,
                () -> Blackboard.put(Blackboard.ACTIVITY_STATE, null));

        assertDisplayedState("ACTIVE");
    }

    @Test
    void unsubscribeStopsSubsequentUpdates() throws Exception {
        publishAndAssert("IDLE");

        display.unsubscribeFromActivity();
        Blackboard.put(Blackboard.ACTIVITY_STATE, "ACTIVE");

        assertDisplayedState("IDLE");
    }

    @Test
    void unsubscribeInvalidatesAnAlreadyQueuedUpdate() throws Exception {
        publishAndAssert("IDLE");

        SwingUtilities.invokeAndWait(() -> {
            Blackboard.put(Blackboard.ACTIVITY_STATE, "ACTIVE");
            display.unsubscribeFromActivity();
        });

        assertDisplayedState("IDLE");
    }

    @Test
    void resubscribeDoesNotApplyAnOldQueuedUpdate() throws Exception {
        publishAndAssert("IDLE");

        SwingUtilities.invokeAndWait(() -> {
            Blackboard.put(Blackboard.ACTIVITY_STATE, "ACTIVE");
            display.unsubscribeFromActivity();
            display.subscribeToActivity();
        });

        assertDisplayedState("IDLE");

        publishAndAssert("IDLE");    // ACTIVE is currently stored, so publish a different value first.

        publishAndAssert("ACTIVE");
    }

    @Test
    void originalClassifierStatesRemainSupported() throws Exception {
        publishAndAssert("NO_ACTIVITY");
        publishAndAssert("NORMAL");
        publishAndAssert("HIGH_ACTIVITY");
    }

    @Test
    void originalCountLabelsStillDisplayProvidedCounts() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            display.updateCounts(12, 8, 4, 6);

            assertArrayEquals(
                    new String[] {
                        "Robot: 12 messages",
                        "Gaze: 8 messages",
                        "Affect: 4 messages",
                        "LiDAR: 6 messages"
                    },
                    display.getCountTexts());
        });
    }

    private void publishAndAssert(String state) throws Exception {
        Blackboard.put(Blackboard.ACTIVITY_STATE, state);
        assertDisplayedState(state);
    }

    private void assertDisplayedState(String state) throws Exception {
        SwingUtilities.invokeAndWait(() ->
                assertEquals(
                        "System State: " + state,
                        display.getSystemStateText()));
    }
}