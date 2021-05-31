package exe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import model.ObjektVerwaltung;

public class OV_View extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ObjektVerwaltung ov;

	public OV_View(ObjektVerwaltung ov) {
		this.ov = ov;
		
		ov.setView(this);
		
		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				ov.changeSichtaufloesung(e.getPreciseWheelRotation());
			}
		});
		
//		PaintThread pt = new PaintThread();
//		pt.start();
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.translate(getWidth() / 2 - ov.getBetrachter().getX(), getHeight() / 2 - ov.getBetrachter().getY());
		
		ov.draw(g2d);

		g2d.translate(getWidth() / 2 + ov.getBetrachter().getX(), getHeight() / 2 + ov.getBetrachter().getY());
		
	}
	
	class PaintThread extends Thread {
		
		public PaintThread () {
			
			Timer t = new Timer();
			TimerTask tt  = new TimerTask() {
				
				@Override
				public void run() {
					updateUI();
				}
			};
			t.scheduleAtFixedRate(tt, 0, 30);
			
		}
		
	}

}
