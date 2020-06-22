package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class District {

	private int districtId;
	private double lon;
	private double lat;
	private LatLng centro;
	
	public District(int districtId, double lon, double lat) {
		super();
		this.districtId = districtId;
		this.lon = lon;
		this.lat = lat;
		centro = new LatLng(lat, lon);
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public LatLng getCentro() {
		return centro;
	}

	public void setCentro(LatLng centro) {
		this.centro = centro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + districtId;
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
		District other = (District) obj;
		if (districtId != other.districtId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "#D: " + districtId;
	}
	
	
}
