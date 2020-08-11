package it.polito.tdp.covid.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.covid.model.*;

public class CovidDAO {
	
	public List<Comune> listAllCities(Map <Integer, Comune> comuniId, Map<String, Comune> comuniName){
		String sql = "SELECT *\r\n" + 
				"FROM italy_cities, italy_geo\r\n" + 
				"WHERE italy_cities.istat = italy_geo.istat;";
		List<Comune> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Comune comune = new Comune(res.getInt("italy_cities.istat"), res.getString("italy_cities.comune"),
						res.getString("regione"), res.getString("provincia"), res.getDouble("superficie"),
						res.getInt("num_residenti"), res.getDouble("lat"), res.getDouble("lng"));
				
				result.add(comune);
				if (comuniId != null)
					comuniId.put(comune.getIstat(), comune);
				if (comuniName != null)
					comuniName.put(comune.getComune(), comune);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}
	
	public void listCapoluoghiProvincia(Map<String, Comune> capoluoghi, Map<Integer, Comune> comuni) {
		String sql = "SELECT c.istat, c.comune, p.provincia, p.sigla\r\n" + 
				"FROM italy_provincies AS p, italy_cities AS c\r\n" + 
				"WHERE p.provincia IN (SELECT c.comune\r\n" + 
				"							FROM italy_cities AS c)\r\n" + 
				"		AND p.provincia = c.comune\r\n" + 
				"UNION\r\n" + 
				"SELECT c.istat, c.comune, p.provincia, p.sigla\r\n" + 
				"FROM italy_provincies AS p, italy_cities AS c\r\n" + 
				"WHERE p.provincia NOT IN (SELECT c.comune\r\n" + 
				"							FROM italy_cities AS c)\r\n" + 
				"		AND p.capoluogo IN (SELECT c.comune\r\n" + 
				"							FROM italy_cities AS c)\r\n" + 
				"		AND p.capoluogo = c.comune;";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				capoluoghi.put(res.getString("sigla"), comuni.get(res.getInt("istat")));
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void listCapoluoghiRegione(Map<String, Comune> capoluoghi, Map<Integer, Comune> comuni) {
		String sql = "SELECT c.istat, c.comune\r\n" + 
				"FROM italy_cities AS c, italy_regions AS r\r\n" + 
				"WHERE c.comune = r.capoluogo";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				capoluoghi.put(res.getString("c.comune"), comuni.get(res.getInt("istat")));
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Provincia> listAllProvincies(Map <String, Provincia> province, Map<String, Provincia> provinceName){
		String sql = "SELECT italy_provincies.sigla, italy_provincies.provincia, italy_provincies.superficie, \r\n" + 
				"		italy_provincies.residenti, italy_provincies.num_comuni, italy_provincies.id_regione, \r\n" + 
				"		AVG(lat) AS lat, AVG(lng) AS lng\r\n" + 
				"FROM italy_cities, italy_geo, italy_provincies\r\n" + 
				"WHERE italy_cities.istat = italy_geo.istat AND italy_cities.provincia = italy_provincies.sigla\r\n" + 
				"GROUP BY italy_provincies.sigla, italy_provincies.provincia, italy_provincies.superficie, \r\n" + 
				"		italy_provincies.residenti, italy_provincies.num_comuni, italy_provincies.id_regione;";
		List<Provincia> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Provincia provincia = new Provincia(res.getString("sigla"), res.getString("provincia"),
						res.getDouble("superficie"), res.getInt("residenti"), res.getInt("num_comuni"),
						res.getInt("id_regione"), res.getDouble("lat"), res.getDouble("lng"));
				
				result.add(provincia);
				if (province != null)
					province.put(provincia.getSigla(), provincia);
				if (provinceName != null)
					provinceName.put(provincia.getProvincia(), provincia);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}
	
	public List<Regione> listAllRegions(Map <Integer, Regione> regioniId, Map <String, Regione> regioniName){
		String sql = "SELECT italy_regions.id_regione, italy_regions.regione, italy_regions.superficie, \r\n" + 
				"		italy_regions.num_residenti, italy_regions.num_comuni, italy_regions.num_provincie, \r\n" + 
				"		italy_regions.capoluogo, AVG(lat) AS lat, AVG(lng) AS lng\r\n" + 
				"FROM italy_cities, italy_geo, italy_regions\r\n" + 
				"WHERE italy_cities.istat = italy_geo.istat AND italy_cities.regione = italy_regions.regione\r\n" + 
				"GROUP BY italy_regions.id_regione, italy_regions.regione, italy_regions.superficie, \r\n" + 
				"		italy_regions.num_residenti, italy_regions.num_comuni, italy_regions.num_provincie;";
		List<Regione> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Regione regione = new Regione(res.getInt("id_regione"), res.getString("regione"),
						res.getDouble("superficie"), res.getInt("num_residenti"), res.getInt("num_comuni"),
						res.getInt("num_provincie"), res.getString("capoluogo"), res.getDouble("lat"), res.getDouble("lng"));
				
				result.add(regione);
				if (regioniId != null)
					regioniId.put(regione.getIdRegione(), regione);
				if (regioniName != null)
					regioniName.put(regione.getRegione(), regione);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}

	public List<Arco> getProvinceRegioniVicine(int idRegione1, int idRegione2) {
		String sql = "SELECT p1.sigla AS s1, p2.sigla AS s2\r\n" + 
				"FROM italy_provincies AS p1, italy_provincies AS p2\r\n" + 
				"WHERE p1.id_regione = ? AND p2.id_regione = ?;";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idRegione1);
			st.setInt(2, idRegione2);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Arco(res.getString("s1"), res.getString("s2"), 0.0));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}

	public List<Arco> getProvinceRegione(int idRegione) {
		String sql = "SELECT p1.sigla AS s1, p2.sigla AS s2 \r\n" + 
				"FROM italy_provincies AS p1, italy_provincies AS p2\r\n" + 
				"WHERE p1.id_regione = ? AND p1.id_regione = p2.id_regione AND\r\n" + 
				"		p1.provincia < p2.provincia;";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idRegione);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Arco(res.getString("s1"), res.getString("s2"), 0.0));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}

	public List<Arco> getComuniProvinceVicine(String sigla1, String sigla2) {
		String sql = "SELECT c1.istat AS com1, c2.istat AS com2\r\n" + 
				"FROM italy_cities AS c1, italy_cities AS c2\r\n" + 
				"WHERE c1.provincia = ? AND c2.provincia = ?;";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, sigla1);
			st.setString(2, sigla2);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Arco(res.getInt("com1"), res.getInt("com2"), 0.0));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}

	public List<Arco> getComuniVicini() {
		String sql = "SELECT c1.istat AS com1, c2.istat AS com2\r\n" + 
				"FROM italy_cities AS c1, italy_cities AS c2\r\n" + 
				"WHERE c1.provincia = c2.provincia AND c1.istat < c2.istat;";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Arco(res.getInt("com1"), res.getInt("com2"), 0.0));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}

	public Double getItalianAvgDensity() {
		String sql = "SELECT SUM(r.superficie) AS sup, SUM(r.num_residenti) AS ab\r\n" + 
				"FROM italy_regions AS r";
		Connection conn = DBConnect.getConnection();
		Double result = null;
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if (res.next()) {
				result = ((double)res.getInt("ab")) / res.getDouble("sup");
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}

	public int getItalianPopulation() {
		String sql = "SELECT SUM(r.num_residenti) AS ab\r\n" + 
				"FROM italy_regions AS r";
		Connection conn = DBConnect.getConnection();
		Integer result = null;
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			if (res.next()) {
				result = res.getInt("ab");
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}
}
