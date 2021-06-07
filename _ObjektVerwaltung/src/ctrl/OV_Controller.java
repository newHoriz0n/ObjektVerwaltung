package ctrl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import exe.OV_View;

public class OV_Controller implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private OV_View v;

	private boolean[] tasten = new boolean[256];

	private long lastMoveUpdate;
	private long moveUpdateRate = 10;
	private int[] aktRealMausPos;

	private List<KeyHandler> keyHandler;
	private List<MouseHandler> mouseLHandler;

	public OV_Controller() {
		this.keyHandler = new ArrayList<>();
		this.mouseLHandler = new ArrayList<>();
		this.aktRealMausPos = new int[2];
	}

	public void setViewer(OV_View v) {
		this.v = v;
	}

	public void addKeyHandler(KeyHandler l) {
		this.keyHandler.add(l);
	}

	public void addMouseHandler(MouseHandler l) {
		this.mouseLHandler.add(l);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (tasten.length > e.getKeyCode()) {
			tasten[e.getKeyCode()] = true;
		}
		for (KeyHandler k : keyHandler) {
			k.handleUpdate(tasten);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (tasten.length > e.getKeyCode()) {
			tasten[e.getKeyCode()] = false;
		}
		for (KeyHandler k : keyHandler) {
			k.handleUpdate(tasten);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// MOUSE WHEEL

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	// MOUSE MOVE

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (System.currentTimeMillis() - lastMoveUpdate > moveUpdateRate) {
			aktRealMausPos = getRealVonScreenKoords(e.getX(), e.getY());
			lastMoveUpdate = System.currentTimeMillis();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (System.currentTimeMillis() - lastMoveUpdate > moveUpdateRate) {
			aktRealMausPos = getRealVonScreenKoords(e.getX(), e.getY());
			lastMoveUpdate = System.currentTimeMillis();
			for (MouseHandler m : mouseLHandler) {
				m.handleMouseUpdate(this, v);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public int[] getRealVonScreenKoords(int screenX, int screenY) {
		if (v != null) {
			double[] offset = v.getOffset();
			return new int[] { (int) (screenX - offset[0]), (int) (screenY - offset[1]) };
		}
		return new int[] { -1, -1 };
	}

	public int[] getAktRealMausPos() {
		return aktRealMausPos;
	}

}
