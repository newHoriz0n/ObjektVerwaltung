package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import ctrl.OV_KeyHandler;
import ctrl.OV_MouseHandler;
import ctrl.gui.Aktion;
import ctrl.EEventTyp;
import ctrl.OV_Controller;
import exe.OV_MainFrame;
import model.Betrachter;
import model.KreisObjekt;
import model.ObjektVerwaltung;
import model.listener.EUpdateTopic;
import view.OV_View;

public class TestMain {

	public static void main(String[] args) {

		Betrachter b = new TestBetrachter(50000, 50000);
		ObjektVerwaltung ov = new ObjektVerwaltung(b);

		Random r = new Random();

		long start = System.currentTimeMillis();

		// Generiere Kreise

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("gfx/smiley.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 1000000; i++) {
			KreisObjekt k = new KreisObjekt(r.nextInt(700000), r.nextInt(700000), 5 + r.nextInt(100),
					new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 0),
					new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

			k.setBild(img);
			k.setAusrichtung(r.nextDouble() * 2 * Math.PI);
			k.setEventAktion(EEventTyp.MAUSKLICK_LINKS, new Aktion() {
				
				@Override
				public void run() {
					k.setAusrichtung(k.getAusrichtung() + 0.1);
				}
			});
			k.setEventAktion(EEventTyp.MAUSKLICK_RECHTS, new Aktion() {
				
				@Override
				public void run() {
					k.setAusrichtung(k.getAusrichtung() - 0.1);
				}
			});
			
			ov.addKreis(k);
			
		}

		System.out.println("Erzeug - Dauer: " + (System.currentTimeMillis() - start));

		// Berechne Relevante
		ov.calcRelevanzen();

		OV_Controller tc = new OV_Controller(ov);
		tc.addKeyHandler((OV_KeyHandler) b);
		tc.addMouseHandler((OV_MouseHandler) b);

		ov.addUpdateListener(EUpdateTopic.RELEVANZEN, tc);

		OV_View v = new OV_View(ov, tc);
		tc.setViewer(v);
		v.addMouseListener(tc);
		v.addMouseMotionListener(tc);
		v.addMouseWheelListener(tc);

		OV_MainFrame mf = new OV_MainFrame(ov, v);
		mf.addKeyListener(tc);

		mf.requestFocus();
	}

}
