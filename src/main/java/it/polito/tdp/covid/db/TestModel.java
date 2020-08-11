package it.polito.tdp.covid.db;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.covid.model.Model;
import it.polito.tdp.covid.model.TypedWeightedEdge;
import it.polito.tdp.covid.model.TypedWeightedEdge.*;
import it.polito.tdp.covid.stats.StatisticheEnte.AgeGroup;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model model = new Model();
		Map <AgeGroup, Integer> aiai = new HashMap<>();
		for (AgeGroup ag : AgeGroup.values()) {
			aiai.put(ag, 1000);
		}
		model.creaGrafo();
		/*
		model.startSimulazione("Piemonte", aiai);
		System.out.println(model.getStatistiche(AggregationType.REGIONE, "Piemonte"));
		*/
		/*
		model.startSimulazione("TO", aiai);
		System.out.println(model.getStatistiche(AggregationType.PROVINCIA, "TO"));
		*/
		///*
		//model.startSimulazione("Torino", aiai);
		System.out.println(model.getStatistiche(AggregationType.COMUNE, "Torino"));
		System.out.println(model.getStatistiche(AggregationType.PROVINCIA, "TO"));
		System.out.println(model.getStatistiche(AggregationType.REGIONE, "Sardegna"));
		//*/
		
	
		
		/*
		for (int i = 0; i < (int) 1000000000; i++) {
			//NUOVO!
			if (Math.random() < 0.000000001) {
				double temp = Math.random();
				if (temp <= 0.2)
					System.out.println("AHHHH");
				else if (temp <= 0.7)
					System.out.println("EHHHH");
				else
					System.out.println("IHHHH");
			}
		}
		/*
		System.out.println("FINITO");
		for (int i = 0;  i<=1000000000; i++) {
			if (i % 100000000 == 0)
				System.out.println(i);
		}
		/*
		System.out.println(Double.MAX_VALUE);
		Model model = new Model();
		
		try {
			model.creaGrafo(AggregationType.COMUNE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		for (TypedWeightedEdge e : model.getEdgesFrom("TO", AggregationType.PROVINCIA)) {
			System.out.println(e+"\n");
		}
		/*
		for (TypedWeightedEdge e : model.getEdgesFrom(AggregationType.COMUNE, AggregationType.REGIONE, "Valle d'Aosta")) {	
			if (e.getAttraversamento() == CrossType.REGIONALE)
				System.out.println(e+"\n");
		}
		/*
		for (TypedWeightedEdge e : model.getEdgesFrom(AggregationType.COMUNE)) {
			if (//e.getAttraversamento() == CrossType.PROVINCIALE || 
				e.getAttraversamento() == CrossType.REGIONALE)
				System.out.println(e+"\n");
		}
		/*
		for (TypedWeightedEdge e : model.getEdgesFrom("Torino", AggregationType.COMUNE)) {
			System.out.println(e+"\n");
		}
		for (TypedWeightedEdge e : model.getEdgesFrom("TO", AggregationType.PROVINCIA)) {
			System.out.println(e+"\n");
		}
		for (TypedWeightedEdge e : model.getEdgesFrom("Piemonte", AggregationType.REGIONE)) {
			System.out.println(e+"\n");
		}
		
		
		for (TypedWeightedEdge e : model.getEdgesFrom("Ceresole Reale", AggregationType.COMUNE)) {
			System.out.println(e+"\n");
		}
		for (TypedWeightedEdge e : model.getEdgesFrom("Noasca", AggregationType.COMUNE)) {
			System.out.println(e+"\n");
		}
		for (TypedWeightedEdge e : model.getEdgesFrom("Laigueglia", AggregationType.COMUNE)) {
			System.out.println(e+"\n");
		}
		for (TypedWeightedEdge e : model.getEdgesFrom("Alba", AggregationType.COMUNE)) {
			System.out.println(e+"\n");
		}
		for (TypedWeightedEdge e : model.getEdgesFrom("Messina", AggregationType.COMUNE)) {
			System.out.println(e+"\n");
		}
		*/
	}

}
