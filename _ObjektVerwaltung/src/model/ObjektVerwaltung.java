package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.listener.EUpdateTopic;
import model.listener.UpdateListener;
import view.OV_ViewContainer;

public class ObjektVerwaltung {

	private List<KreisObjekt> kreise;

	private List<KreisObjekt> entferntRelevanteKreise;
	private List<KreisObjekt> entferntSichtbareKreise; // Entfernte, die nicht verdeckt sind
	private List<KreisObjekt> direktSichtbareKreise;

	// Tempor?re Listen w?hrend Erstellung
	private Object addKreisLock = new Object();
	private Object realLock = new Object();
	private List<KreisObjekt> entferntRelevanteKreiseTemp;
	private List<KreisObjekt> entferntSichtbareKreiseTemp;
	private List<KreisObjekt> direktSichtbareKreiseTemp;

	private double sichtAufloesung = 0.01;

	private int screenRadius = 400;

	private Betrachter b;

	private OV_ViewContainer v;

	private long lastUpdate;

	private HashMap<EUpdateTopic, List<UpdateListener>> listeners;

	public ObjektVerwaltung(Betrachter b) {
		this.b = b;

		this.kreise = new ArrayList<>();
		this.entferntRelevanteKreise = new ArrayList<>();
		this.entferntSichtbareKreise = new ArrayList<>();
		this.direktSichtbareKreise = new ArrayList<>();
		this.entferntRelevanteKreiseTemp = new ArrayList<>();
		this.entferntSichtbareKreiseTemp = new ArrayList<>();
		this.direktSichtbareKreiseTemp = new ArrayList<>();

		this.listeners = new HashMap<>();

		CalcRelevanzThread crt = new CalcRelevanzThread();
		crt.start();

		BetrachterUpdateThread but = new BetrachterUpdateThread();
		but.start();

	}

	public void updateOV() {
		if (v != null) {
			v.updateUI();
		}
		long dt = System.nanoTime() - lastUpdate;
		b.update(dt);
		lastUpdate = System.nanoTime();
	}

	public void setView(OV_ViewContainer v) {
		this.v = v;
	}

	public Betrachter getBetrachter() {
		return b;
	}

	public void addKreis(KreisObjekt k) {
		synchronized (addKreisLock) {
			kreise.add(k);
		}
	}

	public void calcRelevanzen() {

		long start = System.currentTimeMillis();

		entferntRelevanteKreiseTemp.clear();
		entferntSichtbareKreiseTemp.clear();
		direktSichtbareKreiseTemp.clear();

		double metereologischeSichtweite = 1200;

		// Bestimme Relevanz aller Kreise
		for (KreisObjekt k : kreise) {

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

		// long start_sort = System.currentTimeMillis();
		Collections.sort(direktSichtbareKreiseTemp);
		// System.out.println("Calc Sort - Dauer: " + (System.currentTimeMillis() -
		// start_sort) + " (" + direktSichtbareKreiseTemp.size() + ")");

		// Calc Entferntsichtbare
		// long start_entfernsichtbar = System.currentTimeMillis();
		Collections.sort(entferntRelevanteKreiseTemp); // nahe Kreise sind bei hohen Indizes
		entferntSichtbareKreiseTemp.addAll(entferntRelevanteKreiseTemp);

		// System.out.println("Calc entferntsichtbare - Dauer: " +
		// (System.currentTimeMillis() - start_entfernsichtbar) + " ("
		// + entferntRelevanteKreiseTemp.size() + ")");

		// Temps ?bertragen
		synchronized (realLock) {
			// long start_uebertrag = System.currentTimeMillis();
			direktSichtbareKreise.clear();
			direktSichtbareKreise.addAll(direktSichtbareKreiseTemp);
			entferntSichtbareKreise.clear();
			entferntSichtbareKreise.addAll(entferntSichtbareKreiseTemp);
			entferntRelevanteKreise.clear();
			entferntRelevanteKreise.addAll(entferntRelevanteKreiseTemp);

			// System.out.println("Calc ?bertrag - Dauer: " + (System.currentTimeMillis() -
			// start_uebertrag));
		}

		if (listeners != null) {
			if (listeners.containsKey(EUpdateTopic.RELEVANZEN)) {
				for (UpdateListener ul : listeners.get(EUpdateTopic.RELEVANZEN)) {
					ul.handleOVUpdate(EUpdateTopic.RELEVANZEN);
				}
			}
		}

	}

	public void draw(Graphics2D g) {

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

//		long start = System.currentTimeMillis();

		// Entfernte Kreise

		synchronized (realLock) {

			double entferntleistenHoehe = 30;

			// Schablone f?r runden Screen
			Ellipse2D aussenloch = new Ellipse2D.Double(b.getX() - screenRadius, b.getY() - screenRadius,
					2 * (screenRadius), 2 * (screenRadius));
			g.setClip(aussenloch);

			// die n?chsten X Entfernten anzeigen:
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
			for (KreisObjekt k : direktSichtbareKreise) {
				k.draw(g, b, screenRadius);
			}

		}

		// System.out.println("Entfernte Kreise: " + entferntSichtbareKreise.size());
		// System.out.println("Nahe Kreise: " + direktSichtbareKreise.size());
		// System.out.println("Paint-Dauer: " + (System.currentTimeMillis() - start));

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

		public CalcRelevanzThread() {
			Timer t = new Timer();
			TimerTask tt = new TimerTask() {

				@Override
				public void run() {
					synchronized (addKreisLock) {
						calcRelevanzen();
					}
				}
			};
			t.scheduleAtFixedRate(tt, 0, 1000);
		}

	}

	public void addUpdateListener(EUpdateTopic topic, UpdateListener ul) {
		if (listeners != null) {
			if (!listeners.containsKey(topic)) {
				listeners.put(topic, new ArrayList<>());
			}
			listeners.get(topic).add(ul);
		}
	}

	public List<KreisObjekt> getDirektSichtbareKreise() {
		return direktSichtbareKreise;
	}
}
