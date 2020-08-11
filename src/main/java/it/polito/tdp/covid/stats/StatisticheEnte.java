package it.polito.tdp.covid.stats;

import java.util.*;

import it.polito.tdp.covid.model.*;
import it.polito.tdp.covid.model.ParametriVirus.*;
import it.polito.tdp.covid.model.TypedWeightedEdge.*;
import it.polito.tdp.covid.stats.StatisticheEnte.AgeGroup;
import it.polito.tdp.covid.stats.StatisticheEnte.Status;
import javafx.scene.chart.XYChart;

public class StatisticheEnte {
	
	public enum AgeGroup {
		SCUOLA, LAVORO, PENSIONE;
	}
	public enum Status {
		SANO, CONTAGIOSO, GUARITO, MALATO, CURATO, MORTO;
	}
	
	private String ref;
	private AggregationType agg;
	private Map <AgeGroup, HandlingType> handlingAge;
	
	private int nRow;
	private int nCol;
	
	private Map <AgeGroup, Integer> rowAgeGroup;
	private Map <Status, Integer> colStatus;
	
	private Integer[/*#R*/][/*#C*/] informazioniEnte;
	private Double[/*#R*/][/*#C*/] informazioniVicini;
	private int totPopolazione;
	
	private Integer giornoArrivoContagio;

	private Map <Status, XYChart.Series <String, Number>> nuoviContainer;
	private XYChart.Series <String, Number> nuoviContagiati;
	private XYChart.Series <String, Number> nuoviMalati;
	private XYChart.Series <String, Number> nuoviMorti;
	
	private Map <Status, Integer> ultimaStampa;
	private int giorniAgg;
	
	private Map <Status, Integer> tempValues;
	private Map <Status, Integer> maxValues;
	private Map <Status, Integer> maxDates;
	
	public StatisticheEnte(String ref, AggregationType agg) {
		super();
		this.ref = ref;
		this.agg = agg;
		createMatrix();
	}

	private void createMatrix() {
		rowAgeGroup = new HashMap<StatisticheEnte.AgeGroup, Integer>();
		rowAgeGroup.put(AgeGroup.SCUOLA, 0);
		rowAgeGroup.put(AgeGroup.LAVORO, 1);
		rowAgeGroup.put(AgeGroup.PENSIONE, 2);
		nRow = rowAgeGroup.size();
		
		colStatus = new HashMap<StatisticheEnte.Status, Integer>();
		colStatus.put(Status.SANO, 0);
		colStatus.put(Status.CONTAGIOSO, 1);
		colStatus.put(Status.GUARITO, 2);
		colStatus.put(Status.MALATO, 3);
		colStatus.put(Status.CURATO, 4);
		colStatus.put(Status.MORTO, 5);
		nCol = colStatus.size();
		
		informazioniEnte = new Integer[nRow][nCol];
		informazioniVicini = new Double[nRow][nCol];
		totPopolazione = 0;
		
		handlingAge = new HashMap<>();
		handlingAge.put(AgeGroup.SCUOLA, HandlingType.NO_ACTS);
		handlingAge.put(AgeGroup.LAVORO, HandlingType.NO_ACTS);
		handlingAge.put(AgeGroup.PENSIONE, HandlingType.NO_ACTS);
		
		giorniAgg = 1;
		giornoArrivoContagio = null;
		
		nuoviContainer = new HashMap<>();
		nuoviContagiati = new XYChart.Series <> ();
		nuoviContagiati.setName("Contagiati ("+giorniAgg+"gg)");
		nuoviMalati = new XYChart.Series <> ();
		nuoviMalati.setName("Malati ("+giorniAgg+"gg)");
		nuoviMorti = new XYChart.Series <> ();
		nuoviMorti.setName("Morti ("+giorniAgg+"gg)");
		
		nuoviContainer.put(Status.CONTAGIOSO, nuoviContagiati);
		nuoviContainer.put(Status.MALATO, nuoviMalati);
		nuoviContainer.put(Status.MORTO, nuoviMorti);
		
		tempValues = new HashMap<StatisticheEnte.Status, Integer>();
		maxValues = new HashMap<StatisticheEnte.Status, Integer>();
		maxDates = new HashMap<StatisticheEnte.Status, Integer>();
		
		ultimaStampa = new HashMap<>();
		
		Status[] stati = {Status.CONTAGIOSO, Status.MALATO, Status.MORTO};
		
		for (Status st : stati) {
			tempValues.put(st, 0);
			maxValues.put(st, 0);
			maxDates.put(st, 0);
			ultimaStampa.put(st, 0);
		}
		
		azzeraInformazioni();
		azzeraVicini();
	}

	public String getRef() {
		return ref;
	}

	public AggregationType getAgg() {
		return agg;
	}

	/**
	 * Aggiorna lo stato delle misure prese per contenere il 
	 * virus in un determinato ente.
	 * @param ag Age group per cui si effettua la modifica
	 * @param ht tipo di misura adottata
	 */
	public void updateHandligAge(AgeGroup ag, HandlingType ht) {
		handlingAge.put(ag, ht);
	}
	
	/**
	 * Mappa tra AgeGroup e indici delle righe della
	 * matrice informazioni
	 * @return	indice data eta'.
	 */
	public Map<AgeGroup, Integer> getRowAgeGroup() {
		return rowAgeGroup;
	}

	/**
	 * Mappa tra Status e indici delle colonne della
	 * matrice informazioni
	 * @return	indice dato stato.
	 */
	public Map<Status, Integer> getColStatus() {
		return colStatus;
	}

	public Integer[][] getInformazioni() {
		return informazioniEnte;
	}
	
	public int getTotPopolazione() {
		return totPopolazione;
	}

	/**
	 * Funzione che restituisce la popolazione, divisa per AgeGroup, 
	 * dello Status passato per parametro.
	 * @param status Status che si vuole nel risultato.
	 * @return Mappa: Stato -> Popolazione nella fascia indicata
	 */
	public Map <AgeGroup, Integer> getNumStato(Status status) {
		Map <AgeGroup, Integer> nStatus = new HashMap<>();
		for (AgeGroup ag : AgeGroup.values()) 
			nStatus.put(ag, informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(status)]);
		return nStatus;
	}
	
	/**
	 * Funzione che restituisce la popolazione, divisa per Status, 
	 * dell'AgeGroup passato per parametro.
	 * @param age AgeGroup che si vuole nel risultato.
	 * @return Mappa: Eta' -> Popolazione nella fascia indicata
	 */
	public Map <Status, Integer> getNumEta(AgeGroup age) {
		Map <Status, Integer> nAge = new HashMap<>();
		for (Status st : Status.values()) 
			nAge.put(st, informazioniEnte[rowAgeGroup.get(age)][colStatus.get(st)]);
		return nAge;
	}
	
	/**
	 * Funzione che restituisce la somma delle popolazioni, divise per AgeGroup, 
	 * degli status passati per parametro.
	 * @param stati Array di Status che si vogliono aggregare
	 * 		nel risultato.
	 * @return Mappa: Stato -> Popolazione nelle fasce indicate
	 */
	public Map <AgeGroup, Integer> getNumPopulationFromStati(Status[] stati) {
		Map <AgeGroup, Integer> nStati = new HashMap<>();
		for (AgeGroup ag : AgeGroup.values()) {
			int cnt = 0;
			for (Status st : stati)
				cnt += informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(st)];
			nStati.put(ag, cnt);
		}
		return nStati;
	}
	
	/**
	 * Funzione che restituisce la somma delle popolazioni, divise per Status, 
	 * degli AgeGroup passati per parametro.
	 * @param ages Array di AgeGroup che si vogliono aggregare
	 * 		nel risultato.
	 * @return Mappa: Eta' -> Popolazione nelle fasce indicate
	 */
	public Map <Status, Integer> getNumPopulationFromEta(AgeGroup[] ages) {
		Map <Status, Integer> nEta = new HashMap<>();
		for (Status st : Status.values()) {
			int cnt = 0;
			for (AgeGroup ag : ages)
				cnt += informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(st)];
				nEta.put(st, cnt);
		}
		return nEta;
	}
	
	/**
	 * Funzione che restituisce una mappa che, 
	 * dato l'AgeGroup, fornisce le misure
	 * che sono state adottate per quell'eta'.
	 * @return Mappa: Eta' -> Misure adottate
	 */
	public HandlingType getHandlingTypes(AgeGroup ag) {
		return handlingAge.get(ag);
	}
	
	public void updateVicini(StatisticheEnte statsVicino, Map<AgeGroup, Double> coeff) {
		for (AgeGroup ag : AgeGroup.values()) {
			for (Status st : Status.values()) {
				int i = rowAgeGroup.get(ag);
				int j = colStatus.get(st);
				informazioniVicini[i][j] += coeff.get(ag)*statsVicino.getInformazioni()[i][j];
			}
		}
	}
	
	/**
	 * Funzione che restituisce la somma delle popolazioni (sia propria che di passaggio), divise per Status, 
	 * degli AgeGroup passati per parametro.
	 * @param ages Array di AgeGroup che si vogliono aggregare
	 * 		nel risultato.
	 * @return Mappa: Eta' -> Popolazione nelle fasce indicate
	 */
	public Map <AgeGroup, Double> getNumPopulationFromStatiForIteration(Status[] stati, Map <AgeGroup, Double> percImmobilita) {
		Map <AgeGroup, Double> nStati = new HashMap<>();
		for (AgeGroup ag : AgeGroup.values()) {
			double cnt = 0.0;
			for (Status st : stati)
				cnt += (double) informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(st)]*percImmobilita.get(ag) + informazioniVicini[rowAgeGroup.get(ag)][colStatus.get(st)];
			nStati.put(ag, cnt);
		}
		return nStati;
	}
	
	/**
	 * Restituisce le probabilità che un nuovo contagio si attui
	 * in una determinata fascia di età (Sommatoria Pag = 1)
	 * @param st status a cui ci si riferisce (per implementazione solo SANO)
	 * @param popRiferimento popolazione totale di quello status
	 * @param parVirus parametri del virus
	 * @return Mappa tra AgeGroup -> Prob determinata classe
	 */
	public Map <AgeGroup, Double> getPercentages(Status st, double popRiferimento, ParametriVirus parVirus) {
		Map <AgeGroup, Double> nuovi = new HashMap<>();
		Map <AgeGroup, Double> molt = new HashMap<>();
		if (st == Status.SANO) {
			double totS = 0.0;
			for (AgeGroup ag : AgeGroup.values()) {
				double coeff = parVirus.getMapSpreadReductions(ag, handlingAge.get(ag))
						* informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(st)] / popRiferimento;
				totS += coeff;
				molt.put(ag, coeff);
			}
			for (AgeGroup ag : AgeGroup.values()) {
				nuovi.put(ag, molt.get(ag)/totS);
			}
			return nuovi;
		}
		//ALTRI
		return null;
	}
	
	/**
	 * Controlla che le persone sane (per ogni fascia di età) siano abbastanza
	 * per coprire i nuovi contagi.
	 * In caso contario risolve le controversie.
	 * @param nuoviContagi Numero di nuovi contagi per fascia di età
	 */
	public void checkSani(Map <AgeGroup, Integer> nuoviContagi) {
		for (AgeGroup ag : AgeGroup.values()) {
			nuoviContagi.put(ag, Math.min(informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.SANO)], nuoviContagi.get(ag)));
		}
	}
	
	/**
	 * Controlla che le persone contagiate (per ogni fascia di età) siano abbastanza
	 * per coprire i nuovi malati o guariti.
	 * In caso contario risolve le controversie.
	 * @param nuoviMalati Numero di nuovi malati per fascia di età
	 */
	public void checkContagiati(Map <AgeGroup, Integer> nuoviMalati, Map <AgeGroup, Integer> nuoviGuariti) {
		for (AgeGroup ag : AgeGroup.values()) {
			int contagiati = informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.CONTAGIOSO)];
			if (Math.random() < 0.5) {
				nuoviMalati.put(ag, Math.min(contagiati, nuoviMalati.get(ag)));
				nuoviGuariti.put(ag, Math.min(contagiati-nuoviMalati.get(ag), nuoviGuariti.get(ag)));
			}
			else {
				nuoviGuariti.put(ag, Math.min(contagiati, nuoviGuariti.get(ag)));
				nuoviMalati.put(ag, Math.min(contagiati-nuoviGuariti.get(ag), nuoviMalati.get(ag)));
			}
		}
	}
	
	/**
	 * Controlla che le persone malate (per ogni fascia di età) siano abbastanza
	 * per coprire i nuovi morti o curati.
	 * In caso contario risolve le controversie.
	 * @param nonPiuMalati Numero di non più malati per fascia di età
	 */
	public void checkMalati(Map <AgeGroup, Integer> nuoviMorti, Map <AgeGroup, Integer> nuoviCurati) {
		for (AgeGroup ag : AgeGroup.values()) {
			int malati = informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.MALATO)];
			if (Math.random() < 0.5) {
				nuoviMorti.put(ag, Math.min(malati, nuoviMorti.get(ag)));
				nuoviCurati.put(ag, Math.min(malati-nuoviMorti.get(ag), nuoviCurati.get(ag)));
			}
			else {
				nuoviCurati.put(ag, Math.min(malati, nuoviCurati.get(ag)));
				nuoviMorti.put(ag, Math.min(malati-nuoviCurati.get(ag), nuoviMorti.get(ag)));
			}
		}
	}
	
	/**
	 * Funzione itelligente che aggiorna le informazioni sulla base dei parametri
	 * passati come input
	 * @param nuovi Numero di nuovi per lo status
	 * @param st Status a cui si fa riferimento
	 */
	public void updateInformation(Map <AgeGroup, Integer> nuovi, Status st) {
		for (AgeGroup ag : AgeGroup.values()) {
			informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(st)] += nuovi.get(ag);
			totPopolazione += nuovi.get(ag);
			
			if (st == Status.CONTAGIOSO)
				informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.SANO)] -= nuovi.get(ag);
			if (st == Status.MALATO || st == Status.GUARITO)
				informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.CONTAGIOSO)] -= nuovi.get(ag);
			if (st == Status.MORTO || st == Status.CURATO)
				informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.MALATO)] -= nuovi.get(ag);
		}
	}

	public void updateTemporalStats(int nuovi, Status st, int giorno) {
		
		if (giornoArrivoContagio == null)
			giornoArrivoContagio = giorno;
		
		if (!(st == Status.CONTAGIOSO || st == Status.MALATO || st == Status.MORTO))
			return;
		
		if (giorno > ultimaStampa.get(st)) {
			
			if (giorno >= ultimaStampa.get(st) + giorniAgg) {
				
				if (tempValues.get(st) > maxValues.get(st)) {
					maxValues.put(st, tempValues.get(st));
					maxDates.put(st, ultimaStampa.get(st));
				}
				
				nuoviContainer.get(st).getData().add(new XYChart.Data <String, Number> 
					(Integer.toString(ultimaStampa.get(st)), tempValues.get(st)));
				
				tempValues.put(st, 0);
				
				ultimaStampa.put(st, giorno);
			}
		}
		
		tempValues.put(st, nuovi + tempValues.get(st));
	}
	
	public void azzeraVicini() {
		for (int i = 0; i <nRow; i++)
			for (int j = 0; j<nCol; j++)
				informazioniVicini[i][j] = 0.0;
	}
	
	public void azzeraInformazioni() {
		totPopolazione = 0;
		for (int i = 0; i <nRow; i++)
			for (int j = 0; j<nCol; j++)
				informazioniEnte[i][j] = 0;
	}
	
	public void setSaniInizio(AgeGroup ag, int saniPerAge) {
		informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(Status.SANO)] += saniPerAge;
		totPopolazione += saniPerAge;
	}

	public void setHandling(HandlingType handling) {
		for (AgeGroup ag : AgeGroup.values())
			this.handlingAge.put(ag, handling);
	}
	
	public HandlingType getHandling() {
		return handlingAge.get(AgeGroup.SCUOLA);
	}

	@Override
	public String toString() {
		String r = ref + "\n";
		for (AgeGroup ag : AgeGroup.values()) {
			r += ag + ":";
			for (Status st : Status.values()) {
				r += " " + st + ":" + informazioniEnte[rowAgeGroup.get(ag)][colStatus.get(st)];
			}
			r += "\n";
		}
		return r;
	}

	public Integer getGiornoArrivoContagio() {
		return giornoArrivoContagio;
	}

	public Map<Status, XYChart.Series<String, Number>> getNuoviContainer() {
		return nuoviContainer;
	}

	public Map<Status, Integer> getMaxValues() {
		return maxValues;
	}

	public Map<Status, Integer> getMaxDates() {
		return maxDates;
	}
}
