package edu.calpoly.eye;

/**
 * Strategy used for GatherEye
 * Implemented by PointAnalysis, AreaAnalysis, and VectorAnalysis
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public interface GazeAnalysisStrategy {
	String createMessage(float x, float y);
}
