package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import exe.OV_View;

public class ObjektVerwaltung {

	private List<Kreis> kreise;

	private List<Kreis> entferntRelevanteKreise;
	private List<Kreis> entferntSichtbareKreise; // Entfernte, die nicht verdeckt sind
	private List<Kreis> direktSichtbareKreise;

	// Temporäre Listen während Erstellung
	private Object tempLock = new Object();
	private List<Kreis> entferntRelevanteKreiseTemp;
	private List<Kreis> entferntSichtbareKreiseTemp;
	private List<Kreis> direktSichtbareKreiseTemp;

	private double sichtAufloesung = 0.01;

	private int screenRadius = 400;

	private Betrachter b;

	private OV_View v;

	public ObjektVerwaltung(Betrachter b) {
		this.b = b;
		loadKreise();

		CalcRelevanzThread crt = new CalcRelevanzThread();
		crt.start();

		BetrachterUpdateThread but = new BetrachterUpdateThread();
		but.start();

	}

	public void updateOV() {
		b.update();
	}

	public void setView(OV_View v) {
		this.v = v;
	}

	public Betrachter getBetrachter() {
		return b;
	}

	private void loadKreise() {
		Random r = new Random();

		this.kreise = new ArrayList<>();
		this.entferntRelevanteKreise = new ArrayList<>();
		this.entferntSichtbareKreise = new ArrayList<>();
		this.direktSichtbareKreise = new ArrayList<>();
		this.entferntRelevanteKreiseTemp = new ArrayList<>();
		this.entferntSichtbareKreiseTemp = new ArrayList<>();
		this.direktSichtbareKreiseTemp = new ArrayList<>();

		long start = System.currentTimeMillis();

		// Geniere Kreise
		for (int i = 0; i < 10000000; i++) {
			kreise.add(new Kreis(r.nextInt(700000), r.nextInt(700000), 5 + r.nextInt(100),
					new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))));
		}

		System.out.println("Erzeug - Dauer: " + (System.currentTimeMillis() - start));

		// Berechne Relevante
		calcRelevanzen();

	}

	public void calcRelevanzen() {

		long start = System.currentTimeMillis();

		entferntRelevanteKreiseTemp.clear();
		entferntSichtbareKreiseTemp.clear();
		direktSichtbareKreiseTemp.clear();

		double metereologischeSichtweite = 1000;

		// Bestimme Relevanz aller Kreise
		for (Kreis k : kreise) {

			int relevanz = k.calcRelevanz(b, metereologischeSichtweite, sichtAufloesung, screenRadius);

			if (relevanz == 1) {
				entferntRelevanteKreiseTemp.add(k);
			} else if (relevanz == 2) {
				entferntRelevanteKreiseTemp.add(k);
				direktSichtbareKreiseTemp.add(k);
			} else if (relevanz == 3) {
				direktSichtbareKreiseTemp.add(k);
			}

		}
		System.out
				.println("Calc Relevanz - Dauer: " + (System.currentTimeMillis() - start) + " (" + kreise.size() + ")");

		long start_sort = System.currentTimeMillis();
		Collections.sort(direktSichtbareKreiseTemp);
		System.out.println("Calc Sort - Dauer: " + (System.currentTimeMillis() - start_sort) + " ("
				+ direktSichtbareKreiseTemp.size() + ")");

		// Calc Entferntsichtbare
		long start_entfernsichtbar = System.currentTimeMillis();
		Collections.sort(entferntRelevanteKreiseTemp); // nahe Kreise sind bei hohen Indizes
		entferntSichtbareKreiseTemp.addAll(entferntRelevanteKreiseTemp);

		System.out.println("Calc entferntsichtbare - Dauer: " + (System.currentTimeMillis() - start_entfernsichtbar)
				+ " (" + entferntRelevanteKreiseTemp.size() + ")");

		// Temps übertragen
		synchronized (tempLock) {
			long start_uebertrag = System.currentTimeMillis();
			direktSichtbareKreise.clear();
			direktSichtbareKreise.addAll(direktSichtbareKreiseTemp);
			entferntSichtbareKreise.clear();
			entferntSichtbareKreise.addAll(entferntSichtbareKreiseTemp);
			entferntRelevanteKreise.clear();
			entferntRelevanteKreise.addAll(entferntRelevanteKreiseTemp);
			System.out.println("Calc Übertrag - Dauer: " + (System.currentTimeMillis() - start_uebertrag));
		}

	}

	public void draw(Graphics2D g) {

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

		long start = System.currentTimeMillis();

		// Entfernte Kreise

		synchronized (tempLock) {

			double entferntleistenHoehe = 30;

			// Schablone für runden Screen
			Ellipse2D aussenloch = new Ellipse2D.Double(b.getX() - screenRadius, b.getY() - screenRadius,
					2 * (screenRadius), 2 * (screenRadius));
			g.setClip(aussenloch);

			// die nächsten X Entfernten anzeigen:
			for (int i = entferntSichtbareKreise.size() - 100; i < entferntSichtbareKreise.size(); i++) {
				if (i >= 0) {
					entferntSichtbareKreise.get(i).drawEntfernt(g, b, screenRadius, entferntleistenHoehe);
				}
			}

			// Hauptfeld clearen
			g.setColor(Color.WHITE);
			g.fillOval((int) (b.getX() - screenRadius + entferntleistenHoehe),
					(int) (b.getY() - screenRadius + entferntleistenHoehe),
					(int) (2 * (screenRadius - entferntleistenHoehe)),
					(int) ((screenRadius - entferntleistenHoehe) * 2));
			
			// Nahe Kreise
			for (Kreis k : direktSichtbareKreise) {
				k.draw(g, b, screenRadius);
			}

		}

		System.out.println("Entfernte Kreise: " + entferntSichtbareKreise.size());
		System.out.println("Nahe Kreise: " + direktSichtbareKreise.size());
		System.out.println("Paint-Dauer: " + (System.currentTimeMillis() - start));

	}

	public void changeSichtaufloesung(double r) {

		if (r > 0) {
			sichtAufloesung *= 1.1;
		} else {
			sichtAufloesung /= 1.1;
		}
		System.out.println(sichtAufloesung);

		calcRelevanzen();

		if (v != null) {
			v.updateUI();
		}

	}

	class BetrachterUpdateThread extends Thread {

		public BetrachterUpdateThread() {

			Timer t = new Timer();
			TimerTask tt = new TimerTask() {

				@Override
				public void run() {
					updateOV();
				}
			};
			t.scheduleAtFixedRate(tt, 0, 30);
		}

	}

	class CalcRelevanzThread extends Thread {
		@Override
		public void run() {
			while (true) {
				calcRelevanzen();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
