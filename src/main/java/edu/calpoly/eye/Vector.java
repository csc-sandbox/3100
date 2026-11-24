package edu.calpoly.eye;

/**
 *
 * Vector class used to better easily store
 * information for VectorAnalysis 
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public class Vector {

	private float x;
	private float y;

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}

	public void setX(float x){
		this.x = x;
	}
	public void setY(float x){
		this.y = x;
	}

}
