package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulatore {
	private PriorityQueue<Evento> queue = new PriorityQueue<>();

	// VARIABILI
	private Graph <District, DefaultWeightedEdge> grafo;
	private List <Event> eventi;
	private int distrettoBase;
	private int eventiMalGestiti;
	private Integer n;
	private int i;
	private Map <Integer, District> distretti;
	// SETTER PER I PARAMETRI ESTERNI
	
	public void setGrafo(Graph <District, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
		distretti = new HashMap<Integer, District>();
		for (District d : grafo.vertexSet()) {
			distretti.put(d.getDistrictId(), d);
		}
	}
	
	public void init(Integer n, List <Event> eventi, Integer baseDistretto) {
		this.eventi = eventi;
		this.distrettoBase = baseDistretto;
		this.n = n;
	}
	
	// GETTER PER I RISULTATI
	
	
	// FUNZIONI DI UTILITA'
	

	// SIMULAZIONE VERA E PROPRIA

	public void run() {
		// preparazione iniziale (mondo + coda eventi)
		queue = new PriorityQueue<Evento>();
		District centro = null;
		eventiMalGestiti = 0;
		i = 0;
		
		centro = distretti.get(distrettoBase);
		
		if (centro == null || grafo == null) {
			return;
		}
		
		// inizializzazione coda eventi
		
		for (i = 0; i < Math.min(n, eventi.size()) ; i++) {
			Event ev = eventi.get(i);
			District destinazione = distretti.get(ev.getDistrict_id());
			DefaultWeightedEdge e = grafo.getEdge(centro, destinazione);
			Duration tempo = Duration.of( (long) grafo.getEdgeWeight(e), ChronoUnit.MINUTES);
			Evento evento = new Evento(EventType.ARRIVO_LUOGO, ev.getReported_date().plus(tempo), ev);
			queue.add(evento);
		}
		
		// esecuzione del ciclo di simulazione
		while(!queue.isEmpty()) {
			Evento e = this.queue.poll();
			//System.out.println(e);
			processEvent(e);
		}
		
	}
		
	private void processEvent(Evento e) {
		switch(e.getType()) {

		case ARRIVO_LUOGO:
			if(e.getDaGestire().getReported_date().plusMinutes(15).isBefore(e.getTempo())) {
				eventiMalGestiti++;
			}
			int ore = 2;
			if (e.getDaGestire().getOffense_category_id().equals("all-other-crimes")) {
				if (Math.random() > 0.5) {
					ore = 1;
				}
			}
			queue.add( new Evento(EventType.FINE_EVENTO, e.getTempo().plus(Duration.of(ore, ChronoUnit.HOURS)), e.getDaGestire()));
			break;

		case FINE_EVENTO:
			if (i < eventi.size()) {
				Event ev = eventi.get(i);
				i++;
				District destinazione = distretti.get(ev.getDistrict_id());
				District sorgente = distretti.get(e.getDaGestire().getDistrict_id());
				DefaultWeightedEdge arco = grafo.getEdge(sorgente, destinazione);
				Duration tempo;
				if (sorgente.equals(destinazione))
					tempo = Duration.of(0, ChronoUnit.MINUTES);
				else
					tempo = Duration.of( (long) grafo.getEdgeWeight(arco), ChronoUnit.MINUTES);
				queue.add(new Evento(EventType.ARRIVO_LUOGO, e.getTempo().plus(tempo), ev));
			}
			break;
		}

	}
	
	public List <Integer> getStats() {
		List<Integer> elementi = new LinkedList<Integer>();
		
		elementi.add(eventiMalGestiti);
		elementi.add(eventi.size());
		elementi.add(i);
		
		return elementi;
	}
}
