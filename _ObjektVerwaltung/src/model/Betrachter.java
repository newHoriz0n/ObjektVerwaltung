package model;

import java.awt.Graphics2D;

public interface Betrachter {

	public void update(long dt);

	public void setPosition(double x, double y);

	public double getX();

	public double getY();

	public void draw(Graphics2D g);

	/**
	 * Zeichne Betrachter auf ScreenPosition X,Y
	 * @param g2d
	 */
	public void drawFixed(Graphics2D g2d, int x, int y);
}
