package edu.calpoly.eye;

import java.util.ArrayList;

/**
 * Implementation of GaseAnalysisStrategy
 *
 * Calculates and broadcasts "area" rectangle based off of up to 5
 * received points from GatherEye, then resets
 *
 * Format header: "GAZE AREA: X;Y;WIDTH;HEIGHT"
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public class AreaAnalysis implements GazeAnalysisStrategy{

	private Point latestPoint;
	private Rectangle latestRectangle;

	private final ArrayList<Point> pointList = new ArrayList<>();

	private static final int MAX_POINTS = 5;
	private static final int BORDER_SIZE = 3;


	@Override
	public String createMessage(float x, float y) {
		
		if (pointList.size() >= MAX_POINTS) {
			pointList.remove(0);
		}

		latestPoint = new Point(x, y);
		pointList.add(latestPoint);

		if (pointList.size() > 1) {
			latestRectangle = calculateRectangle();
		} else {
			latestRectangle = new Rectangle(
				latestPoint.getX() - BORDER_SIZE,
				latestPoint.getY() - BORDER_SIZE,
				BORDER_SIZE * 2,
				BORDER_SIZE * 2
			);
		}

		return "GAZE AREA: X;Y;WIDTH;HEIGHT," +
			latestRectangle.getX() + "," +
			latestRectangle.getY() + "," +
			latestRectangle.getWidth() + "," + 
			latestRectangle.getHeight();
	}

	private Rectangle calculateRectangle() {

		float totalX = 0;
		float totalY = 0;

		float meanX = 0;
		float meanY = 0;


		for (Point point : pointList) {
			totalX += point.getX();
			totalY += point.getY();
		}

		int count = pointList.size();

		meanX = totalX / count; 
		meanY = totalY / count; 
		
		float deviationX = 0;
		float deviationY = 0;

		for (Point point : pointList) {
			deviationX += Math.abs(point.getX() - meanX);
			deviationY += Math.abs(point.getY() - meanY);
		}

		float averageDeviationX = deviationX / count;
		float averageDeviationY = deviationY / count;

		float rectWidth = averageDeviationX * 2;
		float rectHeight = averageDeviationY * 2;

		float rectX = meanX - averageDeviationX;
		float rectY = meanY - averageDeviationY;
		

		return new Rectangle(
			rectX,
			rectY,
			rectWidth,
			rectHeight
		);
	}

}
