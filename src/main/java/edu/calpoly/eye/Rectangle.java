package edu.calpoly.eye;

/**
 *
 * Rectangle class used to better easily store
 * information for AreaAnalysis
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0 (2026-10-24)
 */
public class Rectangle {

	private float x;
	private float y;
	private float width;
	private float height;

	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public float getWidth(){
		return this.width;
	}
	public float getHeight(){
		return this.height;
	}

	public void setX(float x){
		this.x = x;
	}
	public void setY(float x){
		this.y = x;
	}
	public void setWidth(float x){
		this.width = x;
	}
	public void setHeight(float x){
		this.height = x;
	}


}
