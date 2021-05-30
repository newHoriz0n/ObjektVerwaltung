package test;

import model.Betrachter;

public class TestBetrachter implements Betrachter {

	private double x;
	private double y;

	public TestBetrachter(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		this.x++;
	}

	@Override
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

}
