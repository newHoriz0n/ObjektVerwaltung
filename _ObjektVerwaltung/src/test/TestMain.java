package test;

import ctrl.KeyHandler;
import ctrl.OV_Controller;
import exe.OV_MainFrame;
import model.Betrachter;
import model.ObjektVerwaltung;

public class TestMain {

	
	public static void main(String [] args) {
		
		Betrachter b = new TestBetrachter(50000,50000);		
		ObjektVerwaltung ov = new ObjektVerwaltung(b);
		OV_Controller tc = new OV_Controller();
		tc.addListener((KeyHandler) b);
		
		OV_MainFrame mf = new OV_MainFrame(ov);
		
		mf.addKeyListener(tc);
		
		mf.requestFocus();
	}
	
}
