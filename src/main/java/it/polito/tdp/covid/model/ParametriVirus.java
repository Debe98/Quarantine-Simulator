package it.polito.tdp.covid.model;

import java.util.*;

import it.polito.tdp.covid.model.ParametriVirus.HandlingType;
import it.polito.tdp.covid.stats.*;
import it.polito.tdp.covid.stats.StatisticheEnte.*;

public class ParametriVirus {
	
	public enum HandlingType {
		NO_ACTS, REDUCED, NO_SCHOOL, LOCKDOWN;
	}

	double parB0;
	private Map <Status, Double> avgTime;
	private Map <AgeGroup, Double> ageGroupFromContagiosoToMalato;
	private Map <AgeGroup, Double> ageGroupDeathsRatio;
	private Map <HandlingType, Double> schoolSpreadReductions;
	private Map <HandlingType, Double> workSpreadReductions;
	private Map <HandlingType, Double> retirementSpreadReductions;
	private Map <AgeGroup, Map <HandlingType, Double>> mapSpreadReductions;

	public ParametriVirus() {
		
		avgTime = new HashMap<>();
		
		ageGroupFromContagiosoToMalato = new HashMap<>();
		
		ageGroupDeathsRatio = new HashMap<>();
		
		schoolSpreadReductions = new HashMap<>();
		
		workSpreadReductions = new HashMap<>();
		
		retirementSpreadReductions = new HashMap<>();
		
		mapSpreadReductions = new HashMap<>();
		mapSpreadReductions.put(AgeGroup.SCUOLA, schoolSpreadReductions);
		mapSpreadReductions.put(AgeGroup.LAVORO, workSpreadReductions);
		mapSpreadReductions.put(AgeGroup.PENSIONE, retirementSpreadReductions);
		
		resetVirus();
		resetContromisure();
		
	}
	
	//TEMPORANEI; POI DA SISTEMARE CON IL DEFINIRSI DELLA CLASSE
	
	public void resetVirus() {
		parB0 = 0.05;			//Probabilità contagio da contatto a rischio
		avgTime.put(Status.CONTAGIOSO, 10.0);
		avgTime.put(Status.MALATO, 15.0);
		ageGroupFromContagiosoToMalato.put(AgeGroup.SCUOLA, 0.25);
		ageGroupFromContagiosoToMalato.put(AgeGroup.LAVORO, 0.55);
		ageGroupFromContagiosoToMalato.put(AgeGroup.PENSIONE, 0.8);
		ageGroupDeathsRatio.put(AgeGroup.SCUOLA, 0.001);
		ageGroupDeathsRatio.put(AgeGroup.LAVORO, 0.007);
		ageGroupDeathsRatio.put(AgeGroup.PENSIONE, 0.07);
	}
	
	public void resetContromisure() {
		schoolSpreadReductions.put(HandlingType.NO_ACTS, 1.0);
		schoolSpreadReductions.put(HandlingType.REDUCED, 0.3);
		schoolSpreadReductions.put(HandlingType.NO_SCHOOL, 0.1);
		schoolSpreadReductions.put(HandlingType.LOCKDOWN, 0.01);
		workSpreadReductions.put(HandlingType.NO_ACTS, 1.0);
		workSpreadReductions.put(HandlingType.REDUCED, 0.3);
		workSpreadReductions.put(HandlingType.NO_SCHOOL, 0.25);
		workSpreadReductions.put(HandlingType.LOCKDOWN, 0.01);
		retirementSpreadReductions.put(HandlingType.NO_ACTS, 1.0);
		retirementSpreadReductions.put(HandlingType.REDUCED, 0.6);
		retirementSpreadReductions.put(HandlingType.NO_SCHOOL, 0.65);
		retirementSpreadReductions.put(HandlingType.LOCKDOWN, 0.05);
	}

	public double getParB0() {
		return parB0;
	}

	public void setParB0(double parB0) throws Exception {
		if (parB0 < 0)
			throw new Exception("Parametro B0 inserito non valido!");
		this.parB0 = parB0;
	}

	public double getAvgTime(Status st) {
		return avgTime.get(st);
	}

	public void setAvgTime(Map<Status, Double> avgTime) throws Exception {
		for (Status st : this.avgTime.keySet()) {
			Double temp = avgTime.get(st);
			if (temp == null || temp < 0)
				throw new Exception("Parametro inserito per \""+st+"\"non valido!");
		}
		this.avgTime = avgTime;
	}
	
	public void setAvgTime(Status st, double avgTime) throws Exception {
		if (avgTime < 0)
			throw new Exception("Parametro inserito per \""+st+"\"non valido!");
		this.avgTime.put(st, avgTime);
	}

	public double getAgeGroupFromContagiosoToMalato(AgeGroup ag) {
		return ageGroupFromContagiosoToMalato.get(ag);
	}

	public void setAgeGroupFromContagiosoToMalato(Map<AgeGroup, Double> ageGroupFromContagiosoToMalato) throws Exception {
		for (AgeGroup ag : AgeGroup.values()) {
			Double temp = ageGroupFromContagiosoToMalato.get(ag);
			if (temp == null || temp > 1)
				throw new Exception("Parametro inserito per \""+ag+"\"non valido!");
		}
		this.ageGroupFromContagiosoToMalato = ageGroupFromContagiosoToMalato;
	}
	
	public void setAgeGroupFromContagiosoToMalato(AgeGroup ag, double value) throws Exception {
		if (value > 1)
			throw new Exception("Parametro inserito per \""+ag+"\"non valido!");
		this.ageGroupFromContagiosoToMalato.put(ag, value);
	}

	public double getAgeGroupDeathsRatio(AgeGroup ag) {
		return ageGroupDeathsRatio.get(ag);
	}

	public void setAgeGroupDeathsRatio(Map<AgeGroup, Double> ageGroupDeathsRatio) throws Exception {
		for (AgeGroup ag : AgeGroup.values()) {
			Double temp = ageGroupDeathsRatio.get(ag);
			if (temp == null || temp > 1)
				throw new Exception("Parametro inserito per \""+ag+"\"non valido!");
		}
		this.ageGroupDeathsRatio = ageGroupDeathsRatio;
	}
	
	public void setAgeGroupDeathsRatio(AgeGroup ag, double value) throws Exception {
		if (value > 1)
			throw new Exception("Parametro inserito per \""+ag+"\"non valido!");
		this.ageGroupDeathsRatio.put(ag, value);
	}
	
	public double getMapSpreadReductions(AgeGroup ag, HandlingType ht) {
		return mapSpreadReductions.get(ag).get(ht);
	}

	/*
	public Map<handlingType, Double> getSchoolSpreadReductions() {
		return schoolSpreadReductions;
	}

	public Map<handlingType, Double> getWorkSpreadReductions() {
		return workSpreadReductions;
	}

	public Map<handlingType, Double> getRetirementSpreadReductions() {
		return retirementSpreadReductions;
	}
	*/
	
	public void setSpreadReduction(AgeGroup ag, HandlingType ht, double value) throws Exception {
		if (value > 1)
			throw new Exception("Parametro inserito per \""+ag+"\" e \""+ht+"\"non valido!");
		mapSpreadReductions.get(ag).put(ht, value);
	}
}
