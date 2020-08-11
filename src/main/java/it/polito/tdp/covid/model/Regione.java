package it.polito.tdp.covid.model;

import com.javadocmd.simplelatlng.LatLng;

public class Regione {
	private int idRegione;
	private String regione;
	private double superficie;
	private double raggio;
	private int numResidenti;
	private int numComuni;
	private int numProvincie;
	private String capoluogo;
	private LatLng centro;
	private double density;
	
	public Regione(int idRegione, String regione, double superficie, int numResidenti,
			int numComuni, int numProvincie, String capoluogo, double lat, double lng) {
		super();
		this.idRegione = idRegione;
		this.regione = regione;
		this.superficie = superficie;
		this.raggio = Math.sqrt(superficie/Math.PI);
		this.numResidenti = numResidenti;
		this.numComuni = numComuni;
		this.numProvincie = numProvincie;
		this.capoluogo = capoluogo;
		this.centro = new LatLng(lat, lng);
		this.density = ((double) numResidenti)/superficie;
	}

	public int getIdRegione() {
		return idRegione;
	}

	public void setIdRegione(int idRegione) {
		this.idRegione = idRegione;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		regione = regione;
	}

	public double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(double superfice) {
		this.superficie = superfice;
	}

	public double getRaggio() {
		return raggio;
	}

	public void setRaggio(double raggio) {
		this.raggio = raggio;
	}

	public int getNumResidenti() {
		return numResidenti;
	}

	public void setNumResidenti(int numResidenti) {
		this.numResidenti = numResidenti;
	}

	public int getNumComuni() {
		return numComuni;
	}

	public void setNumComuni(int numComuni) {
		this.numComuni = numComuni;
	}

	public int getNumProvincie() {
		return numProvincie;
	}

	public void setNumProvincie(int numProvincie) {
		this.numProvincie = numProvincie;
	}

	public LatLng getCentro() {
		return centro;
	}

	public void setCentro(LatLng centro) {
		this.centro = centro;
	}

	public String getCapoluogo() {
		return capoluogo;
	}

	public void setCapoluogo(String capoluogo) {
		this.capoluogo = capoluogo;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	@Override
	public String toString() {
		return regione;
	}
	
	public String descriviti() {
		return String.format("%s (%d) ab: %d, d: %.2f ab/kmq [%s]", regione, idRegione, numResidenti, density, capoluogo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRegione;
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
		Regione other = (Regione) obj;
		if (idRegione != other.idRegione)
			return false;
		return true;
	}
}
