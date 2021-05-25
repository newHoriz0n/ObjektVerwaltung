package exe;

import javax.swing.JFrame;

import model.ObjektVerwaltung;

public class OV_MainFrame extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OV_MainFrame() {
		ObjektVerwaltung ov = new ObjektVerwaltung();
		
		OV_View v = new OV_View(ov);
		add(v);
		
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String [] args) {
		OV_MainFrame mf = new OV_MainFrame();
		mf.requestFocus();
	}
	
}
