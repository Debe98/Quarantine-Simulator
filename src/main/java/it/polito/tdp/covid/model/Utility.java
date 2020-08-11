package it.polito.tdp.covid.model;

import java.util.Map;

import it.polito.tdp.covid.model.TypedWeightedEdge.AggregationType;
import it.polito.tdp.covid.stats.StatisticheEnte;
import it.polito.tdp.covid.stats.StatisticheEnte.Status;

public class Utility {
	
	AggregationType agg;
	Status st;
	boolean isPercentuale;
	double valore;
	
	public Utility(AggregationType agg, Status st, boolean isPercentuale, double valore) {
		super();
		this.agg = agg;
		this.st = st;
		this.isPercentuale = isPercentuale;
		this.valore = valore;
	}

	public AggregationType getAgg() {
		return agg;
	}

	public Status getSt() {
		return st;
	}

	public boolean isPercentuale() {
		return isPercentuale;
	}

	public double getValore() {
		return valore;
	}
	
	public boolean corrisponde(Map <Status, Integer> valori, StatisticheEnte stats) {
		if (stats.getAgg() != agg)
			return false;
		if (isPercentuale) 
			if (((double) valori.get(st)) / stats.getTotPopolazione() >= valore)
				return true;
			else 
				return false;
		else
			if (valori.get(st) >= (int) Math.round(valore))
				return true;
			else
				return false;

	}

	@Override
	public String toString() {
		if (isPercentuale)
			return agg+" a " + (int)(valore*100) +"% popolazione "+st;
		else
			return agg+" a " + (int)valore +" "+st;
	}
	
	
}
