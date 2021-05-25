package model;

public class Sichtfeld {

	private double sichtMinRad;
	private double sichtMaxRad;
	
	public Sichtfeld(double min, double max) {
		this.sichtMaxRad = max;
		this.sichtMinRad = min;
	}
	
	public boolean checkCovered(double min, double max) {
		
		// TODO: Überprüfen, ob Winkelbereich abgedeckt ist
		
		return false;
		
	}
	
}
