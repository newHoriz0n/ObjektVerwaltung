package ctrl.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class ButtonRound extends Button {

	private int posX, posY, radius;

	public ButtonRound(int posX, int posY, int radius) {
		this.posX = posX;
		this.posY = posY;
		this.radius = radius;
	}

	@Override
	public boolean checkMouseOver(int x, int y) {
		double d = Math.sqrt(Math.pow(x - posX, 2) + Math.pow(y - posY, 2));
		return d <= radius;
	}

	@Override
	public void draw(Graphics2D g) {

		if (mouseOver) {
			g.setColor(Color.ORANGE);
			g.drawOval(posX - radius, posY - radius, 2 * radius, 2 * radius);
			if (mouseHoldLinks) {
				g.setColor(Color.RED);
				g.drawOval(posX - radius, posY - radius, 2 * radius, 2 * radius);
			}
			if (mouseHoldRechts) {
				g.setColor(Color.BLUE);
				g.drawOval(posX - radius, posY - radius, 2 * radius, 2 * radius);
			}
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + posX;
		result = prime * result + posY;
		result = prime * result + radius;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ButtonRound other = (ButtonRound) obj;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		if (radius != other.radius)
			return false;
		return true;
	}


	
}
