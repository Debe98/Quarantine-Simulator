package it.polito.tdp.crimes.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	private Graph <District, DefaultWeightedEdge> grafo;
	private Simulatore sim;

	public Model() {
		dao = new EventsDao();
		sim = new Simulatore();
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Get Vertici
		Graphs.addAllVertices(grafo, dao.getVertex(anno));
		System.out.println("#Vertici: "+grafo.vertexSet().size());
		
		for (District d : grafo.vertexSet()) {
			for (District d2 : grafo.vertexSet()) {
				if(d.getDistrictId() < d2.getDistrictId()) {
					LatLng point1 = new LatLng(d.getLat(), d.getLon());
					LatLng point2 = new LatLng(d2.getLat(), d2.getLon());
					
					if (grafo.containsVertex(d) && grafo.containsVertex(d2)) {
						
						if (!grafo.containsEdge(d, d2)) {
							Graphs.addEdge(grafo, d, d2, LatLngTool.distance(point1, point2, LengthUnit.KILOMETER));
						}
					}
				}
			}
		}
		
		System.out.println("#Archi-post: "+grafo.edgeSet().size());
	}
	
	public List <Integer> getDistinctAnni () {
		return dao.listAllYears();
	}
	
	public int simula(Integer giorno, Integer mese, Integer anno, Integer n) {
		if(this.grafo != null) {
			sim.setGrafo(this.grafo);
			Integer distretto = dao.getDistrettoBase(anno);
			if (distretto == null)
				return -1;
			List <Event> eventiCriminosi = dao.getCrimini(giorno, mese, anno);
			if (eventiCriminosi.isEmpty())
				return -1;
			sim.init(n, eventiCriminosi, distretto);
			sim.run();
			return 1;
		}
		return -1;
	}

	public List <Arco> getDistanzeVertici() {
		List <Arco> tutti = new LinkedList<>();
		for (District d1 : grafo.vertexSet()) {
			List <Arco> questo = new LinkedList<>();
			for (District d2 : Graphs.neighborListOf(grafo, d1)) {
				if (!d1.equals(d2)) {
					DefaultWeightedEdge e = grafo.getEdge(d1, d2);
					questo.add(new Arco (d1, d2, grafo.getEdgeWeight(e)));
				}
			}
			questo.sort(null);
			tutti.addAll(questo);
		}
		return tutti;
	}
	
	public List <Integer> getStatSim() {
		return sim.getStats();
	}
}
