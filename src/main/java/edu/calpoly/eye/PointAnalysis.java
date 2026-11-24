package edu.calpoly.eye;

/**
 * Implementation of GazeAnalysisStrategy
 *
 * Broadcasts given X and Y float
 *
 * Format header: "GAZE"
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public class PointAnalysis implements GazeAnalysisStrategy {

	@Override
	public String createMessage(float x, float y) {
		return "GAZE," + x + "," + y; 
	}

}
