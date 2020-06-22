package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Evento implements Comparable <Evento> {

	public enum EventType {
		ARRIVO_LUOGO, FINE_EVENTO
	}

	private EventType type ;
	private LocalDateTime tempo ;
	private Event daGestire;
	
	//COSTRUTTORE, GETTERS E SETTERS
	
	

	@Override
	public int compareTo(Evento o) {
		return this.tempo.compareTo(o.tempo);
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public LocalDateTime getTempo() {
		return tempo;
	}

	public void setTempo(LocalDateTime tempo) {
		this.tempo = tempo;
	}

	public Event getDaGestire() {
		return daGestire;
	}

	public void setDaGestire(Event daGestire) {
		this.daGestire = daGestire;
	}

	public Evento(EventType type, LocalDateTime tempo, Event daGestire) {
		super();
		this.type = type;
		this.tempo = tempo;
		this.daGestire = daGestire;
	}
}