package it.polito.tdp.covid.model;

import com.javadocmd.simplelatlng.LatLng;

public class Provincia {

	private String sigla;
	private String provincia;
	private Double superficie;
	private Double raggio;
	private int residenti;
	private int residentiVicini;
	private int numComuni;
	private int idRegione;
	private LatLng centro;
	private double density;
	
	public Provincia(String sigla, String provincia, 
			Double superficie, int residenti, int numComuni, int idRegione, double lat, double lng) {
		super();
		this.sigla = sigla;
		this.provincia = provincia;
		this.superficie = superficie;
		this.raggio = Math.sqrt(superficie/Math.PI);
		this.residenti = residenti;
		this.residentiVicini = 0;
		this.numComuni = numComuni;
		this.idRegione = idRegione;
		this.centro = new LatLng(lat, lng);
		this.density = ((double) residenti)/superficie;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public Double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Double superficie) {
		this.superficie = superficie;
	}

	public Double getRaggio() {
		return raggio;
	}

	public void setRaggio(Double raggio) {
		this.raggio = raggio;
	}

	public int getResidenti() {
		return residenti;
	}

	public void setResidenti(int residenti) {
		this.residenti = residenti;
	}

	public int getResidentiVicini() {
		return residentiVicini;
	}

	public void addResidentiVicini(int residentiVicino) {
		this.residentiVicini += residentiVicino;
	}

	public int getNumComuni() {
		return numComuni;
	}

	public void setNumComuni(int numComuni) {
		this.numComuni = numComuni;
	}

	public int getIdRegione() {
		return idRegione;
	}

	public void setIdRegione(int idRegione) {
		this.idRegione = idRegione;
	}

	public LatLng getCentro() {
		return centro;
	}

	public void setCentro(LatLng centro) {
		this.centro = centro;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	@Override
	public String toString() {
		return provincia;
	}
	
	public String descriviti() {
		return String.format("%s (%s) ab: %d, d: %.2f ab/kmq", provincia, sigla, residenti, density);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Provincia other = (Provincia) obj;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}
}
