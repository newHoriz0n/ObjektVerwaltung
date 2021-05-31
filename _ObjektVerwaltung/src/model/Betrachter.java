package model;

import java.awt.Graphics2D;

public interface Betrachter {

	public void update();

	public void setPosition(double x, double y);

	public double getX();

	public double getY();

	public void draw(Graphics2D g);
}
