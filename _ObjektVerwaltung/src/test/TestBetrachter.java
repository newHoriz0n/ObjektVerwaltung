package test;

import java.awt.Color;
import java.awt.Graphics2D;

import ctrl.KeyHandler;
import ctrl.MouseHandler;
import ctrl.OV_Controller;
import exe.OV_View;
import model.Betrachter;

public class TestBetrachter implements Betrachter, KeyHandler, MouseHandler {

	private double x;
	private double y;

	private double ausrichtung;

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

		double s = (double) dt / (double) 1000000000;

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
		g.setColor(Color.BLACK);
		g.drawLine((int) x, (int) y, (int) (x + Math.cos(ausrichtung) * size), (int) (y - Math.sin(ausrichtung) * size));
	}

	@Override
	public void drawFixed(Graphics2D g, int posX, int posY) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) (posX - size), (int) (posY - size), (int) (2 * size), (int) (2 * size));
		g.setColor(Color.BLACK);
		g.drawLine((int) posX, (int) posY, (int) (posX + Math.cos(ausrichtung) * (size-1)), (int) (posY - Math.sin(ausrichtung) * (size-1)));
	}

	@Override
	public void handleMouseUpdate(OV_Controller c, OV_View v) {
		calcAusrichtung(c.getAktRealMausPos());
		drawFixed((Graphics2D) v.getGraphics(), v.getWidth() / 2, v.getHeight() / 2);
	}

	private void calcAusrichtung(int[] p) {
		this.ausrichtung = Math.atan2(y - p[1], p[0] - x);
	}

}
