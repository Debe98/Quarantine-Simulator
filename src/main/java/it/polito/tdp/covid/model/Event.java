package it.polito.tdp.covid.model;

import java.util.Map;

import it.polito.tdp.covid.model.TypedWeightedEdge.AggregationType;
import it.polito.tdp.covid.stats.StatisticheEnte.AgeGroup;
import it.polito.tdp.covid.stats.StatisticheEnte.Status;

public class Event implements Comparable <Event>{
	
	public enum EventType {
		NUOVO_GIORNO, STOP, TERMINA;
	}
	
	private EventType type;
	private int giorno;
	
	public Event(EventType type, int giorno) {
		super();
		this.type = type;
		this.giorno = giorno;
	}
	
	public EventType getType() {
		return type;
	}
	
	public void setType(EventType type) {
		this.type = type;
	}
	
	public int getGiorno() {
		return giorno;
	}
	
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	private int ordinator() {
		switch (type) {
		case NUOVO_GIORNO:
			return 1;
		case TERMINA:
			return 2;
		case STOP:
			return 3;
		}
		return 4;
	}
	
	@Override
	public int compareTo(Event o) {
		if (this.giorno != o.giorno)
			return this.giorno - o.giorno;
		else {
			return this.ordinator() - o.ordinator();
		}
	}
	
	
}
