package it.polito.tdp.covid.model;

import java.util.*;

import it.polito.tdp.covid.model.ParametriVirus.HandlingType;
import it.polito.tdp.covid.model.TypedWeightedEdge.*;
import it.polito.tdp.covid.stats.StatisticheEnte;
import it.polito.tdp.covid.stats.StatisticheEnte.Status;

public class ParametriSimulazione {
	
	public enum SimulationType {
		LIVE_INTERACTION, PRE_PARAMETERIZED;
	}
	
	public enum ChanceType {
		SEMI_DETERMINISTICA, STOCASTICA;
	}
	
	public enum ReturnCondition {
		FINE_EPIDEMIA, ESTINZIONE_VIRUS, NUM_GIORNI, NUM_MALATI, PER_MALATI, NUM_MORTI, PER_MORTI;
	}

	private AggregationType aggSimulazione;
	private SimulationType tipoSimulazione;
	private ChanceType tipoProbabilita;
	private ReturnCondition hopCondition;
	private int numGiorniHop;
	private int numStatoHop;
	private double perStatoHop;
	//PRE_PARAMETER!!
	private Map <AggregationType, Utility> chiusura;
	private Map <AggregationType, Map <HandlingType, Utility>> contromisure;
	private List <HandlingType> utilHT;
	private List <String> report;
	

	public ParametriSimulazione() {
		super();
		aggSimulazione = AggregationType.REGIONE;
		tipoSimulazione = SimulationType.PRE_PARAMETERIZED;
		tipoProbabilita = ChanceType.SEMI_DETERMINISTICA;
		hopCondition = ReturnCondition.ESTINZIONE_VIRUS;
		numGiorniHop = 5;
		numStatoHop = 10000;
		perStatoHop = 0.1;
		chiusura = new HashMap<>();
		contromisure = new HashMap<>();
		utilHT = new LinkedList<>();
		utilHT.add(HandlingType.LOCKDOWN);
		utilHT.add(HandlingType.NO_SCHOOL);
		utilHT.add(HandlingType.REDUCED);
		report = new LinkedList<String>();
		//chiusura.put(AggregationType.COMUNE, new Utility(AggregationType.COMUNE, Status.MALATO, true, 0.05));
	}

	public AggregationType getAggSimulazione() {
		return aggSimulazione;
	}

	public void setAggSimulazione(AggregationType aggSimulazione) {
		this.aggSimulazione = aggSimulazione;
	}

	public SimulationType getTipoSimulazione() {
		return tipoSimulazione;
	}

	public void setTipoSimulazione(SimulationType tipoSimulazione) {
		this.tipoSimulazione = tipoSimulazione;
	}

	public ChanceType getTipoProbabilita() {
		return tipoProbabilita;
	}

	public void setTipoProbabilita(ChanceType tipoProbabilita) {
		this.tipoProbabilita = tipoProbabilita;
	}

	public ReturnCondition getHopCondition() {
		return hopCondition;
	}

	public void setHopCondition(ReturnCondition hopCondition) {
		this.hopCondition = hopCondition;
	}

	public int getNumGiorniHop() {
		return numGiorniHop;
	}

	public void setNumGiorniHop(int numGiorni) {
		this.numGiorniHop = numGiorni;
	}

	public int getNumStatoHop() {
		return numStatoHop;
	}

	public void setNumStatoHop(int numStato) {
		this.numStatoHop = numStato;
	}

	public double getPerStatoHop() {
		return perStatoHop;
	}

	public void setPerStatoHop(double perStato) {
		this.perStatoHop = perStato;
	}
	
	//PRE_PARAMETER

	public boolean verChiusura(Map <Status, Integer> valori, StatisticheEnte stats, int giornoCorrente, boolean giaChiuso) {
		Utility u = chiusura.get(stats.getAgg());
		if (u == null)
			return false;
		else {
			boolean b = u.corrisponde(valori, stats);
			if (b && !giaChiuso) 
				report.add("Giorno: "+giornoCorrente+" - "+stats.getRef()+" ("+stats.getAgg()+"): CHIUSO");
			else if (!b && giaChiuso)
				report.add("Giorno: "+giornoCorrente+" - "+stats.getRef()+" ("+stats.getAgg()+"): APERTO");
			return b;
		}
	}
	
	public void addChiusura(Utility chius) {
		chiusura.put(chius.getAgg(), chius);
	}
	
	public void removeChiusura(AggregationType agg) {
		chiusura.remove(agg);
	}
	
	public void changeHandingType(Map <Status, Integer> valori, StatisticheEnte stats, int giornoCorrente) {
		HandlingType presente = stats.getHandling();
		/* SE NON VOLESSI DIMINUIRE
		if (presente == HandlingType.LOCKDOWN)
			return;
			*/
		Map <HandlingType, Utility> temp = contromisure.get(stats.getAgg());
		if (temp == null)
			return;
		for (HandlingType ht : utilHT) {
			Utility u = temp.get(ht);
			if (u != null && u.corrisponde(valori, stats)) {
				if (ht != presente) {
					report.add("Giorno: "+giornoCorrente+" - "+stats.getRef()+" ("+stats.getAgg()+"): "+ht);
					stats.setHandling(ht);
				}
				return;
			}
		}
		if (HandlingType.NO_ACTS != presente) {
			report.add("Giorno: "+giornoCorrente+" - "+stats.getRef()+" ("+stats.getAgg()+"): "+HandlingType.NO_ACTS);
			stats.setHandling(HandlingType.NO_ACTS);
		}
	}
	
	public void addHandingType(HandlingType ht, Utility chius) {
		Map <HandlingType, Utility> temp = contromisure.get(chius.getAgg());
		if (temp == null) {
			temp = new HashMap<>();
			contromisure.put(chius.getAgg(), temp);
		}
		temp.put(ht, chius);
	}
	
	public void removeHandling(AggregationType agg, HandlingType ht) {
		contromisure.get(agg).remove(ht);
	}

	public List<String> getReport() {
		return report;
	}
	
	public List <String> getHandList() {
		LinkedList<String> temp = new LinkedList<String>();
		for (AggregationType agg : contromisure.keySet()) {
			for (HandlingType ht : contromisure.get(agg).keySet()) {
				temp.add("Imposta "+ht+" "+contromisure.get(agg).get(ht)+"\n");
			}
		}
		return temp;
	}
	
	public List <String> getChiusure() {
		LinkedList<String> temp = new LinkedList<String>();
		for (AggregationType agg : chiusura.keySet()) {
			temp.add("Chiudi "+chiusura.get(agg)+"\n");
		}
		return temp;
	}
	
	public void resetPrePar() {
		chiusura = new HashMap<>();
		contromisure = new HashMap<>();
	}
	
	public void resetToNewSimulation() {
		resetPrePar();
		report = new LinkedList<String>();
	}
}
