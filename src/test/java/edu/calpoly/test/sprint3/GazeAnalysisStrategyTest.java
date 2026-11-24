
package edu.calpoly.test.sprint3;

import edu.calpoly.eye.GatherEye;
import edu.calpoly.eye.GazeAnalysisStrategy;
import edu.calpoly.eye.PointAnalysis;
import edu.calpoly.eye.AreaAnalysis;
import edu.calpoly.eye.VectorAnalysis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Used to test GatherEye's implementation of GazeAnalysisStrategy.java.
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public class GazeAnalysisStrategyTest {

    private static final String AREA_ANALYSIS_HEADER =
            "GAZE AREA: X;Y;WIDTH;HEIGHT";

    private static final String VECTOR_ANALYSIS_HEADER =
            "GAZE VECTOR: HEAD_X;HEAD_Y;TAIL_X;TAIL_Y";

    @Test
    void testCreateMessage() {
        GazeAnalysisStrategy strategy = new PointAnalysis();

        assertEquals(
                "GAZE,100.0,200.0",
                strategy.createMessage(100.0f, 200.0f)
        );
    }

    @Test
    void testCreateAreaMessage() {
        GazeAnalysisStrategy strategy = new AreaAnalysis();

        assertEquals(
                AREA_ANALYSIS_HEADER,
                strategy.createMessage(1.0f, 2.0f).split(",")[0]
        );
    }

    @Test
    void testCreateVectorMessage() {
        GazeAnalysisStrategy strategy = new VectorAnalysis();

        assertEquals(
                VECTOR_ANALYSIS_HEADER,
                strategy.createMessage(1.0f, 2.0f).split(",")[0]
        );
    }

    @Test
    void testSwitchStrategy() {
        GatherEye gatherEye = new GatherEye();

        GazeAnalysisStrategy pointStrategy = new PointAnalysis();
        GazeAnalysisStrategy areaStrategy = new AreaAnalysis();

        gatherEye.setGazeAnalysisStrategy(pointStrategy);
        gatherEye.setGazeAnalysisStrategy(areaStrategy);

        assertEquals(
                AREA_ANALYSIS_HEADER,
                areaStrategy.createMessage(1.0f, 2.0f).split(",")[0]
        );
    }
}
