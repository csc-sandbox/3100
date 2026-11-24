package edu.calpoly.eye;

import edu.calpoly.provided.*;



/**
 * Broadcasts X and Y coordinates into message "GAZE,X,Y" format for use in CSV
 *
 * Currently receives X and Y coordinates from class GatherEyeGUI.java
 *
 * @author Marshall Ramsey (mars.rams)
 * @version 1.0 (2026-09-26)
 *
 **/
public class GatherEye {

	private GazeAnalysisStrategy strategy;

	public GatherEye(){
		this.strategy = new PointAnalysis();	
	}

	public void displayGUI(){
		GatherEyeGUI gui = new GatherEyeGUI(this);
	}

	public void setGazeAnalysisStrategy(GazeAnalysisStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException(
				"Null strategy"
			);
		}
		this.strategy = strategy;
	}

	protected void sendBrokerCoords(float x, float y){
		String message = strategy.createMessage(x, y);

		Broker broker = new Broker("localhost", 5000);
		broker.send(message);
	}

}
