package ctrl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class OV_Controller implements KeyListener {

	private boolean[] tasten = new boolean[256];

	private List<KeyHandler> listener;
	
	public OV_Controller() {
		this.listener = new ArrayList<KeyHandler>();
	}
	
	public void addListener(KeyHandler l) {
		this.listener.add(l);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (tasten.length > e.getKeyCode()) {
			tasten[e.getKeyCode()] = true;
		}
		for (KeyHandler k : listener) {
			k.handleUpdate(tasten);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (tasten.length > e.getKeyCode()) {
			tasten[e.getKeyCode()] = false;
		}
		for (KeyHandler k : listener) {
			k.handleUpdate(tasten);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
