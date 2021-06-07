package test;

import java.awt.Color;
import java.util.Random;

import ctrl.KeyHandler;
import ctrl.MouseHandler;
import ctrl.OV_Controller;
import exe.OV_MainFrame;
import exe.OV_View;
import model.Betrachter;
import model.Kreis;
import model.ObjektVerwaltung;

public class TestMain {

	
	public static void main(String [] args) {
		
		Betrachter b = new TestBetrachter(50000,50000);		
		ObjektVerwaltung ov = new ObjektVerwaltung(b);
		
		Random r = new Random();

		long start = System.currentTimeMillis();

		// Geniere Kreise
		for (int i = 0; i < 1000000; i++) {
			ov.addKreis(new Kreis(r.nextInt(700000), r.nextInt(700000), 5 + r.nextInt(100),
					new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))));
		}

		System.out.println("Erzeug - Dauer: " + (System.currentTimeMillis() - start));

		// Berechne Relevante
		ov.calcRelevanzen();

		OV_Controller tc = new OV_Controller();
				
		tc.addKeyHandler((KeyHandler) b);
		tc.addMouseHandler((MouseHandler) b);
		
		OV_View v =  new OV_View(ov);
		tc.setViewer(v);
		v.addMouseListener(tc);
		v.addMouseMotionListener(tc);
		v.addMouseWheelListener(tc);
		
		OV_MainFrame mf = new OV_MainFrame(ov, v);
		mf.addKeyListener(tc);
		
		mf.requestFocus();
	}
	
}
