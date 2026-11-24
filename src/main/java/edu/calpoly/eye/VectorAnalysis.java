package edu.calpoly.eye;

import java.util.ArrayList;

/**
 * Implementation of GazeAnalysisStrategy
 *
 * Calculates and broadcasts vector based off of up to 5
 * received points from GatherEye, then resets
 *
 * The first of the five points will form the tail of the vector,
 * the rest approximate to the tip
 *
 * Format header: "GAZE VECTOR: HEAD_X;HEAD_Y;TAIL_X;TAIL_Y"
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public class VectorAnalysis implements GazeAnalysisStrategy{

	private Point latestPoint;
	private Vector latestHeadVector;
	private Vector latestTailVector;
	
	private final ArrayList<Point> pointList = new ArrayList<>();

	private static final int MAX_POINTS = 5;

	public VectorAnalysis() {
		latestHeadVector = new Vector(0, 0);
		latestTailVector = new Vector(0, 0);
	}

	@Override
	public String createMessage(float x, float y) {
		
		if (pointList.size() >= MAX_POINTS) {
			pointList.remove(0);
		}

		latestPoint = new Point(x, y);
		pointList.add(latestPoint);

		if (pointList.size() == 1) {
			this.latestHeadVector = new Vector(0, 0);
			this.latestTailVector = new Vector(0, 0);
		} else {
			calculateVector(); 
		}

		return "GAZE VECTOR: HEAD_X;HEAD_Y;TAIL_X;TAIL_Y," +
			latestHeadVector.getX() + "," +
			latestHeadVector.getY() + "," +
			latestTailVector.getX() + "," +
			latestTailVector.getY();
	}

	private void calculateVector() {
	
			Point tail = pointList.get(0);

			latestTailVector = new Vector(
				tail.getX(),
				tail.getY()
			);

			float totalX = 0;
			float totalY = 0;

			float meanX = 0;
			float meanY = 0;

			for (int i = 1; i < pointList.size(); i++) {
				Point point = pointList.get(i);
				totalX += point.getX();
				totalY += point.getY();
			}

			int count = pointList.size() - 1;

			meanX = totalX / count;
			meanY = totalY / count; 

			this.latestHeadVector = new Vector(meanX, meanY);

	}
}
