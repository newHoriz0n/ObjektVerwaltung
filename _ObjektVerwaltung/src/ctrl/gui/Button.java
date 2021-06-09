package ctrl.gui;

import java.awt.Graphics2D;

public abstract class Button {

	private static long hoverTimeOut = 10; // [ms] Zeit zwischen letzter Bewegung und MouseOverCheck
	private long lastMove;

	protected boolean mouseOver;
	protected boolean mouseHoldLinks;
	protected boolean mouseHoldRechts;

	protected Aktion aktionLinks;
	protected Aktion aktionRechts;

	public void setAktionLinks(Aktion a) {
		this.aktionLinks = a;
	}

	public void setAktionRechts(Aktion a) {
		this.aktionRechts = a;
	}

	public void handleMouseMove(int x, int y) {
		if (System.currentTimeMillis() - lastMove > hoverTimeOut) {
			lastMove = System.currentTimeMillis();
			if (checkMouseOver(x, y)) {
				mouseOver = true;
				return;
			}
			mouseOver = false;
			mouseHoldLinks = false;
			mouseHoldRechts = false;
		}
	}

	public void handleMousePress(int x, int y, int button) {
		if (checkMouseOver(x, y)) {
			if (button == 1) {
				mouseHoldLinks = true;
			}
			if (button == 3) {
				mouseHoldRechts = true;
			}
			return;
		}
		mouseHoldLinks = false;
		mouseHoldRechts = false;
	}

	public void handleMouseRelease(int x, int y, int button) {
		if (checkMouseOver(x, y)) {
			if (button == 1 && aktionLinks != null) {
				aktionLinks.run();
				mouseHoldLinks = false;
			}
			if (button == 3 && aktionRechts != null) {
				aktionRechts.run();
				mouseHoldRechts = false;
			}
		}
	}

	public abstract boolean checkMouseOver(int x, int y);

	public abstract void draw(Graphics2D g);

}
