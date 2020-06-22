/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	if (boxAnno.getItems().isEmpty()) {
    	    txtResult.setText("Prima crea il grafo!");
    	    return;
    	}
    	if (boxAnno.getValue() == null) {
    		txtResult.setText("Devi scegliere un anno");
    	    return;
    	}
    	model.creaGrafo(boxAnno.getValue());
    	List <Arco> archi = model.getDistanzeVertici();
    	
    	txtResult.clear();
    	for (Arco a : archi) {
    		txtResult.appendText(a+"\n");
    	}
    	btnSimula.setDisable(false);
    	List <Integer> giorni = new LinkedList<Integer>();
    	List <Integer> mesi = new LinkedList<Integer>();
    	
    	for (int i = 1; i < 13 ; i++) {
    		mesi.add(i);
    	}
    	for (int i = 1; i < 32 ; i++) {
    		giorni.add(i);
    	}
    	
    	boxGiorno.getItems().clear();
    	boxMese.getItems().clear();
    	
    	boxGiorno.getItems().addAll(giorni);
    	boxMese.getItems().addAll(mesi);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	if (boxGiorno.getItems().isEmpty()) {
    	    txtResult.setText("Prima crea il grafo!");
    	    return;
    	}
    	if (boxGiorno.getValue() == null) {
    		txtResult.setText("Devi scegliere un opzione");
    	    return;
    	}
    	if (boxMese.getItems().isEmpty()) {
    	    txtResult.setText("Prima crea il grafo!");
    	    return;
    	}
    	if (boxMese.getValue() == null) {
    		txtResult.setText("Devi scegliere un opzione");
    	    return;
    	}
    	String raw = txtN.getText();
    	int n;
    	try {
    		n = Integer.parseInt(raw);
    		
    	}
    	catch (NumberFormatException e) {
    		txtResult.appendText("Il parametro inserito non è un numero\n");
    		return;
    	}
    	if (n < 1 || n > 10) {
    		txtResult.appendText("Il numero inserito deve essere compreso tra 1 e 10\n");
    		return;
    	}
    	model.simula(boxGiorno.getValue(), boxMese.getValue(), boxAnno.getValue(), n);
    	List <Integer> stat = model.getStatSim();
    	
    	txtResult.setText("Gestiti "+stat.get(1)+" ("+stat.get(2)+"), gestiti male "+stat.get(0));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().clear();
    	boxAnno.getItems().addAll(model.getDistinctAnni());
    	btnSimula.setDisable(true);
    }
}
