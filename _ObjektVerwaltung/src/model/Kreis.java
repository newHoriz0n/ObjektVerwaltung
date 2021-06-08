package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Kreis implements Comparable<Kreis> {

	private int radius;
	private double posX;
	private double posY;

	private double ausrichtung;
	
	private Color farbeHintergrund;
	private Color farbeRahmen;
	private BufferedImage bild;

	private double sichtbarkeit = 0; // [0,1]

	private double centerDistanz = 0;
	private double randDistanz = 0;

	public Kreis(int x, int y, int rad, Color hintergrund, Color rahmen) {
		this.posX = x;
		this.posY = y;
		this.radius = rad;
		this.farbeHintergrund = hintergrund;
		this.farbeRahmen = rahmen;
	}

	public int getRadius() {
		return radius;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setBild(BufferedImage img) {
		this.bild = img;
	}

	public void setAusrichtung(double richtung) {
		this.ausrichtung = richtung;
	}
	
	/**
	 * 
	 * @param b
	 * @param metereologischeSichtweite
	 * @param sichtAufloesung
	 * @return 0: irrelevant, 1: metereologisch sichtbar, 2: im Screen sichtbar
	 */
	public int calcRelevanz(Betrachter b, double metereologischeSichtweite, double sichtAufloesung,
			double screenRadius) {

		// TODO: Berücksichtige Bewegungsgeschwindigkeit in screenRadius

		calcDistanzZu(b.getX(), b.getY());

		this.sichtbarkeit = 1 - ((randDistanz - screenRadius) / metereologischeSichtweite);

		if (sichtbarkeit > 0) {
			if (calcSichtwinkelTan() > sichtAufloesung) {
				if (randDistanz < screenRadius - 100) {
					return 3;
				} else if (randDistanz < screenRadius + 100) {
					return 2;
				}
				return 1;
			}
		}
		return 0;

	}

	public void draw(Graphics2D g, Betrachter b, double screenRadius) {
		if (randDistanz < screenRadius) {
			g.setColor(farbeHintergrund);
			g.fillOval((int) (posX - radius), (int) (posY - radius), (int) (radius * 2), (int) (radius * 2));
			if (bild != null) {
				AffineTransform at = new AffineTransform();
				at.translate(posX - radius, posY - radius);
				at.rotate(ausrichtung, radius, radius);
				at.scale(radius * 2 / (double)bild.getWidth(), radius * 2 / (double)bild.getHeight());
				g.drawImage(bild, at, null);
			}
			g.setColor(farbeRahmen);
			g.drawOval((int) (posX - radius), (int) (posY - radius), (int) (radius * 2), (int) (radius * 2));
		}
	}

	public void drawEntfernt(Graphics2D g, Betrachter b, double screenRadius, double entferntleistenHoehe) {
		double dx = posX - b.getX();
		double dy = posY - b.getY();
		double r = calcErscheinungsGroesse(b, screenRadius);

		if (centerDistanz > screenRadius - entferntleistenHoehe / 2) {

			g.setColor(farbeHintergrund);
			g.fillOval((int) (b.getX() + dx * (screenRadius - entferntleistenHoehe / 2) / centerDistanz - r),
					(int) (b.getY() + dy * (screenRadius - entferntleistenHoehe / 2) / centerDistanz - r),
					(int) (r * 2), (int) (r * 2));

			g.setColor(farbeRahmen);
			g.drawOval((int) (b.getX() + dx * (screenRadius - entferntleistenHoehe / 2) / centerDistanz - r),
					(int) (b.getY() + dy * (screenRadius - entferntleistenHoehe / 2) / centerDistanz - r),
					(int) (r * 2), (int) (r * 2));

			double sFaktor = 1 - Math.max(0, Math.min(1, sichtbarkeit));

			// Nebel
			g.setColor(new Color(255, 255, 255, (int) (255 * sFaktor)));
			g.fillOval((int) (b.getX() + dx * (screenRadius - entferntleistenHoehe / 2) / centerDistanz - r),
					(int) (b.getY() + dy * (screenRadius - entferntleistenHoehe / 2) / centerDistanz - r),
					(int) (r * 2), (int) (r * 2));

		}
	}

	/**
	 * 
	 * @param b
	 * @param screenRadius
	 * @return Radius des Kreises erscheinend auf ScreenRadius
	 */
	public double calcErscheinungsGroesse(Betrachter b, double screenRadius) {
		calcDistanzZu(b.getX(), b.getY());
		return (double) radius / (double) (centerDistanz + radius) * screenRadius;
	}

	public double calcSichtwinkelTan() {
		return (radius * 2) / randDistanz;
	}

	public void calcDistanzZu(double x, double y) {
		this.centerDistanz = Math.sqrt(Math.pow(x - posX, 2) + Math.pow(y - posY, 2));
		this.randDistanz = Math.max(0, centerDistanz - radius);
	}

	@Override
	public int compareTo(Kreis o) {
		if (randDistanz < o.randDistanz) {
			return 1;
		} else if (randDistanz > o.randDistanz) {
			return -1;
		} else {
			return 0;
		}
	}

}
