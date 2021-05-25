package model;

public class Betrachter {

	private double x;
	private double y;

	public Betrachter() {
		this.x = 0;
		this.y = 0;		
	}

	public void update() {
		this.x++;
		System.out.println(x);
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}


}
