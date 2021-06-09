package ctrl;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import ctrl.gui.Aktion;
import ctrl.gui.Button;
import ctrl.gui.ButtonRound;
import ctrl.gui.OV_GUI_Controller;
import exe.OV_View;
import model.KreisObjekt;
import model.ObjektVerwaltung;
import model.listener.EUpdateTopic;
import model.listener.UpdateListener;

public class OV_Controller
		implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, UpdateListener {

	private ObjektVerwaltung ov;
	private OV_View v;

	private boolean[] tasten = new boolean[256];

	private long lastMoveUpdate;
	private long moveUpdateRate = 10;
	private int[] aktRealMausPos;

	private List<OV_KeyHandler> keyHandler;
	private List<OV_MouseHandler> mouseLHandler;

	// GUI Controller
	private OV_GUI_Controller gc;

	public OV_Controller(ObjektVerwaltung ov) {
		this.ov = ov;
		this.keyHandler = new ArrayList<>();
		this.mouseLHandler = new ArrayList<>();
		this.aktRealMausPos = new int[2];

		this.gc = new OV_GUI_Controller();

	}

	public void setViewer(OV_View v) {
		this.v = v;
	}

	public void addKeyHandler(OV_KeyHandler l) {
		this.keyHandler.add(l);
	}

	public void addMouseHandler(OV_MouseHandler l) {
		this.mouseLHandler.add(l);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (tasten.length > e.getKeyCode()) {
			tasten[e.getKeyCode()] = true;
		}
		for (OV_KeyHandler k : keyHandler) {
			k.handleUpdate(tasten);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (tasten.length > e.getKeyCode()) {
			tasten[e.getKeyCode()] = false;
		}
		for (OV_KeyHandler k : keyHandler) {
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

		if (System.currentTimeMillis() - lastMoveUpdate > moveUpdateRate) {
			aktRealMausPos = getRealVonScreenKoords(e.getX(), e.getY());
			lastMoveUpdate = System.currentTimeMillis();
			gc.handleMouseMove(aktRealMausPos[0], aktRealMausPos[1]);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (System.currentTimeMillis() - lastMoveUpdate > moveUpdateRate) {
			aktRealMausPos = getRealVonScreenKoords(e.getX(), e.getY());
			lastMoveUpdate = System.currentTimeMillis();
			for (OV_MouseHandler m : mouseLHandler) {
				m.handleMouseUpdate(this, v);
			}
			gc.handleMouseMove(aktRealMausPos[0], aktRealMausPos[1]);
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
		gc.handleMousePress(aktRealMausPos[0], aktRealMausPos[1], e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		gc.handleMouseRelease(aktRealMausPos[0], aktRealMausPos[1], e.getButton());
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

	@Override
	public void handleOVUpdate(EUpdateTopic topic) {
		if (topic.equals(EUpdateTopic.RELEVANZEN)) {

			List<Button> bs = new ArrayList<>();
			for (KreisObjekt k : ov.getDirektSichtbareKreise()) {
				ButtonRound b = new ButtonRound((int) k.getPosX(), (int) k.getPosY(), (int) k.getRadius());
				b.setAktionLinks(new Aktion() {
					
					@Override
					public void run() {
						k.handleEvent(EEventTyp.MAUSKLICK_LINKS);
					}
				});
				b.setAktionRechts(new Aktion() {
					
					@Override
					public void run() {
						k.handleEvent(EEventTyp.MAUSKLICK_RECHTS);
					}
				});
				bs.add(b);
			}
			gc.setCurrentButtons(bs);
		}
	}

	public void draw(Graphics2D g2d) {
		gc.drawGUIController(g2d);
	}

}
