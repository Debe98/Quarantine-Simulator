package it.polito.tdp.covid.model;

import java.util.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.covid.db.CovidDAO;
import it.polito.tdp.covid.model.Event.EventType;
import it.polito.tdp.covid.model.ParametriSimulazione.ChanceType;
import it.polito.tdp.covid.model.ParametriSimulazione.SimulationType;
import it.polito.tdp.covid.model.ParametriVirus.HandlingType;
import it.polito.tdp.covid.model.TypedWeightedEdge.*;
import it.polito.tdp.covid.stats.StatisticheEnte;
import it.polito.tdp.covid.stats.StatisticheEnte.AgeGroup;
import it.polito.tdp.covid.stats.StatisticheEnte.Status;

public class Model {
	private Map <Integer, Comune> comuniId;
	private Map <String, Comune> comuniName;
	private Map <String, Comune> capoluoghiPro;
	private Map <String, Comune> capoluoghiRe;
	private Map <String, Provincia> provinceSigla;
	private Map <String, Provincia> provinceName;
	private Map <Integer, Regione> regioniId;
	private Map <String, Regione> regioniName;
	
	private Map <AggregationType, List <String>> ordinati;
	private List <String> comuniOrdinati;
	private List <String> provinceOrdinate;
	private List <String> regioniOrdinate;
	
	private CovidDAO dao;
	
	private Graph <Comune, TypedWeightedEdge> grafoComuni;
	private Graph <Provincia, TypedWeightedEdge> grafoProvince;
	private Graph <Regione, TypedWeightedEdge> grafoRegioni;
	private int lvGrafi;
	
	private List <TypedWeightedEdge> adiacenzeComuni;
	private List <TypedWeightedEdge> adiacenzeProvincie;
	private List <TypedWeightedEdge> adiacenzeRegioni;
	
	private Map <String, Map <AgeGroup, Integer>> valoriT0;
	
	private PriorityQueue <Event> queue;
	private boolean simulazioneAttiva;
	private int giornoUltimoStop;

	// PARAMETRI DI SIMULAZIONE
	private int giornoCorrente;

	// MODELLO DEL MONDO
	private ParametriSimulazione parSimulazione;
	private ParametriVirus parVirus;
	private ParametriMondo parMondo;
	private double k;
	private double italianAvgDensity;
	private int italianPopulation;

	// VALORI DA CALCOLARE
	private Map <AggregationType, Map <String, StatisticheEnte>> statsContainer;
	private Map <String, StatisticheEnte> statsComuni;
	private Map <String, StatisticheEnte> statsProvince;
	private Map <String, StatisticheEnte> statsRegioni;
	private StatisticheEnte statsNazione;
	
	private Map <AggregationType, Set <String>> isChiusoContainer;
	private Set <String> isChiusoComune;
	private Set <String> isChiusoProvincia;
	private Set <String> isChiusoRegione;

	public Model() {
		dao = new CovidDAO();
		queue = new PriorityQueue<>();
		comuniId = new HashMap<Integer, Comune>();
		comuniName = new HashMap<String, Comune>();
		provinceSigla = new HashMap<String, Provincia>();
		provinceName = new HashMap<String, Provincia>();
		capoluoghiPro = new HashMap<String, Comune>();
		regioniId = new HashMap<Integer, Regione>();
		regioniName = new HashMap<String, Regione>();
		capoluoghiRe = new HashMap<String, Comune>();
		
		comuniOrdinati = new LinkedList<String>();
		provinceOrdinate = new LinkedList<String>();
		regioniOrdinate = new LinkedList<String>();
		
		ordinati = new HashMap<>();
		ordinati.put(AggregationType.COMUNE, comuniOrdinati);
		ordinati.put(AggregationType.PROVINCIA, provinceOrdinate);
		ordinati.put(AggregationType.REGIONE, regioniOrdinate);
		
		//Creazione mappe utili
		dao.listAllCities(comuniId, comuniName);
		dao.listAllProvincies(provinceSigla, provinceName);
		dao.listCapoluoghiProvincia(capoluoghiPro, comuniId);
		dao.listAllRegions(regioniId, regioniName);
		dao.listCapoluoghiRegione(capoluoghiRe, comuniId);
		
		valoriT0 = new HashMap<>();
		
		italianAvgDensity = dao.getItalianAvgDensity();
		italianPopulation = dao.getItalianPopulation();
		//Creazione grafi
		grafoComuni = new SimpleWeightedGraph <>(TypedWeightedEdge.class);
		grafoProvince = new SimpleWeightedGraph <>(TypedWeightedEdge.class);
		grafoRegioni = new SimpleWeightedGraph <>(TypedWeightedEdge.class);
		//Creazione adiacenze
		adiacenzeComuni = new LinkedList<TypedWeightedEdge>();
		adiacenzeProvincie = new LinkedList<TypedWeightedEdge>();
		adiacenzeRegioni = new LinkedList<TypedWeightedEdge>();
		
		lvGrafi = 0;
		
		parSimulazione = new ParametriSimulazione();
		parVirus = new ParametriVirus();
		parMondo = new ParametriMondo();
		simulazioneAttiva = false;
	}

	public ParametriSimulazione getParSimulazione() {
		return parSimulazione;
	}

	public ParametriVirus getParVirus() {
		return parVirus;
	}

	public ParametriMondo getParMondo() {
		return parMondo;
	}

	public void creaGrafo() {
		k = parMondo.getIndiceDiAdiacenza();
		AggregationType agg = parSimulazione.getAggSimulazione();
		switch (agg) {
		case COMUNE:
			creaGrafoComuni();
			if (lvGrafi < 3) lvGrafi = 3;
			break;
		case PROVINCIA:
			creaGrafoProvince();
			if (lvGrafi < 2) lvGrafi = 2;
			break;
		case REGIONE:
			creaGrafoRegioni();
			if (lvGrafi < 1) lvGrafi = 1;
			break;
		}
		inizializzaMappeStatistiche();
	}
	
	public void startSimulazione() {
		//CANCELLA PRECEDENTI SIMULAZIONI!!
		simulazioneAttiva = true;
		giornoUltimoStop = 0;
		giornoCorrente = 0;
		this.queue.clear();
	}

	public void flushNuovi() {
		AggregationType agg = parSimulazione.getAggSimulazione();
		System.out.println(valoriT0);
		for (String ref : valoriT0.keySet())
			aggiornamentoEnte(agg, ref, Status.CONTAGIOSO, valoriT0.get(ref));
		valoriT0.clear();
	}
	
	public void run() {
		iterazione();
		//System.out.println("Giorno: "+giornoCorrente);
	}
	
	public void terminaSimulazione() {
		simulazioneAttiva = false;
		giornoUltimoStop = 0;
		giornoCorrente = 0;
		inizializzaMappeStatistiche();
		this.queue.clear();
	}
	
	public void aggiornamentoEnte(AggregationType agg, String ref, Status st, Map<AgeGroup, Integer> nuoviPerGruppo) {
		int nuovi = nuoviPerGruppo.get(AgeGroup.SCUOLA) + 
				nuoviPerGruppo.get(AgeGroup.LAVORO) + nuoviPerGruppo.get(AgeGroup.PENSIONE);
		
		switch (agg) {			
		case COMUNE:
			Comune c = comuniName.get(ref);

			StatisticheEnte statsComune = statsComuni.get(ref);
			//statsComune... METODO AGGIORNAMENTO CAMPO
			statsComune.updateInformation(nuoviPerGruppo, st);
			statsComune.updateTemporalStats(nuovi, st, giornoCorrente);

			StatisticheEnte statsProvincia = statsProvince.get(c.getProvincia());
			statsProvincia.updateInformation(nuoviPerGruppo, st);
			statsProvincia.updateTemporalStats(nuovi, st, giornoCorrente);

			StatisticheEnte statsRegione = statsRegioni.get(c.getRegione());
			statsRegione.updateInformation(nuoviPerGruppo, st);
			statsRegione.updateTemporalStats(nuovi, st, giornoCorrente);
			break;
			
		case PROVINCIA:
			Provincia p = provinceSigla.get(ref);

			statsProvincia = statsProvince.get(p.getSigla());
			statsProvincia.updateInformation(nuoviPerGruppo, st);
			statsProvincia.updateTemporalStats(nuovi, st, giornoCorrente);

			statsRegione = statsRegioni.get(regioniId.get(p.getIdRegione()).getRegione());
			statsRegione.updateInformation(nuoviPerGruppo, st);
			statsRegione.updateTemporalStats(nuovi, st, giornoCorrente);
			break;
			
		case REGIONE:
			statsRegione = statsRegioni.get(ref);
			statsRegione.updateInformation(nuoviPerGruppo, st);
			statsRegione.updateTemporalStats(nuovi, st, giornoCorrente);
			break;
		}
		statsNazione.updateInformation(nuoviPerGruppo, st);
		statsNazione.updateTemporalStats(nuovi, st, giornoCorrente);
	}

	public int getGiornoCorrente() {
		return giornoCorrente;
	}

	public StatisticheEnte getStatsNazione() {
		return statsNazione;
	}

	public boolean isSimulazioneAttiva() {
		return simulazioneAttiva;
	}

	public void setSimulazioneAttiva(boolean simulazioneAttiva) {
		this.simulazioneAttiva = simulazioneAttiva;
	}

	public void iterazione() {
		giornoCorrente++;
		System.out.println("Giorno: "+giornoCorrente);
		Map <AggregationType, Map <AgeGroup, Double>> percImm = parMondo.getPercentualeImmobilita();
		//System.out.println("perc imm : " + percImm);
		switch (parSimulazione.getAggSimulazione()) {
		case COMUNE:
			updatePopulationsComuni();
			for (Comune c : grafoComuni.vertexSet())
				dailyEvolution(statsComuni.get(c.getComune()), c.getDensity(), percImm);
			
			break;
			
		case PROVINCIA:
			updatePopulationsProvince();
			for (Provincia p : grafoProvince.vertexSet())
				dailyEvolution(statsProvince.get(p.getSigla()), p.getDensity(), percImm);
			
			break;
			
		case REGIONE:
			updatePopulationsRegioni();
			for (Regione r : grafoRegioni.vertexSet())
				dailyEvolution(statsRegioni.get(r.getRegione()), r.getDensity(), percImm);
			
			break;
		}
		//checkCondizioniTerm();
	}
	
	public void dailyEvolution(StatisticheEnte stats, double densita, Map <AggregationType, Map <AgeGroup, Double>> percImm) {
		ChanceType ct = parSimulazione.getTipoProbabilita();
		System.out.println(stats.getRef());
		
		// SANI -> CONTAGIOSI
		double densityRatio = densita / italianAvgDensity;
		densityRatio = Math.sqrt(densityRatio);
		//System.out.println(stats.getRef()+" - densityRatio: "+densityRatio );

		//Numero totale di persone sane
		double totSaniIter = 0.0;
		Status[] sani = {Status.SANO};
		Map <AgeGroup, Double> saniGruppoIter = stats.getNumPopulationFromStatiForIteration(sani, percImm);
		for (AgeGroup ag : AgeGroup.values()) {
			totSaniIter += saniGruppoIter.get(ag);
		}
		//System.out.println("#SANI: "+totSani);
		//Numero totale di persone contagiose
		double totContagiosiIter = 0.0;
		Status[] contagiosi = {Status.CONTAGIOSO};
		Map <AgeGroup, Double> contagiosiGruppoIter = stats.getNumPopulationFromStatiForIteration(contagiosi, percImm);
		for (AgeGroup ag : AgeGroup.values()) {
			totContagiosiIter += contagiosiGruppoIter.get(ag);
		}
		//System.out.println("#CONTAGIOSI: "+totContagiosi);
		//Numero totale di persone libere di muoversi
		double totFree = 0.0;
		double totFreeAdj = 0.0;
		Status[] free = {Status.SANO, Status.CONTAGIOSO, Status.GUARITO, Status.CURATO};
		Map <AgeGroup, Double> freeGruppo = stats.getNumPopulationFromStatiForIteration(free, percImm);
		for (AgeGroup ag : AgeGroup.values()) {
			totFree += freeGruppo.get(ag);
			totFreeAdj += freeGruppo.get(ag)*parVirus.getMapSpreadReductions(ag, stats.getHandlingTypes(ag)); 
		}
		// Effetti delle politiche di handling pesate in base alle età
		// e normalizzate rispetto alla pop totale. (0-1]
		double HandlingAdj = totFreeAdj / totFree;

		//System.out.println("#FREE: "+totFree);
		//System.out.println("Handligh Adjustment: "+HandlingAdj);

		if (totSaniIter < 0) {
			totSaniIter = 0;
		}
		if (totContagiosiIter < 0) {
			totContagiosiIter = 0;
		}

		int n = 0;
		Map <AgeGroup, Integer> nuoviContagiatiPerGruppo = new HashMap<StatisticheEnte.AgeGroup, Integer>();
		if (totContagiosiIter != 0 && totSaniIter != 0) {
			// B = B0 * DensityRatio * HandlingAdj (pesato su POP INCONTRABILE)
			double bAdj = parVirus.getParB0() * densityRatio * HandlingAdj;
			// #new_CONTAGIATI = B * 2 * (SANI*CONTAGIOSI) / (POP INCONTRABILE (SANI+CONTAGIOSI+GUARITI) -1)
			double perContatti = (totSaniIter*totContagiosiIter*2)/(totFree*(totFree-1));
			double perNuovoContagio = bAdj * perContatti;
			double valAtteso = (bAdj * totSaniIter*totContagiosiIter*2)/(totFree-1);
			
			//SANI DEL NODO
			int totSani = 0;
			Map <AgeGroup, Integer> saniGruppo = stats.getNumPopulationFromStati(sani);
			for (AgeGroup ag : AgeGroup.values()) {
				totSani += saniGruppo.get(ag);
			}

			System.out.println("Val atteso new #C = " + valAtteso);

			Map <AgeGroup, Double> percPerGruppo = stats.getPercentages(Status.SANO, totSani, parVirus);
			
			if (ct == ChanceType.STOCASTICA) {
				int nScuola = 0;
				int nLavoro = 0;
				int nPensione = 0;
				double primo = percPerGruppo.get(AgeGroup.SCUOLA);
				double secondo = primo + percPerGruppo.get(AgeGroup.LAVORO);

				for (int i = 0; i < (int)totFree; i++) {
					//NUOVO!
					if (Math.random() < perNuovoContagio) {
						n++;
						double temp = Math.random();
						if (temp <= primo)
							nScuola++;
						else if (temp <= secondo)
							nLavoro++;
						else
							nPensione++;
					}
				}

				nuoviContagiatiPerGruppo.put(AgeGroup.SCUOLA, nScuola);
				nuoviContagiatiPerGruppo.put(AgeGroup.LAVORO, nLavoro);
				nuoviContagiatiPerGruppo.put(AgeGroup.PENSIONE, nPensione);
			}
			else {
				for (AgeGroup ag : AgeGroup.values()) {
					double nuovi = percPerGruppo.get(ag) * valAtteso;
					int pIntera = (int) nuovi;
					if (Math.random() <= (nuovi - pIntera))
						pIntera++;
					n += pIntera;
					nuoviContagiatiPerGruppo.put(ag, pIntera);
				}
			}
			
			stats.checkSani(nuoviContagiatiPerGruppo);

			System.out.println("Effettivi new #C = " + nuoviContagiatiPerGruppo);

		}

		//CONTAGIOSI -> GUARITI e MALATI
		Map <AgeGroup, Integer> nuoviMalatiPerGruppo = new HashMap<StatisticheEnte.AgeGroup, Integer>();
		Map <AgeGroup, Integer> nuoviGuaritiPerGruppo = new HashMap<StatisticheEnte.AgeGroup, Integer>();
		Map <AgeGroup, Integer> contagiosiGruppo = stats.getNumStato(Status.CONTAGIOSO);
		double contRatio = 1.0 / parVirus.getAvgTime(Status.CONTAGIOSO);
		int nma = 0;
		int ngu = 0;
		int nGuariti = 0;
		int nMalati = 0;

		if (ct == ChanceType.STOCASTICA) {
			for (AgeGroup ag : AgeGroup.values()) {
				double temp = contagiosiGruppo.get(ag);
				double percMalato = parVirus.getAgeGroupFromContagiosoToMalato(ag);
				for (int i = 0; i < (int) temp; i++) {
					if (Math.random() < contRatio) {
						if (Math.random() < percMalato) {
							nma++;
							nMalati++;
						}
						else {
							ngu++;
							nGuariti++;
						}
					}
				}
				nuoviMalatiPerGruppo.put(ag, nMalati);
				nuoviGuaritiPerGruppo.put(ag, nGuariti);
				nMalati = 0;
				nGuariti = 0;
			}
		}
		else {
			for (AgeGroup ag : AgeGroup.values()) {
				double fineContagio = contRatio * contagiosiGruppo.get(ag);
				double tempMalati = fineContagio * parVirus.getAgeGroupFromContagiosoToMalato(ag);
				double tempGuariti = fineContagio - tempMalati;
				nMalati = (int) tempMalati;
				nGuariti = (int) tempGuariti;
				
				if (Math.random() <= (tempMalati - nMalati))
					nMalati++;
				if (Math.random() <= (tempGuariti - nGuariti))
					nGuariti++;
				
				nma += nMalati;
				ngu += nGuariti;
				
				nuoviMalatiPerGruppo.put(ag, nMalati);
				nuoviGuaritiPerGruppo.put(ag, nGuariti);
			}
		}
		stats.checkContagiati(nuoviMalatiPerGruppo, nuoviGuaritiPerGruppo);
		
		//MALATI -> CURATI e MORTI
		Map <AgeGroup, Integer> malatiGruppo = stats.getNumStato(Status.MALATO);
		Map <AgeGroup, Integer> nuoviMortiPerGruppo = new HashMap<StatisticheEnte.AgeGroup, Integer>();
		Map <AgeGroup, Integer> nuoviCuratiPerGruppo = new HashMap<StatisticheEnte.AgeGroup, Integer>();
		double malRatio = 1.0 / parVirus.getAvgTime(Status.MALATO);
		int nmo = 0;
		int ncu = 0;
		int nCurati = 0;
		int nMorti = 0;

		if (ct == ChanceType.STOCASTICA) {
			for (AgeGroup ag : AgeGroup.values()) {
				double temp = malatiGruppo.get(ag);
				double percMorto = parVirus.getAgeGroupDeathsRatio(ag);
				for (int i = 0; i < (int) temp; i++) {
					if (Math.random() < malRatio) {
						if (Math.random() < percMorto) {
							nmo++;
							nMorti++;
						}
						else {
							ncu++;
							nCurati++;
						}
					}
				}
				nuoviMortiPerGruppo.put(ag, nMorti);
				nuoviCuratiPerGruppo.put(ag, nCurati);
				nMorti = 0;
				nCurati = 0;
			}
		}
		else {
			for (AgeGroup ag : AgeGroup.values()) {
				double fineMalattia = malRatio * malatiGruppo.get(ag);
				double tempMorti = fineMalattia * parVirus.getAgeGroupDeathsRatio(ag);
				double tempCurati = fineMalattia - tempMorti;
				nMorti = (int) tempMorti;
				nCurati = (int) tempCurati;
				if (Math.random() <= (tempMorti - nMorti))
					nMorti++;
				if (Math.random() <= (tempCurati - nCurati))
					nCurati++;
				
				
				nmo += nMorti;
				ncu += nCurati;
				
				nuoviMortiPerGruppo.put(ag, nMorti);
				nuoviCuratiPerGruppo.put(ag, nCurati);
			}
		}
		
		stats.checkMalati(nuoviMortiPerGruppo, nuoviCuratiPerGruppo);
		
		if (n > 0)
			aggiornamentoEnte(stats.getAgg(), stats.getRef(), Status.CONTAGIOSO, nuoviContagiatiPerGruppo);

		if (nma > 0)
			aggiornamentoEnte(stats.getAgg(), stats.getRef(), Status.MALATO, nuoviMalatiPerGruppo);
		if (ngu > 0)
			aggiornamentoEnte(stats.getAgg(), stats.getRef(), Status.GUARITO, nuoviGuaritiPerGruppo);
		
		if (nmo > 0)
			aggiornamentoEnte(stats.getAgg(), stats.getRef(), Status.MORTO, nuoviMortiPerGruppo);
		if (ncu > 0)
			aggiornamentoEnte(stats.getAgg(), stats.getRef(), Status.CURATO, nuoviCuratiPerGruppo);
		
		//AZZERAMENTO stats.vicini;
		stats.azzeraVicini();

		if (parSimulazione.getTipoSimulazione() == SimulationType.PRE_PARAMETERIZED) {
			
			malatiGruppo = stats.getNumStato(Status.MALATO);
			Map <AgeGroup, Integer> mortiGruppo = stats.getNumStato(Status.MORTO);
			int totMorti = 0;
			int totMalati = 0;
			for (AgeGroup ag : AgeGroup.values()) {
				totMalati += malatiGruppo.get(ag);
				totMorti += mortiGruppo.get(ag);
			}

			Map <Status, Integer> temp = new HashMap<>();
			temp.put(Status.MALATO, totMalati);
			temp.put(Status.MORTO, totMorti);

			boolean giaChiuso = isChiusoContainer.get(stats.getAgg()).contains(stats.getRef());
			
			if (parSimulazione.verChiusura(temp, stats, giornoCorrente, giaChiuso)) {
				if (!giaChiuso)
					isChiusoContainer.get(stats.getAgg()).add(stats.getRef());
			}
			else
				if (giaChiuso)
					isChiusoContainer.get(stats.getAgg()).remove(stats.getRef());

			//Cambia HandingType
			parSimulazione.changeHandingType(temp, stats, giornoCorrente);
		}
	}
	
	public void addChiudiLive(AggregationType agg, String ref) throws Exception {
		if (agg == AggregationType.REGIONE) {
			if (regioniName.get(ref) == null)
				throw new Exception("Errore: Regione non trovata!");
		}
		if (agg == AggregationType.PROVINCIA) {
			if (provinceSigla.get(ref) == null)
				throw new Exception("Errore: Provincia non trovata!");
		}
		if (agg == AggregationType.COMUNE) {
			if (comuniName.get(ref) == null)
				throw new Exception("Errore: Comune non trovato!");
		}
		
		if (isChiusoContainer.get(agg).contains(ref))
			return;
		else
			isChiusoContainer.get(agg).add(ref);
	}
	
	public void removeChiudiLive(AggregationType agg, String ref) throws Exception {
		if (agg == AggregationType.REGIONE) {
			if (regioniName.get(ref) == null)
				throw new Exception("Errore: Regione non trovata!");
		}
		if (agg == AggregationType.PROVINCIA) {
			if (provinceSigla.get(ref) == null)
				throw new Exception("Errore: Provincia non trovata!");
		}
		if (agg == AggregationType.COMUNE) {
			if (comuniName.get(ref) == null)
				throw new Exception("Errore: Comune non trovato!");
		}
		
		if (!isChiusoContainer.get(agg).contains(ref))
			return;
		else
			isChiusoContainer.get(agg).remove(ref);
	}
	
	public void addHandlingLive(AggregationType agg, String ref, HandlingType ht) throws Exception {
		AggregationType aggSim = parSimulazione.getAggSimulazione();
		
		if (agg == aggSim) {
			StatisticheEnte stats = statsContainer.get(agg).get(ref);
			if (stats == null)
				throw new Exception("Errore: Ente non trovato!");
			stats.setHandling(ht);
			return;
		}
		
		if (agg == AggregationType.REGIONE) {
			if (aggSim == AggregationType.PROVINCIA) {
				for (StatisticheEnte stats : statsContainer.get(aggSim).values()) {
					Provincia p = provinceSigla.get(stats.getRef());
					Regione r = regioniId.get(p.getIdRegione());
					if (ref.equals(r.getRegione()))
						stats.setHandling(ht);
				}
				return;
			}
			if (aggSim == AggregationType.COMUNE) {
				for (StatisticheEnte stats : statsContainer.get(aggSim).values()) {
					Comune c = comuniName.get(stats.getRef());
					Regione r = regioniName.get(c.getRegione());
					if (ref.equals(r.getRegione()))
						stats.setHandling(ht);
				}
				return;
			}
		}
		else if (agg == AggregationType.PROVINCIA) {
			if (aggSim == AggregationType.COMUNE) {
				for (StatisticheEnte stats : statsContainer.get(aggSim).values()) {
					Comune c = comuniName.get(stats.getRef());
					Provincia p = provinceSigla.get(c.getProvincia());
					if (ref.equals(p.getSigla()))
						stats.setHandling(ht);
				}
				return;
			}
		}
	}
	
	public void removeHandlingLive(AggregationType agg, String ref) throws Exception {
		addHandlingLive(agg, ref, HandlingType.NO_ACTS);
	}
	
	public void inizializzaMappeStatistiche() {
		statsComuni = new HashMap<String, StatisticheEnte>();
		statsProvince = new HashMap<String, StatisticheEnte>();
		statsRegioni = new HashMap<String, StatisticheEnte>();
		
		statsContainer = new HashMap<TypedWeightedEdge.AggregationType, Map<String,StatisticheEnte>>();
		statsContainer.put(AggregationType.COMUNE, statsComuni);
		statsContainer.put(AggregationType.PROVINCIA, statsProvince);
		statsContainer.put(AggregationType.REGIONE, statsRegioni);
		
		statsNazione = new StatisticheEnte("Italia", null);

		for (String ref : regioniName.keySet())
			statsRegioni.put(ref, new StatisticheEnte(ref, AggregationType.REGIONE));
		
		if (parSimulazione.getAggSimulazione() != AggregationType.REGIONE)
			for (String ref : provinceSigla.keySet())
				statsProvince.put(ref, new StatisticheEnte(ref, AggregationType.PROVINCIA));
		
		if (parSimulazione.getAggSimulazione() == AggregationType.COMUNE)
			for (String ref : comuniName.keySet())
				statsComuni.put(ref, new StatisticheEnte(ref, AggregationType.COMUNE));

		valoriT0.clear();
		
		isChiusoComune = new HashSet<>();
		isChiusoProvincia = new HashSet<>();
		isChiusoRegione = new HashSet<>();
		
		isChiusoContainer = new HashMap<>();
		isChiusoContainer.put(AggregationType.COMUNE, isChiusoComune);
		isChiusoContainer.put(AggregationType.PROVINCIA, isChiusoProvincia);
		isChiusoContainer.put(AggregationType.REGIONE, isChiusoRegione);
		
		parSimulazione.resetToNewSimulation();
		
		switch (parSimulazione.getAggSimulazione()) {
		case COMUNE:
			for (AgeGroup ag : AgeGroup.values()) {
				double per = parMondo.getAgeGroupPercentages(ag);
				for (Comune c : grafoComuni.vertexSet()) {
					int resPerGruppo = (int) (c.getNumResidenti()*per);
					statsComuni.get(c.getComune()).setSaniInizio(ag, resPerGruppo);
					statsProvince.get(c.getProvincia()).setSaniInizio(ag, resPerGruppo);
					statsRegioni.get(c.getRegione()).setSaniInizio(ag, resPerGruppo);
					statsNazione.setSaniInizio(ag, resPerGruppo);
				}
			}
			break;
		case PROVINCIA:
			for (AgeGroup ag : AgeGroup.values()) {
				double per = parMondo.getAgeGroupPercentages(ag);
				for (Provincia p : grafoProvince.vertexSet()) {
					int resPerGruppo = (int) (p.getResidenti()*per);
					statsProvince.get(p.getSigla()).setSaniInizio(ag, resPerGruppo);
					statsRegioni.get(regioniId.get(p.getIdRegione()).getRegione()).setSaniInizio(ag, resPerGruppo);
					statsNazione.setSaniInizio(ag, resPerGruppo);
				}
			}
			break;
		case REGIONE:
			for (AgeGroup ag : AgeGroup.values()) {
				double per = parMondo.getAgeGroupPercentages(ag);
				for (Regione r : grafoRegioni.vertexSet()) {
					int resPerGruppo = (int) (r.getNumResidenti()*per);
					statsRegioni.get(r.getRegione()).setSaniInizio(ag, resPerGruppo);
					statsNazione.setSaniInizio(ag, resPerGruppo);
				}
			}
			break;
		}
		
	}
	
	private void updatePopulationsComuni() {		//Va fatto anche per province e regioni
		//RAPPORTI DA COMUNI
		for (TypedWeightedEdge e : grafoComuni.edgeSet()) {
			Comune c1 = grafoComuni.getEdgeSource(e);
			Comune c2 = grafoComuni.getEdgeTarget(e);
			
			if (arcoNonTransitabile(e, c1, c2))
				continue;
			
			StatisticheEnte stat1 = statsComuni.get(c1.getComune());
			StatisticheEnte stat2 = statsComuni.get(c2.getComune());
			//percentuale che si muove * peso di A tra vicini di B
			Map <AgeGroup, Double> coeff1 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			Map <AgeGroup, Double> coeff2 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			for (AgeGroup ag : AgeGroup.values()) {
				coeff1.put(ag, parMondo.getPercentualeMobilita(AggregationType.COMUNE, ag)*
						c1.getNumResidenti()/c2.getNumResidentiVicini());
				coeff2.put(ag, parMondo.getPercentualeMobilita(AggregationType.COMUNE, ag)*
						c2.getNumResidenti()/c1.getNumResidentiVicini());
			}
			stat1.updateVicini(stat2, coeff1);
			stat2.updateVicini(stat1, coeff2);
			//System.out.format("\nArco: %s\n	%s e %s\n", e, coeff1, coeff2);
		}
		//RAPPORTI FRA PROVINCE
		for (TypedWeightedEdge e : grafoProvince.edgeSet()) {
			Provincia p1 = grafoProvince.getEdgeSource(e);
			Provincia p2 = grafoProvince.getEdgeTarget(e);
			
			Comune c1 = capoluoghiPro.get(p1.getSigla());
			Comune c2 = capoluoghiPro.get(p2.getSigla());
			
			if (arcoNonTransitabile(e, p1, p2))
				continue;
			
			StatisticheEnte stat1 = statsComuni.get(c1.getComune());
			StatisticheEnte stat2 = statsComuni.get(c2.getComune());
			//percentuale che si muove * peso di A tra vicini di B
			Map <AgeGroup, Double> coeff1 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			Map <AgeGroup, Double> coeff2 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			for (AgeGroup ag : AgeGroup.values()) {
				coeff1.put(ag, parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, ag)*
						p1.getResidenti()/p2.getResidentiVicini());
				coeff2.put(ag, parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, ag)*
						p2.getResidenti()/p1.getResidentiVicini());
			}
			stat1.updateVicini(stat2, coeff1);
			stat2.updateVicini(stat1, coeff2);
			//System.out.format("Arco: %s\n	%s e %s\n", e, coeff1, coeff2);
		}
		//RAPPORTI FRA REGIONI
		for (TypedWeightedEdge e : grafoRegioni.edgeSet()) {
			Regione r1 = grafoRegioni.getEdgeSource(e);
			Regione r2 = grafoRegioni.getEdgeTarget(e);

			Comune c1 = capoluoghiRe.get(r1.getCapoluogo());
			Comune c2 = capoluoghiRe.get(r2.getCapoluogo());

			if (arcoNonTransitabile(e, r1, r2))
				continue;

			StatisticheEnte stat1 = statsComuni.get(c1.getComune());
			StatisticheEnte stat2 = statsComuni.get(c2.getComune());
			//percentuale che si muove * peso di A tra vicini di B
			Map <AgeGroup, Double> coeff1 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			Map <AgeGroup, Double> coeff2 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			for (AgeGroup ag : AgeGroup.values()) {
				coeff1.put(ag, parMondo.getPercentualeMobilita(AggregationType.REGIONE, ag)*
						r1.getNumResidenti()/italianPopulation);
				coeff2.put(ag, parMondo.getPercentualeMobilita(AggregationType.REGIONE, ag)*
						r2.getNumResidenti()/italianPopulation);
			}
			stat1.updateVicini(stat2, coeff1);
			stat2.updateVicini(stat1, coeff2);
			//System.out.format("Arco: %s\n	%s e %s\n", e, coeff1, coeff2);
		}
	}

	private void updatePopulationsProvince() {
		//RAPPORTI FRA PROVINCE
		for (TypedWeightedEdge e : grafoProvince.edgeSet()) {
			Provincia p1 = grafoProvince.getEdgeSource(e);
			Provincia p2 = grafoProvince.getEdgeTarget(e);

			if (arcoNonTransitabile(e, p1, p2))
				continue;

			StatisticheEnte stat1 = statsProvince.get(p1.getSigla());
			StatisticheEnte stat2 = statsProvince.get(p2.getSigla());
			//percentuale che si muove * peso di A tra vicini di B
			Map <AgeGroup, Double> coeff1 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			Map <AgeGroup, Double> coeff2 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			for (AgeGroup ag : AgeGroup.values()) {
				coeff1.put(ag, parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, ag)*
						p1.getResidenti()/p2.getResidentiVicini());
				coeff2.put(ag, parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, ag)*
						p2.getResidenti()/p1.getResidentiVicini());
			}
			stat1.updateVicini(stat2, coeff1);
			stat2.updateVicini(stat1, coeff2);
			//System.out.format("Arco: %s\n	%s e %s\n", e, coeff1, coeff2);
		}
		//RAPPORTI FRA REGIONI
		for (TypedWeightedEdge e : grafoRegioni.edgeSet()) {
			Regione r1 = grafoRegioni.getEdgeSource(e);
			Regione r2 = grafoRegioni.getEdgeTarget(e);

			Provincia p1 = provinceSigla.get(capoluoghiRe.get(r1.getCapoluogo()).getProvincia());
			Provincia p2 = provinceSigla.get(capoluoghiRe.get(r2.getCapoluogo()).getProvincia());

			if (arcoNonTransitabile(e, r1, r2))
				continue;

			StatisticheEnte stat1 = statsProvince.get(p1.getSigla());
			StatisticheEnte stat2 = statsProvince.get(p2.getSigla());
			//percentuale che si muove * peso di A tra vicini di B
			Map <AgeGroup, Double> coeff1 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			Map <AgeGroup, Double> coeff2 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			for (AgeGroup ag : AgeGroup.values()) {
				coeff1.put(ag, parMondo.getPercentualeMobilita(AggregationType.REGIONE, ag)*
						r1.getNumResidenti()/italianPopulation);
				coeff2.put(ag, parMondo.getPercentualeMobilita(AggregationType.REGIONE, ag)*
						r2.getNumResidenti()/italianPopulation);
			}
			stat1.updateVicini(stat2, coeff1);
			stat2.updateVicini(stat1, coeff2);
			//System.out.format("Arco: %s\n	%s e %s\n", e, coeff1, coeff2);
		}
	}

	private void updatePopulationsRegioni() {
		//RAPPORTI FRA REGIONI
		for (TypedWeightedEdge e : grafoRegioni.edgeSet()) {
			Regione r1 = grafoRegioni.getEdgeSource(e);
			Regione r2 = grafoRegioni.getEdgeTarget(e);
			
			if (arcoNonTransitabile(e, r1, r2))
				continue;
			
			StatisticheEnte stat1 = statsRegioni.get(r1.getRegione());
			StatisticheEnte stat2 = statsRegioni.get(r2.getRegione());
			//percentuale che si muove * peso di A tra vicini di B
			Map <AgeGroup, Double> coeff1 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			Map <AgeGroup, Double> coeff2 = new HashMap<StatisticheEnte.AgeGroup, Double>();
			for (AgeGroup ag : AgeGroup.values()) {
				coeff1.put(ag, parMondo.getPercentualeMobilita(AggregationType.REGIONE, ag)*
						r1.getNumResidenti()/italianPopulation);
				coeff2.put(ag, parMondo.getPercentualeMobilita(AggregationType.REGIONE, ag)*
						r2.getNumResidenti()/italianPopulation);
			}
			stat1.updateVicini(stat2, coeff1);
			stat2.updateVicini(stat1, coeff2);
			//System.out.format("Arco: %s\n	%s e %s", e, coeff1, coeff2);
		}
	}

	private Boolean arcoNonTransitabile(TypedWeightedEdge e, Comune c1, Comune c2) {
		//Comune chiuso
		if (isChiusoComune.contains(c1.getComune()))
			return true;
		if (isChiusoComune.contains(c2.getComune()))
			return true;
		//Provincia chiusa
		if (e.getAttraversamento() == CrossType.PROVINCIALE) {

			if (isChiusoProvincia.contains(c1.getProvincia()))
				return true;
			if (isChiusoProvincia.contains(c2.getProvincia()))
				return true;
		}
		//Regione Chiusa
		else if (e.getAttraversamento() == CrossType.REGIONALE) {
			
			if (isChiusoRegione.contains(c1.getRegione()))
				return true;
			if (isChiusoRegione.contains(c2.getRegione()))
				return true;
		}
		
		return false;
	}
	
	private Boolean arcoNonTransitabile(TypedWeightedEdge e, Provincia p1, Provincia p2) {
		//Provincia chiusa
		if (isChiusoProvincia.contains(p1.getSigla()))
			return true;
		if (isChiusoProvincia.contains(p2.getSigla()))
			return true;

		//Regione Chiusa
		else if (e.getAttraversamento() == CrossType.REGIONALE) {
			
			if (isChiusoRegione.contains(regioniId.get(p1.getIdRegione()).getRegione()))
				return true;
			if (isChiusoRegione.contains(regioniId.get(p2.getIdRegione()).getRegione()))
				return true;
		}
		
		return false;
	}
	
	private Boolean arcoNonTransitabile(TypedWeightedEdge e, Regione r1, Regione r2) {
		//Regione Chiusa
		if (isChiusoRegione.contains(r1.getRegione()))
			return true;
		if (isChiusoRegione.contains(r2.getRegione()))
			return true;

		return false;
	}
	
	public Double getDensityFrom(String name, AggregationType agg) {
		
		if (agg == AggregationType.COMUNE) {
			if (grafoComuni == null)
				return null;
			Comune c = comuniName.get(name);
			if (c == null)
				return null;
			return c.getDensity();
		}
		
		if (agg == AggregationType.PROVINCIA) {
			if (grafoProvince == null)
				return null;
			Provincia p = provinceSigla.get(name);
			if (p == null)
				return null;
			return p.getDensity();
		}
		
		if (agg == AggregationType.REGIONE) {
			if (grafoRegioni == null)
				return null;
			Regione r = regioniName.get(name);
			if (r == null)
				return null;
			return r.getDensity();
		}
		return null;
	}
	
	public List<TypedWeightedEdge> getEdgesFrom(String name, AggregationType agg) {
		
		if (agg == AggregationType.COMUNE) {
			if (grafoComuni == null)
				return null;
			Comune c = comuniName.get(name);
			if (c == null)
				return null;
			return new LinkedList<TypedWeightedEdge>(grafoComuni.edgesOf(c));
		}
		
		if (agg == AggregationType.PROVINCIA) {
			if (grafoProvince == null)
				return null;
			Provincia p = provinceSigla.get(name);
			if (p == null)
				return null;
			return new LinkedList<TypedWeightedEdge>(grafoProvince.edgesOf(p));
		}
		
		if (agg == AggregationType.REGIONE) {
			if (grafoRegioni == null)
				return null;
			Regione r = regioniName.get(name);
			if (r == null)
				return null;
			return new LinkedList<TypedWeightedEdge>(grafoRegioni.edgesOf(r));
		}
		return null;
	}
	
	public List<TypedWeightedEdge> getEdgesFrom(AggregationType aggGrafo, AggregationType areaLimite, String name) {
		LinkedList <TypedWeightedEdge> ritorno = new LinkedList<TypedWeightedEdge>();
		// Archi da un comune (- efficiente di edgesOf(...))
		if (areaLimite == AggregationType.COMUNE) {
			if (grafoComuni == null)
				return null;
			if (aggGrafo == AggregationType.COMUNE) {
				for (TypedWeightedEdge e : grafoComuni.edgeSet()) {
					Comune c1 = grafoComuni.getEdgeSource(e);
					Comune c2 = grafoComuni.getEdgeTarget(e);
					if (c1.getComune().equals(name) || c2.getComune().equals(name)) {
						ritorno.add(e);
					}
				}
				return ritorno;
			}
			else
				return null;
		}
		// Archi da una provincia
		if (areaLimite == AggregationType.PROVINCIA) {
			if (grafoProvince == null)
				return null;
			if (aggGrafo == AggregationType.COMUNE) {
				if (grafoComuni == null)
					return null;
				for (TypedWeightedEdge e : grafoComuni.edgeSet()) {
					Comune c1 = grafoComuni.getEdgeSource(e);
					Comune c2 = grafoComuni.getEdgeTarget(e);
					if (c1.getProvincia().equals(name) || c2.getProvincia().equals(name)) {
						ritorno.add(e);
					}
				}
				return ritorno;
			}
			else if (aggGrafo == AggregationType.PROVINCIA) {
				for (TypedWeightedEdge e : grafoProvince.edgeSet()) {
					Provincia p1 = grafoProvince.getEdgeSource(e);
					Provincia p2 = grafoProvince.getEdgeTarget(e);
					if (p1.getSigla().equals(name) || p2.getSigla().equals(name) ||
						p1.getProvincia().equals(name) || p2.getProvincia().equals(name)) {
						ritorno.add(e);
					}
				}
				return ritorno;
			}
			else
				return null;
		}
		// Archi da una Regione
		if (areaLimite == AggregationType.REGIONE) {
			if (grafoRegioni == null)
				return null;
			if (aggGrafo == AggregationType.COMUNE) {
				if (grafoComuni == null)
					return null;
				for (TypedWeightedEdge e : grafoComuni.edgeSet()) {
					Comune c1 = grafoComuni.getEdgeSource(e);
					Comune c2 = grafoComuni.getEdgeTarget(e);
					if (c1.getRegione().equals(name) || c2.getRegione().equals(name)) {
						ritorno.add(e);
					}
				}
				return ritorno;
			}
			else if (aggGrafo == AggregationType.PROVINCIA) {
				if (grafoProvince == null)
					return null;
				for (TypedWeightedEdge e : grafoProvince.edgeSet()) {
					Provincia p1 = grafoProvince.getEdgeSource(e);
					Provincia p2 = grafoProvince.getEdgeTarget(e);
					if (regioniId.get(p1.getIdRegione()).getRegione().equals(name) || 
							regioniId.get(p2.getIdRegione()).getRegione().equals(name)) {
						ritorno.add(e);
					}
				}
				return ritorno;
			}
			else if (aggGrafo == AggregationType.REGIONE) {
				for (TypedWeightedEdge e : grafoRegioni.edgeSet()) {
					Regione r1 = grafoRegioni.getEdgeSource(e);
					Regione r2 = grafoRegioni.getEdgeTarget(e);
					if (r1.getRegione().equals(name) || r2.getRegione().equals(name)) {
						ritorno.add(e);
					}
				}
				return ritorno;
			}
			else
				return null;
		}
		return null;
	}
	
	public List<TypedWeightedEdge> getEdgesFrom(AggregationType agg) {
		if (agg == AggregationType.COMUNE) {
			if (grafoComuni == null)
				return null;
			return new LinkedList<TypedWeightedEdge>(grafoComuni.edgeSet());
		}
		
		if (agg == AggregationType.PROVINCIA) {
			if (grafoProvince == null)
				return null;
			return new LinkedList<TypedWeightedEdge>(grafoProvince.edgeSet());
		}
		
		if (agg == AggregationType.REGIONE) {
			if (grafoRegioni == null)
				return null;
			return new LinkedList<TypedWeightedEdge>(grafoRegioni.edgeSet());
		}
		return null;
	}
	
	//Metodi creazione Grafi:
	private void creaGrafoRegioni() {
		if (lvGrafi > 0) return;
		
		System.out.println("Aggregazione: regioni");
		//REGIONI
		//Vertici
		Graphs.addAllVertices(grafoRegioni, regioniId.values());
		System.out.println("#Regioni: "+grafoRegioni.vertexSet().size());
		for (Regione r : grafoRegioni.vertexSet()) {
			regioniOrdinate.add(r.getRegione());
		}
		regioniOrdinate.sort(null);
		
		//Archi
		for (Regione r1: regioniId.values()) {
			for (Regione r2: regioniId.values()) {
				if (!grafoRegioni.containsEdge(r1, r2) && !r1.equals(r2)) {
					double distanza = LatLngTool.distance(r1.getCentro(), r2.getCentro(), LengthUnit.KILOMETER);
					//Adiacenti
					if (distanza < (r1.getRaggio()+r2.getRaggio())*k) {
						grafoRegioni.addEdge(r1, r2, new TypedWeightedEdge(
								AggregationType.REGIONE, CrossType.REGIONALE, RelationType.ADIACENZA));
						TypedWeightedEdge e = grafoRegioni.getEdge(r1, r2);
						grafoRegioni.setEdgeWeight(e, distanza);
						
						adiacenzeRegioni.add(e);
					}
					//Non Adiacenti
					else {
						grafoRegioni.addEdge(r1, r2, new TypedWeightedEdge(
								AggregationType.REGIONE, CrossType.REGIONALE, RelationType.COLLEGAMENTO));
						TypedWeightedEdge e = grafoRegioni.getEdge(r1, r2);
						grafoRegioni.setEdgeWeight(e, distanza);
					}
				}
			}
		}
		System.out.println("#Archi Adiac: "+adiacenzeRegioni.size());
		System.out.println("#Archi reg: "+grafoRegioni.edgeSet().size());
	}
	
	private void creaGrafoProvince() {
		if (lvGrafi > 1) return;
		
		creaGrafoRegioni();
		System.out.println("Aggregazione: province");
		//PROVINCE
		//Vertici
		Graphs.addAllVertices(grafoProvince, provinceSigla.values());
		for (Provincia p : grafoProvince.vertexSet()) {
			provinceOrdinate.add(p.getSigla());
		}
		provinceOrdinate.sort(null);
		
		System.out.println("#Province: "+grafoProvince.vertexSet().size());
		//Archi adiacenti
		for (Provincia p1 : provinceSigla.values()) {
			for (Provincia p2 : provinceSigla.values()) {
				
				CrossType cross = CrossType.REGIONALE;
				if (p1.getIdRegione() == p2.getIdRegione())
					cross = CrossType.PROVINCIALE;

				if (!grafoProvince.containsEdge(p1, p2) && !p1.equals(p2)) {
					double distanza = LatLngTool.distance(p1.getCentro(), p2.getCentro(), LengthUnit.KILOMETER);

					if (distanza < (p1.getRaggio()+p2.getRaggio())*k) {
						grafoProvince.addEdge(p1, p2, new TypedWeightedEdge(
								AggregationType.PROVINCIA, cross, RelationType.ADIACENZA));
						TypedWeightedEdge e = grafoProvince.getEdge(p1, p2);
						grafoProvince.setEdgeWeight(e, distanza);
						
						adiacenzeProvincie.add(e);
						
						p1.addResidentiVicini(p2.getResidenti());
						p2.addResidentiVicini(p1.getResidenti());
					}
				}
			}
		}
		System.out.println("#Archi Adiac: "+adiacenzeProvincie.size());
		//Archi verso capoluogo re
		for (Provincia p1 : provinceSigla.values()) {
			Provincia p2 = provinceSigla.get(comuniName.get(regioniId.get(p1.getIdRegione()).getCapoluogo()).getProvincia());
			
			if (!grafoProvince.containsEdge(p1, p2) && !p1.equals(p2)) {
				double distanza = LatLngTool.distance(p1.getCentro(), p2.getCentro(), LengthUnit.KILOMETER);
				grafoProvince.addEdge(p1, p2, new TypedWeightedEdge(
						AggregationType.PROVINCIA, CrossType.PROVINCIALE, RelationType.SUBORDINAZIONE));
				TypedWeightedEdge e1 = grafoProvince.getEdge(p1, p2);
				grafoProvince.setEdgeWeight(e1, distanza);
				p1.addResidentiVicini(p2.getResidenti());
				p2.addResidentiVicini(p1.getResidenti());
			}
		}
		System.out.println("#Archi prov: "+grafoProvince.edgeSet().size());
	}
	
	private void creaGrafoComuni() {
		if (lvGrafi > 2) return;
		
		creaGrafoProvince();
		System.out.println("Aggregazione: comuni");
		//COMUNI
		//Vertici
		Graphs.addAllVertices(grafoComuni, comuniId.values());
		System.out.println("#Comuni: "+grafoComuni.vertexSet().size());
		for (Comune c : grafoComuni.vertexSet()) {
			comuniOrdinati.add(c.getComune());
		}
		comuniOrdinati.sort(null);
		
		//Archi diverse province
		for (TypedWeightedEdge e : adiacenzeProvincie) {
			Provincia p1 = grafoProvince.getEdgeSource(e);
			Provincia p2 = grafoProvince.getEdgeTarget(e);
			List <Arco> comuniVicini = dao.getComuniProvinceVicine(p1.getSigla(), p2.getSigla());
			for (Arco c : comuniVicini) {
				Comune c1 = comuniId.get(c.getVertice1());
				Comune c2 = comuniId.get(c.getVertice2());

				if (!grafoComuni.containsEdge(c1, c2)) {
					double distanza = LatLngTool.distance(c1.getCentro(), c2.getCentro(), LengthUnit.KILOMETER);

					if (distanza < (c1.getRaggio()+c2.getRaggio())*k){
						CrossType cross = CrossType.PROVINCIALE;
						if (!c1.getRegione().equals(c2.getRegione()))
							cross = CrossType.REGIONALE;
						
						grafoComuni.addEdge(c1, c2, new TypedWeightedEdge(
								AggregationType.COMUNE, cross, RelationType.ADIACENZA));
						TypedWeightedEdge e1 = grafoComuni.getEdge(c1, c2);
						grafoComuni.setEdgeWeight(e1, distanza);
						c1.addNumResidentiVicini(c2.getNumResidenti());
						c2.addNumResidentiVicini(c1.getNumResidenti());
					}
				}
			}
		}
		System.out.println("#Archi cross-provincia: "+grafoComuni.edgeSet().size());
		//Archi stessa provincia
		List <Arco> comuniVicini = dao.getComuniVicini();
		for (Arco c : comuniVicini) {
			Comune c1 = comuniId.get(c.getVertice1());
			Comune c2 = comuniId.get(c.getVertice2());

			if (!grafoComuni.containsEdge(c1, c2)) {
				double distanza = LatLngTool.distance(c1.getCentro(), c2.getCentro(), LengthUnit.KILOMETER);

				if (distanza < (c1.getRaggio()+c2.getRaggio())*k){
					grafoComuni.addEdge(c1, c2, new TypedWeightedEdge(
							AggregationType.COMUNE, CrossType.COMUNALE, RelationType.ADIACENZA));
					TypedWeightedEdge e1 = grafoComuni.getEdge(c1, c2);
					grafoComuni.setEdgeWeight(e1, distanza);
					c1.addNumResidentiVicini(c2.getNumResidenti());
					c2.addNumResidentiVicini(c2.getNumResidenti());
				}
			}
		}
		System.out.println("#Archi adiacenti: "+grafoComuni.edgeSet().size());
		//Archi verso capoluogo di provincia
		for(Comune c1 : comuniId.values()) {
			Comune c2 = capoluoghiPro.get(c1.getProvincia());

			if (!grafoComuni.containsEdge(c1, c2) && !c1.equals(c2)) {
				double distanza = LatLngTool.distance(c1.getCentro(), c2.getCentro(), LengthUnit.KILOMETER);
				grafoComuni.addEdge(c1, c2, new TypedWeightedEdge(
						AggregationType.COMUNE, CrossType.COMUNALE, RelationType.SUBORDINAZIONE));
				TypedWeightedEdge e1 = grafoComuni.getEdge(c1, c2);
				grafoComuni.setEdgeWeight(e1, distanza);
				c1.addNumResidentiVicini(c2.getNumResidenti());
				c2.addNumResidentiVicini(c1.getNumResidenti());
			}
		}
		System.out.println("#Comuni anche capoluoghi: "+grafoComuni.edgeSet().size());
		
		adiacenzeComuni = new LinkedList<TypedWeightedEdge>(grafoComuni.edgeSet());
	}
	
	public StatisticheEnte getStatistiche(AggregationType agg, String ref) {
		return statsContainer.get(agg).get(ref);
	}
	
	public void isFinita() {
		Status[] malati = {Status.MALATO};
		boolean inCorso = false;
		Map <AgeGroup, Integer> temp = statsNazione.getNumPopulationFromStati(malati);
		for (AgeGroup ag : AgeGroup.values()) {
			if (temp.get(ag) != 0) {
				inCorso = true;
			}
		}
		simulazioneAttiva = inCorso;
	}
	
	
	public boolean verificaContinuazione() {
		isFinita();
		Status[] totPop = {Status.SANO, Status.CONTAGIOSO, Status.GUARITO, Status.MALATO, Status.CURATO, Status.MORTO};
		
		switch (parSimulazione.getHopCondition()) {
		case FINE_EPIDEMIA:
			System.out.println(statsNazione);
			Status[] contagiosi = {Status.CONTAGIOSO};
			Map <AgeGroup, Integer> temp = statsNazione.getNumPopulationFromStati(contagiosi);
			for (AgeGroup ag : AgeGroup.values()) {
				if (temp.get(ag) != 0)
					return true;
			}
			simulazioneAttiva = false;
			return false;
		case ESTINZIONE_VIRUS:
			System.out.println(statsNazione);
			Status[] portatoriVirus = {Status.CONTAGIOSO, Status.MALATO};
			temp = statsNazione.getNumPopulationFromStati(portatoriVirus);
			for (AgeGroup ag : AgeGroup.values()) {
				if (temp.get(ag) != 0)
					return true;
			}
			simulazioneAttiva = false;
			return false;
		case NUM_GIORNI:
			if (giornoCorrente - giornoUltimoStop >= parSimulazione.getNumGiorniHop()) {
				giornoUltimoStop = giornoCorrente;
				return false;
			}
			return true;
		case NUM_MALATI:
			Status[] malati = {Status.MALATO};
			int totMalati = 0;
			temp = statsNazione.getNumPopulationFromStati(malati);
			for (AgeGroup ag : AgeGroup.values()) {
				totMalati += temp.get(ag);
			}
			if (totMalati >= parSimulazione.getNumStatoHop()) {
				giornoUltimoStop = giornoCorrente;
				return false;
			}
			return true;
		case NUM_MORTI:
			Status[] morti = {Status.MORTO};
			int totMorti = 0;
			temp = statsNazione.getNumPopulationFromStati(morti);
			for (AgeGroup ag : AgeGroup.values()) {
				totMorti += temp.get(ag);
			}
			if (totMorti >= parSimulazione.getNumStatoHop()) {
				giornoUltimoStop = giornoCorrente;
				return false;
			}
			return true;
		case PER_MALATI:
			int tot = 0;
			totMalati = 0;
			Map <AgeGroup, Integer> tempMalati = statsNazione.getNumStato(Status.MALATO);
			temp = statsNazione.getNumPopulationFromStati(totPop);
			for (AgeGroup ag : AgeGroup.values()) {
				tot += temp.get(ag);
				totMalati += tempMalati.get(ag);
			}
			if (((double) totMalati) / tot >= parSimulazione.getPerStatoHop()) {
				giornoUltimoStop = giornoCorrente;
				return false;
			}
			return true;
		case PER_MORTI:
			tot = 0;
			totMorti = 0;
			Map <AgeGroup, Integer> tempMorti = statsNazione.getNumStato(Status.MORTO);
			temp = statsNazione.getNumPopulationFromStati(totPop);
			for (AgeGroup ag : AgeGroup.values()) {
				tot += temp.get(ag);
				totMorti += tempMorti.get(ag);
			}
			if (((double) totMorti) / tot >= parSimulazione.getPerStatoHop()) {
				giornoUltimoStop = giornoCorrente;
				return false;
			}
			return true;
		}
		return false;
	}

	public void setParSimulazione(ParametriSimulazione parSimulazione) {
		this.parSimulazione = parSimulazione;
	}

	public void setParVirus(ParametriVirus parVirus) {
		this.parVirus = parVirus;
	}

	public void setParMondo(ParametriMondo parMondo) {
		this.parMondo = parMondo;
	}

	public void setNuoviContagi(String value, Map<AgeGroup, Integer> temp) throws Exception {
		StatisticheEnte stats;
		AggregationType agg = parSimulazione.getAggSimulazione();
		
		if (value.equals("Random!")) {
			int i = (int) (statsContainer.get(agg).size()*Math.random());
			value = ordinati.get(agg).get(i);
		}
		
		stats = statsContainer.get(agg).get(value);
		
		if (stats == null)
			throw new Exception("Errore: \""+value+"\" non esiste.");
		
		stats.checkSani(temp);
		valoriT0.put(value, temp);
	}

	public List <String> getNuoviContagi() {
		LinkedList<String> temp = new LinkedList<String>();
		for (String ref : valoriT0.keySet()) {
			temp.add(ref+": Scuola: "+valoriT0.get(ref).get(AgeGroup.SCUOLA)+
							": Lavoro: "+valoriT0.get(ref).get(AgeGroup.LAVORO)+
							": Pensione: "+valoriT0.get(ref).get(AgeGroup.PENSIONE));
		}
		return temp;
	}

	public void resetNuoviContagi() {
		valoriT0 = new HashMap<>();
	}
	
	public List <String> getEnti(AggregationType agg) {
		return ordinati.get(agg);
	}
}