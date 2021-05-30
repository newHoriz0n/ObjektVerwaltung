package test;

import ctrl.KeyHandler;
import model.Betrachter;

public class TestBetrachter implements Betrachter, KeyHandler {

	private double x;
	private double y;
	
	private boolean links;
	private boolean rechts;
	private boolean hoch;
	private boolean runter;

	public TestBetrachter(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		if (links) {
			x--;
		} else if (hoch) {
			y--;
		} else if (rechts) {
			x++;
		} else if (runter) {
			y++;
		}
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

	@Override
	public void handleUpdate(boolean[] keys) {
		links = keys[37];
		hoch = keys[38];
		rechts = keys[39];
		runter = keys[40];
	}

}
