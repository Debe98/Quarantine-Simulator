package it.polito.tdp.covid.model;

import com.javadocmd.simplelatlng.LatLng;

public class Comune {
	
	private int istat;
	private String comune;
	private String regione;
	private String provincia;
	private double superficie;
	private double raggio;
	private int numResidenti;
	private int numResidentiVicini;
	private LatLng centro;
	private double density;
	
	public Comune(int istat, String comune, String regione, String provincia,
			double superficie, int numResidenti, double lat, double lng) {
		super();
		this.istat = istat;
		this.comune = comune;
		this.regione = regione;
		this.provincia = provincia;
		this.superficie = superficie;
		this.raggio = Math.sqrt(superficie/Math.PI);
		this.numResidenti = numResidenti;
		this.numResidentiVicini = 0;
		this.centro = new LatLng(lat, lng);
		this.density = ((double) numResidenti)/superficie;
	}

	public int getIstat() {
		return istat;
	}

	public void setIstat(int istat) {
		this.istat = istat;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(double superficie) {
		this.superficie = superficie;
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

	public int getNumResidentiVicini() {
		return numResidentiVicini;
	}

	public void addNumResidentiVicini(int numResidentiVicino) {
		this.numResidentiVicini += numResidentiVicino;
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
		return comune;
	}
	
	public String descriviti() {
		return String.format("%s (%d) ab: %d, d: %.2f ab/kmq, %s, %s", 
				comune, istat, numResidenti, density, provincia, regione);
		}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + istat;
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
		Comune other = (Comune) obj;
		if (istat != other.istat)
			return false;
		return true;
	}
}
