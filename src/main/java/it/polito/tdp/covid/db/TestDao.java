package it.polito.tdp.covid.db;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		CovidDAO dao = new CovidDAO();
		System.out.println("Comuni:");
		System.out.println(dao.listAllCities(null, null));
		System.out.println("Province:");
		System.out.println(dao.listAllProvincies(null, null));
		System.out.println("Regioni:");
		System.out.println(dao.listAllRegions(null, null));
	}

}
