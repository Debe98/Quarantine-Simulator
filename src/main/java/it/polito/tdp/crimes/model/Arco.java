package it.polito.tdp.crimes.model;

public class Arco implements Comparable <Arco> {
	District vertice1;
	District vertice2;
	Double distanza;
	
	public Arco(District vertice1, District vertice2, Double distanza) {
		super();
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.distanza = distanza;
	}

	public District getVertice1() {
		return vertice1;
	}

	public void setVertice1(District vertice1) {
		this.vertice1 = vertice1;
	}

	public District getVertice2() {
		return vertice2;
	}

	public void setVertice2(District vertice2) {
		this.vertice2 = vertice2;
	}

	public Double getDistanza() {
		return distanza;
	}

	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}

	@Override
	public int compareTo(Arco o) {
		// TODO Auto-generated method stub
		return this.distanza.compareTo(o.distanza);
	}

	@Override
	public String toString() {
		return vertice1 + " - " + vertice2 + ", " + String.format("%.2f km.", distanza);
	}
	
	
}
