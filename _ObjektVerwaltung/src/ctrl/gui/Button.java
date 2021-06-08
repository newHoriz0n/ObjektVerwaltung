package ctrl.gui;

import java.awt.Graphics2D;

public abstract class Button {

	private static long hoverTimeOut = 10; // [ms] Zeit zwischen letzter Bewegung und MouseOverCheck
	private long lastMove;

	protected boolean mouseOver;
	protected boolean mouseHold;

	protected Aktion aktion;

	public void setAktion(Aktion a) {
		this.aktion = a;
	}

	public void handleMouseMove(int x, int y) {
		if (System.currentTimeMillis() - lastMove > hoverTimeOut) {
			lastMove = System.currentTimeMillis();
			if (checkMouseOver(x, y)) {
				mouseOver = true;
				return;
			}
			mouseOver = false;
			mouseHold = false;
		}
	}

	public void handleMousePress(int x, int y) {
		if (checkMouseOver(x, y)) {
			mouseHold = true;
			return;
		}
		mouseHold = false;
	}

	public void handleMouseRelease(int x, int y) {
		if (checkMouseOver(x, y)) {
			if (aktion != null) {
				aktion.run();
			}
		}
		mouseHold = false;
	}

	public abstract boolean checkMouseOver(int x, int y);

	public abstract void draw(Graphics2D g);

}
