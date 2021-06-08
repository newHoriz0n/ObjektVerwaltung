package exe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JPanel;

import ctrl.OV_Controller;
import model.ObjektVerwaltung;

public class OV_View extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ObjektVerwaltung ov;
	private OV_Controller oc;
	
	private double [] offset = new double [2];

	public OV_View(ObjektVerwaltung ov, OV_Controller oc) {
		this.ov = ov;
		this.oc = oc;
		
		ov.setView(this);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		offset[0] = getWidth() / 2 - ov.getBetrachter().getX();
		offset[1] = getHeight() / 2 - ov.getBetrachter().getY();
		
		AffineTransform at = new AffineTransform();
		at.translate(offset[0], offset[1]);
		g2d.transform(at);
		
		ov.draw(g2d);
		oc.draw(g2d);

		try {
			at.invert();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		g2d.transform(at);
		
		ov.getBetrachter().drawFixed(g2d, getWidth() / 2, getHeight() / 2);

	}
	
	public double[] getOffset() {
		return offset;
	}
	
}
