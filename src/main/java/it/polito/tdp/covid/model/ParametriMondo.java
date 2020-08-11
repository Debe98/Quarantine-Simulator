package it.polito.tdp.covid.model;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.covid.model.TypedWeightedEdge.AggregationType;
import it.polito.tdp.covid.stats.StatisticheEnte.AgeGroup;

public class ParametriMondo {

	private Map <AgeGroup, Double> ageGroupPercentages;
	private Map <AggregationType, Map <AgeGroup, Double>> percentualeMobilitaContainer;
	private Map <AgeGroup, Double> percentualeMobilitaComuni;
	private Map <AgeGroup, Double> percentualeMobilitaProvince;
	private Map <AgeGroup, Double> percentualeMobilitaRegioni;
	private double indiceDiAdiacenza;
	
	public ParametriMondo() {
		indiceDiAdiacenza = 1.5;
		
		ageGroupPercentages = new HashMap<>();
		ageGroupPercentages.put(AgeGroup.SCUOLA, 0.2);
		ageGroupPercentages.put(AgeGroup.LAVORO, 0.55);
		ageGroupPercentages.put(AgeGroup.PENSIONE, 0.25);
		
		percentualeMobilitaContainer = new HashMap<>();
		percentualeMobilitaComuni = new HashMap<>();
		percentualeMobilitaProvince = new HashMap<>();
		percentualeMobilitaRegioni = new HashMap<>();
		
		percentualeMobilitaComuni.put(AgeGroup.SCUOLA, 0.02);
		percentualeMobilitaComuni.put(AgeGroup.LAVORO, 0.05);
		percentualeMobilitaComuni.put(AgeGroup.PENSIONE, 0.05);
		
		percentualeMobilitaProvince.put(AgeGroup.SCUOLA, 0.02);
		percentualeMobilitaProvince.put(AgeGroup.LAVORO, 0.05);
		percentualeMobilitaProvince.put(AgeGroup.PENSIONE, 0.05);
		
		percentualeMobilitaRegioni.put(AgeGroup.SCUOLA, 0.02);
		percentualeMobilitaRegioni.put(AgeGroup.LAVORO, 0.05);
		percentualeMobilitaRegioni.put(AgeGroup.PENSIONE, 0.05);
		
		percentualeMobilitaContainer.put(AggregationType.COMUNE, percentualeMobilitaComuni);
		percentualeMobilitaContainer.put(AggregationType.PROVINCIA, percentualeMobilitaProvince);
		percentualeMobilitaContainer.put(AggregationType.REGIONE, percentualeMobilitaRegioni);
	}

	public double getAgeGroupPercentages(AgeGroup ag) {
		return ageGroupPercentages.get(ag);
	}

	public void setAgeGroupPercentages(Map<AgeGroup, Double> ageGroupPercentages) throws Exception {
		double sum = 0.0;
		for (AgeGroup ag : AgeGroup.values()) {
			Double temp = ageGroupPercentages.get(ag);
			if (temp == null || temp > 1)
				throw new Exception("Parametro inserito per \""+ag+"\"non valido.");
			sum += temp;
		}
		if (sum != 1)
			throw new Exception("Le probabilità devono sommare a 1.");
		this.ageGroupPercentages = ageGroupPercentages;
	}

	public double getIndiceDiAdiacenza() {
		return indiceDiAdiacenza;
	}

	public void setIndiceDiAdiacenza(double indiceDiAdiacenza) throws Exception {
		if (indiceDiAdiacenza < 0)
			throw new Exception("Indice di adiacenza non valido.");
		this.indiceDiAdiacenza = indiceDiAdiacenza;
	}

	public double getPercentualeMobilita(AggregationType agg, AgeGroup ag) {
		return percentualeMobilitaContainer.get(agg).get(ag);
	}

	public void setPercentualeMobilita(AggregationType agg, AgeGroup ag, double perc) {
		percentualeMobilitaContainer.get(agg).put(ag, perc);
	}

	public Map <AgeGroup, Double> getPercentualeImmobilita() {
		Map <AgeGroup, Double> ritorno = new HashMap<>();
		for (AgeGroup ag : AgeGroup.values()) {
			double perc = 0.0;
			for (Map <AgeGroup, Double> mappa : percentualeMobilitaContainer.values()) {
				perc += mappa.get(ag);
			}
			ritorno.put(ag, 1- perc);
		}
		return ritorno;
	}
	
	
	
}
