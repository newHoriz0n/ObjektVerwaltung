package test;

import exe.OV_MainFrame;
import model.Betrachter;
import model.ObjektVerwaltung;

public class TestMain {

	
	public static void main(String [] args) {
		
		Betrachter b = new TestBetrachter(50000,50000);
		
		ObjektVerwaltung ov = new ObjektVerwaltung(b);
		
		OV_MainFrame mf = new OV_MainFrame(ov);
		mf.requestFocus();
	}
	
}
