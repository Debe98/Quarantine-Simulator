package it.polito.tdp.covid.model;

public class Arco {

	private Integer Vertice1;
	private Integer Vertice2;
	
	private String VerticeA;
	private String VerticeB;
	
	private double peso;
	
	public Arco(Integer vertice1, Integer vertice2, double peso) {
		super();
		Vertice1 = vertice1;
		Vertice2 = vertice2;
		VerticeA = null;
		VerticeB = null;
		this.peso = peso;
	}
	
	
	public Arco(String verticeA, String verticeB, double peso) {
		super();
		Vertice1 = null;
		Vertice2 = null;
		VerticeA = verticeA;
		VerticeB = verticeB;
		this.peso = peso;
	}

	public Integer getVertice1() {
		return Vertice1;
	}

	public void setVertice1(Integer vertice1) {
		Vertice1 = vertice1;
	}

	public Integer getVertice2() {
		return Vertice2;
	}

	public void setVertice2(Integer vertice2) {
		Vertice2 = vertice2;
	}

	public String getVerticeA() {
		return VerticeA;
	}

	public void setVerticeA(String verticeA) {
		VerticeA = verticeA;
	}

	public String getVerticeB() {
		return VerticeB;
	}

	public void setVerticeB(String verticeB) {
		VerticeB = verticeB;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}
}
