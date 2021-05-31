package test;

import java.awt.Color;
import java.awt.Graphics2D;

import ctrl.KeyHandler;
import model.Betrachter;

public class TestBetrachter implements Betrachter, KeyHandler {

	private double x;
	private double y;

	private boolean links;
	private boolean rechts;
	private boolean hoch;
	private boolean runter;

	private int size;

	public TestBetrachter(double x, double y) {
		this.x = x;
		this.y = y;
		this.size = 20;
	}

	@Override
	public void update(long dt) {

		double s = (double)dt / (double)1000000000;
		
		if (links) {
			x--;
		} else if (hoch) {
			y -= 100 * s;
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

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) (x - size), (int) (y - size), (int) (2 * size), (int) (2 * size));
	}

	@Override
	public void drawFixed(Graphics2D g, int posX, int posY) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) (posX - size), (int) (posY - size), (int) (2 * size), (int) (2 * size));
	}

}
