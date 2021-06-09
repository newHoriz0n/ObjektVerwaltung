package exe;

import javax.swing.JFrame;

import model.ObjektVerwaltung;
import view.OV_View;

public class OV_MainFrame extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OV_MainFrame(ObjektVerwaltung ov, OV_View v) {
		add(v);
		
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	
}
