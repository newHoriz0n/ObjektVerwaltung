package ctrl.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class OV_GUI_Controller {

	// Verwaltet Buttons

	private Object ButtonLock = new Object();
	private List<Button> buttons = new ArrayList<>();

	public void setCurrentButtons(List<Button> bs) {

		synchronized (ButtonLock) {
			for (Button b : bs) {
				if (!buttons.contains(b)) {
					buttons.add(b);
				}
			}
			for (int i = buttons.size() - 1; i >= 0; i--) {
				if (!bs.contains(buttons.get(i))) {
					buttons.remove(i);
				}
			}
		}
	}

	public void handleMouseMove(int x, int y) {
		for (Button b : buttons) {
			b.handleMouseMove(x, y);
		}
	}

	public void handleMousePress(int x, int y, int button) {
		for (Button b : buttons) {
			b.handleMousePress(x, y, button);
		}
	}

	public void handleMouseRelease(int x, int y, int button) {
		for (Button b : buttons) {
			b.handleMouseRelease(x, y, button);
		}
	}

	public void drawGUIController(Graphics2D g) {
		synchronized (ButtonLock) {
			for (Button b : buttons) {
				b.draw(g);
			}
		}
	}

}
