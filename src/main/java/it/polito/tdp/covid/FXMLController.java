/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.covid;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

import org.jheaps.AddressableHeap.Handle;

import com.sun.scenario.effect.impl.state.BoxRenderState;

import it.polito.tdp.covid.model.Model;
import it.polito.tdp.covid.model.ParametriMondo;
import it.polito.tdp.covid.model.ParametriSimulazione;
import it.polito.tdp.covid.model.ParametriSimulazione.ChanceType;
import it.polito.tdp.covid.model.ParametriSimulazione.ReturnCondition;
import it.polito.tdp.covid.model.ParametriSimulazione.SimulationType;
import it.polito.tdp.covid.model.ParametriVirus;
import it.polito.tdp.covid.model.ParametriVirus.HandlingType;
import it.polito.tdp.covid.model.TypedWeightedEdge.AggregationType;
import it.polito.tdp.covid.model.Utility;
import it.polito.tdp.covid.stats.StatisticheEnte;
import it.polito.tdp.covid.stats.StatisticheEnte.AgeGroup;
import it.polito.tdp.covid.stats.StatisticheEnte.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabHome;

    @FXML
    private ComboBox<AggregationType> boxAggregazione;
    
    @FXML
    private Label tPrec;

    @FXML
    private Label tEsec;

    @FXML
    private Label tPrep;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Tab tabVirus;

    @FXML
    private TextField txtTContagio;

    @FXML
    private TextField txtTMalattia;

    @FXML
    private TextField txtB0;

    @FXML
    private TextField txtToMalatoScuola;

    @FXML
    private TextField txtToMortoPensione;

    @FXML
    private TextField txtToMortoLavoro;

    @FXML
    private TextField txtToMortoScuola;

    @FXML
    private TextField txtToMalatoPensione;

    @FXML
    private TextField txtToMalatoLavoro;

    @FXML
    private TextArea txtResultVirus;

    @FXML
    private Button btnAggiornaVirus;

    @FXML
    private Button btnResetVirus;

    @FXML
    private Tab tabContromisure;

    @FXML
    private TextField noScuolaScuola;

    @FXML
    private TextField lockLavoro;

    @FXML
    private TextField reducedLavoro;

    @FXML
    private TextField reducedScuola;

    @FXML
    private TextField noActPensione;

    @FXML
    private TextField lockScuola;

    @FXML
    private TextField lockPensione;

    @FXML
    private TextField noActLavoro;

    @FXML
    private TextField noActScuola;

    @FXML
    private TextField reducedPensione;

    @FXML
    private TextField noScuolaPensione;

    @FXML
    private TextField noScuolaLavoro;

    @FXML
    private TextArea txtResultContromisure;

    @FXML
    private Button btnAggiornaContromisure;

    @FXML
    private Button btnResetContromisure;

    @FXML
    private Tab tabMondo;

    @FXML
    private TextField txtPercScuola;

    @FXML
    private TextField txtPercPensione;

    @FXML
    private TextField txtPercLavoro;

    @FXML
    private TextField txtScuolaAttrComunale;

    @FXML
    private TextField txtPensioneAttrProvinciale;

    @FXML
    private TextField txtLavoroAttrProvinciale;

    @FXML
    private TextField txtScuolaAttrProvinciale;

    @FXML
    private TextField txtPensioneAttrComunale;

    @FXML
    private TextField txtLavoroAttrComunale;

    @FXML
    private TextField txtPensioneNoShift;

    @FXML
    private TextField txtLavoroNoShift;

    @FXML
    private TextField txtScuolaNoShift;

    @FXML
    private TextField txtPensioneAttrRegionale;

    @FXML
    private TextField txtLavoroAttrRegionale;

    @FXML
    private TextField txtScuolaAttrRegionale;

    @FXML
    private TextArea txtResultMondo;

    @FXML
    private Button btnAggiornaMondo;

    @FXML
    private Button btnResetMondo;

    @FXML
    private Tab tabSimulazione;

    @FXML
    private ComboBox<SimulationType> boxTipoSimulazione;

    @FXML
    private Button btnAddNewCont;
    
    @FXML
    private Button btnResetSimulazione;

    @FXML
    private Button btnAggiornaSimulazione;

    @FXML
    private ComboBox<ReturnCondition> boxTerminazione;

    @FXML
    private TextField txtTerminazione;

    @FXML
    private ComboBox<ChanceType> boxProbabilita;
    
    @FXML
    private TextField txtNuoviScuola;

    @FXML
    private TextField txtNuoviPensione;

    @FXML
    private TextField txtNuoviLavoro;
    
    @FXML
    private ComboBox<String> boxNuovi;
    
    @FXML
    private CheckBox randomNuovi;

    @FXML
    private TextArea txtResultSimulazione;

    @FXML
    private Tab tabParametrizzata;

    @FXML
    private Button btnResetParametrizzata;

    @FXML
    private Button btnStartSimulazione;

    @FXML
    private TextArea txtResultParametrizzata;

    @FXML
    private TextField txtValueChiusura;

    @FXML
    private ComboBox<AggregationType> boxEnteChiusura;

    @FXML
    private ComboBox<Status> boxStatusChiusura;

    @FXML
    private Button btnAddChiusura;

    @FXML
    private Button btnRemoveChiusura;

    @FXML
    private ComboBox<HandlingType> boxHandling;

    @FXML
    private ComboBox<String> boxEnteHandling;

    @FXML
    private TextField txtValueHandling;

    @FXML
    private ComboBox<Status> boxStatusHandling;

    @FXML
    private Button btnAddHandling;

    @FXML
    private Button btnRemoveHandling;

    @FXML
    private CheckBox isPercentualeChiusura;

    @FXML
    private CheckBox isPercentualeHandling;

    @FXML
    private Tab tabLive;

    @FXML
    private Button btnResetParametriLive;

    @FXML
    private Button btnStartLive;

    @FXML
    private TextArea txtResultLive;

    @FXML
    private ComboBox<AggregationType> boxTipoEnteChiusuraLive;

    @FXML
    private ComboBox<String> boxEnteChiusuraLive;

    @FXML
    private Button btnAddChiusuraLive;

    @FXML
    private Button btnRemoveChiusuraLive;

    @FXML
    private ComboBox<HandlingType> boxHandlingLive;

    @FXML
    private ComboBox<AggregationType> boxTipoEnteHandlingLive;

    @FXML
    private ComboBox<String> boxEnteHandlingLive;

    @FXML
    private Button btnAddHandlingLive;

    @FXML
    private Button btnRemoveHandlingLive;

    @FXML
    private Tab tabRisultati;

    @FXML
    private Label txtGornoRisultato;

    @FXML
    private Label txtIsTerminata;

    @FXML
    private ComboBox<AggregationType> boxScegliAggregazioneRisultato;

    @FXML
    private ComboBox<String> boxScegliEnteRisultato;
    
    @FXML
    private CheckBox nazionale;

    @FXML
    private Label result03;

    @FXML
    private Label result02;

    @FXML
    private Label result01;

    @FXML
    private Label result00;

    @FXML
    private Label result04;

    @FXML
    private Label result24;

    @FXML
    private Label result15;

    @FXML
    private Label result05;

    @FXML
    private Label result14;

    @FXML
    private Label result23;

    @FXML
    private Label result13;

    @FXML
    private Label result22;

    @FXML
    private Label result12;

    @FXML
    private Label result21;

    @FXML
    private Label result11;

    @FXML
    private Label result20;

    @FXML
    private Label result10;

    @FXML
    private Label result25;

    @FXML
    private TextArea txtResultRisultati;
    
    @FXML
    private Tab tabGrafici;
    
    @FXML
    private Tab tabGrafici2;
    
    @FXML
    private LineChart<String, Number> grafico1;
    
    @FXML
    private LineChart<String, Number> malatiGrafico;
    
    @FXML
    private LineChart<String, Number> mortiGrafico;
    
    @FXML
    private BarChart<String, Number> grafico4;
    
    //HOME
    @FXML
    void aggregazione(ActionEvent event) {
    	AggregationType agg = boxAggregazione.getValue();
    	handleSetAggregazione(agg);
    }
    
    @FXML
    void creaGrafo(ActionEvent event) {
    	utilCreaGrafo();
    	AggregationType agg = boxAggregazione.getValue();
    	model.getParSimulazione().setAggSimulazione(agg);
    	
    	model.creaGrafo();
    	btnCreaGrafo.setText("Prepara Simulazione".toUpperCase());
    	btnCreaGrafo.setDisable(false);
    	tabContromisure.setDisable(false);
    	tabMondo.setDisable(false);
    	tabVirus.setDisable(false);
    	tabSimulazione.setDisable(false);
    	handleTipoSimulazione();
    	tabLive.setDisable(true);
    	tabParametrizzata.setDisable(true);
    	boxNuovi.getItems().clear();
    	boxNuovi.getItems().addAll(model.getEnti(model.getParSimulazione().getAggSimulazione()));
    	handleContromisure();
    	handleLive();
    	
    	resetVirus(event);
    	resetMondo(event);
    	resetContromisure(event);
    	resetSimulazione(event);
    }
    
    void utilCreaGrafo() {
    	btnCreaGrafo.setText("In corso...");
    	btnCreaGrafo.setDisable(true);
    }
    
    void handleSetAggregazione(AggregationType agg) {
    	switch (agg) {
		case COMUNE:
			tPrep.setText("LUNGA");
			tEsec.setText("COMPLESSA");
			tPrec.setText("MASSIMA");
			/*
			txtResultSimulazione.setText("Attenzione:\nLa preparazione della simulazione"
					+ "può richiedere fino a qualche minuto.\nSi consiglia solo in caso"
					+ "di computer con buona capacità di calcolo!");
			*/
			break;
		case PROVINCIA:
			tPrep.setText("BREVE");
			tEsec.setText("RAPIDA");
			tPrec.setText("BUONA");
			//txtResultSimulazione.setText("Migliore Compromesso tra velocità e precisione!");
			break;
		case REGIONE:
			tPrep.setText("IMMEDIATA");
			tEsec.setText("VELOCE");
			tPrec.setText("ACCETTABILE");
			//txtResultSimulazione.setText("Risultati accettabili in brevissimo tempo.");
			break;
    	}
    }
    
    
    
    // VIRUS
    
    void handleVirus() {
    	ParametriVirus parVirus = model.getParVirus();
    	txtB0.setText(parVirus.getParB0()+"");
    	txtTContagio.setText(parVirus.getAvgTime(Status.CONTAGIOSO)+"");
    	txtTMalattia.setText(parVirus.getAvgTime(Status.MALATO)+"");
    	txtToMalatoScuola.setText(parVirus.getAgeGroupFromContagiosoToMalato(AgeGroup.SCUOLA)+"");
    	txtToMalatoLavoro.setText(parVirus.getAgeGroupFromContagiosoToMalato(AgeGroup.LAVORO)+"");
    	txtToMalatoPensione.setText(parVirus.getAgeGroupFromContagiosoToMalato(AgeGroup.PENSIONE)+"");
    	txtToMortoScuola.setText(parVirus.getAgeGroupDeathsRatio(AgeGroup.SCUOLA)+"");
    	txtToMortoLavoro.setText(parVirus.getAgeGroupDeathsRatio(AgeGroup.LAVORO)+"");
    	txtToMortoPensione.setText(parVirus.getAgeGroupDeathsRatio(AgeGroup.PENSIONE)+"");
    }
    
    @FXML
    void resetVirus(ActionEvent event) {
    	txtResultVirus.setText("Reset effettuato!");
    	
    	model.getParVirus().resetVirus();
    	handleVirus();
    }
    
    @FXML
    void aggiornaVirus(ActionEvent event) {
    	ParametriVirus parVirus = model.getParVirus();
    	txtResultVirus.clear();
    	try {
    		parVirus.setParB0(checkPercentuale(txtB0.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("B0: "+e.getMessage()+"\n");
		}
    	
    	try {
    		parVirus.setAvgTime(Status.CONTAGIOSO, checkNumeroVirgola(txtTContagio.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Contagio: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setAvgTime(Status.MALATO, checkNumeroVirgola(txtTMalattia.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Malattia: "+e.getMessage()+"\n");
		}
    	
    	try {
    		parVirus.setAgeGroupFromContagiosoToMalato(AgeGroup.SCUOLA, checkPercentuale(txtToMalatoScuola.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Malato - Scuola: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setAgeGroupFromContagiosoToMalato(AgeGroup.LAVORO, checkPercentuale(txtToMalatoLavoro.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Malato - Lavoro: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setAgeGroupFromContagiosoToMalato(AgeGroup.PENSIONE, checkPercentuale(txtToMalatoPensione.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Malato - Pensione: "+e.getMessage()+"\n");
		}
    	
    	try {
    		parVirus.setAgeGroupDeathsRatio(AgeGroup.SCUOLA, checkPercentuale(txtToMortoScuola.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Morto - Scuola: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setAgeGroupDeathsRatio(AgeGroup.LAVORO, checkPercentuale(txtToMortoLavoro.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Morto - Lavoro: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setAgeGroupDeathsRatio(AgeGroup.PENSIONE, checkPercentuale(txtToMortoPensione.getText()));
    	} catch (Exception e) {
			txtResultVirus.appendText("Morto - Pensione: "+e.getMessage()+"\n");
		}    	
    	
    	handleVirus();
    }
    
    
    
    //CONTROMISURE
    
    @FXML
    void aggiornaContromisure(ActionEvent event) {
    	ParametriVirus parVirus = model.getParVirus();
    	txtResultContromisure.clear();
    	txtResultContromisure.setText("Aggiornato!\n");
    	try {
    		parVirus.setSpreadReduction(AgeGroup.SCUOLA, HandlingType.NO_ACTS, checkPercentuale(noActScuola.getText()));;
    	} catch (Exception e) {
			txtResultContromisure.appendText("Scuola - None: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.LAVORO, HandlingType.NO_ACTS, checkPercentuale(noActLavoro.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Lavoro - None: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.PENSIONE, HandlingType.NO_ACTS, checkPercentuale(noActPensione.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Pensione - None: "+e.getMessage()+"\n");
		}
    	
    	try {
    		parVirus.setSpreadReduction(AgeGroup.SCUOLA, HandlingType.REDUCED, checkPercentuale(reducedScuola.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Scuola - Misure base: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.LAVORO, HandlingType.REDUCED, checkPercentuale(reducedLavoro.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Lavoro - Misure base: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.PENSIONE, HandlingType.REDUCED, checkPercentuale(reducedPensione.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Pensione - Misure base: "+e.getMessage()+"\n");
		}
    	
    	try {
    		parVirus.setSpreadReduction(AgeGroup.SCUOLA, HandlingType.NO_SCHOOL, checkPercentuale(noScuolaScuola.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Scuola - No scuola: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.LAVORO, HandlingType.NO_SCHOOL, checkPercentuale(noScuolaLavoro.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Lavoro - No scuola: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.PENSIONE, HandlingType.NO_SCHOOL, checkPercentuale(noScuolaPensione.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Pensione - No scuola: "+e.getMessage()+"\n");
    	}
    	
    	try {
    		parVirus.setSpreadReduction(AgeGroup.SCUOLA, HandlingType.LOCKDOWN, checkPercentuale(lockScuola.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Scuola - Lockdown: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.LAVORO, HandlingType.LOCKDOWN, checkPercentuale(lockLavoro.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Lavoro - Lockdown: "+e.getMessage()+"\n");
		}
    	try {
    		parVirus.setSpreadReduction(AgeGroup.PENSIONE, HandlingType.LOCKDOWN, checkPercentuale(lockPensione.getText()));;
    	} catch (Exception e) {
    		txtResultContromisure.appendText("Pensione - Lockdown: "+e.getMessage()+"\n");
		}
    	
    	handleContromisure();
    }
    
    @FXML
    void resetContromisure(ActionEvent event) {
    	txtResultContromisure.setText("Reset effettuato!");
    	
    	model.getParVirus().resetContromisure();
    	handleContromisure();
    }
    
    void handleContromisure() {
    	ParametriVirus parVirus = model.getParVirus();
    	
    	noActScuola.setText(parVirus.getMapSpreadReductions(AgeGroup.SCUOLA, HandlingType.NO_ACTS)+"");
    	noActLavoro.setText(parVirus.getMapSpreadReductions(AgeGroup.LAVORO, HandlingType.NO_ACTS)+"");
    	noActPensione.setText(parVirus.getMapSpreadReductions(AgeGroup.PENSIONE, HandlingType.NO_ACTS)+"");
    	
    	reducedScuola.setText(parVirus.getMapSpreadReductions(AgeGroup.SCUOLA, HandlingType.REDUCED)+"");
    	reducedLavoro.setText(parVirus.getMapSpreadReductions(AgeGroup.LAVORO, HandlingType.REDUCED)+"");
    	reducedPensione.setText(parVirus.getMapSpreadReductions(AgeGroup.PENSIONE, HandlingType.REDUCED)+"");
    	
    	noScuolaScuola.setText(parVirus.getMapSpreadReductions(AgeGroup.SCUOLA, HandlingType.NO_SCHOOL)+"");
    	noScuolaLavoro.setText(parVirus.getMapSpreadReductions(AgeGroup.LAVORO, HandlingType.NO_SCHOOL)+"");
    	noScuolaPensione.setText(parVirus.getMapSpreadReductions(AgeGroup.PENSIONE, HandlingType.NO_SCHOOL)+"");
    	
    	lockScuola.setText(parVirus.getMapSpreadReductions(AgeGroup.SCUOLA, HandlingType.LOCKDOWN)+"");
    	lockLavoro.setText(parVirus.getMapSpreadReductions(AgeGroup.LAVORO, HandlingType.LOCKDOWN)+"");
    	lockPensione.setText(parVirus.getMapSpreadReductions(AgeGroup.PENSIONE, HandlingType.LOCKDOWN)+"");
    }
    
    
    
    //MONDO
    
    void handleMondo() {
    	ParametriMondo parMondo = model.getParMondo();
    	txtPercScuola.setText(String.format("%.2f", parMondo.getAgeGroupPercentages(AgeGroup.SCUOLA))+"");
    	txtPercLavoro.setText(String.format("%.2f", parMondo.getAgeGroupPercentages(AgeGroup.LAVORO))+"");
    	txtPercPensione.setText(String.format("%.2f", parMondo.getAgeGroupPercentages(AgeGroup.PENSIONE))+"");
    	
    	txtScuolaAttrComunale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.COMUNE, AgeGroup.SCUOLA))+"");
    	txtScuolaAttrProvinciale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, AgeGroup.SCUOLA))+"");
    	txtScuolaAttrRegionale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.REGIONE, AgeGroup.SCUOLA))+"");
    	
    	txtLavoroAttrComunale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.COMUNE, AgeGroup.LAVORO))+"");
    	txtLavoroAttrProvinciale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, AgeGroup.LAVORO))+"");
    	txtLavoroAttrRegionale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.REGIONE, AgeGroup.LAVORO))+"");
    	
    	txtPensioneAttrComunale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.COMUNE, AgeGroup.PENSIONE))+"");
    	txtPensioneAttrProvinciale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.PROVINCIA, AgeGroup.PENSIONE))+"");
    	txtPensioneAttrRegionale.setText(String.format("%.2f", parMondo.getPercentualeMobilita(AggregationType.REGIONE, AgeGroup.PENSIONE))+"");
    	
    	txtScuolaNoShift.setText(String.format("%.2f", parMondo.getPercentualeImmobilita().get(AgeGroup.SCUOLA))+"");
    	txtLavoroNoShift.setText(String.format("%.2f", parMondo.getPercentualeImmobilita().get(AgeGroup.LAVORO))+"");
    	txtPensioneNoShift.setText(String.format("%.2f", parMondo.getPercentualeImmobilita().get(AgeGroup.PENSIONE))+"");
    }   
    
    @FXML
    void aggiornaMondo(ActionEvent event) {
    	ParametriMondo parMondo = model.getParMondo();
    	txtResultMondo.clear();
    	txtResultMondo.setText("Aggiornato!\n");
    	
    	try {
    		double scuola = checkPercentuale(txtPercScuola.getText());
    		double lavoro = checkPercentuale(txtPercLavoro.getText());
    		double pensione = checkPercentuale(txtPercPensione.getText());
    		
    		double tot = scuola+lavoro+pensione;
    		if (tot < 1.001 && tot > 0.999)
    			pensione = 1- (scuola + lavoro);
    		else
    			throw new Exception("la somma deve fare 1!");
    		Map <AgeGroup, Double> temp = new HashMap<>();
    		temp.put(AgeGroup.SCUOLA, scuola);
    		temp.put(AgeGroup.LAVORO, lavoro);
    		temp.put(AgeGroup.PENSIONE, pensione);
    		parMondo.setAgeGroupPercentages(temp);
    	} catch (Exception e) {
    		txtResultMondo.appendText("Popolazione: "+e.getMessage()+"\n");
		}
    	
    	try {
    		//Scuola
    		double comune = checkPercentuale(txtScuolaAttrComunale.getText());
    		double provincia = checkPercentuale(txtScuolaAttrProvinciale.getText());
    		double regione = checkPercentuale(txtScuolaAttrRegionale.getText());
    		
    		double tot = comune+provincia+regione;
    		if (tot < 1.001 && tot > 1)
    			regione = 1- (comune + provincia);
    		else if (tot <= 1);
    		else
    			throw new Exception("la somma deve essere <= 1!");
    		parMondo.setPercentualeMobilita(AggregationType.COMUNE, AgeGroup.SCUOLA, comune);
    		parMondo.setPercentualeMobilita(AggregationType.PROVINCIA, AgeGroup.SCUOLA, provincia);
    		parMondo.setPercentualeMobilita(AggregationType.REGIONE, AgeGroup.SCUOLA, regione);
    	} catch (Exception e) {
    		txtResultMondo.appendText("Mob. scuola: "+e.getMessage()+"\n");
		}
    	
    	try {
    		//Lavoro
    		double comune = checkPercentuale(txtLavoroAttrComunale.getText());
    		double provincia = checkPercentuale(txtLavoroAttrProvinciale.getText());
    		double regione = checkPercentuale(txtLavoroAttrRegionale.getText());
    		
    		double tot = comune+provincia+regione;
    		if (tot < 1.001 && tot > 1)
    			regione = 1- (comune + provincia);
    		else if (tot <= 1);
    		else
    			throw new Exception("la somma deve essere <= 1!");
    		parMondo.setPercentualeMobilita(AggregationType.COMUNE, AgeGroup.LAVORO, comune);
    		parMondo.setPercentualeMobilita(AggregationType.PROVINCIA, AgeGroup.LAVORO, provincia);
    		parMondo.setPercentualeMobilita(AggregationType.REGIONE, AgeGroup.LAVORO, regione);
    	} catch (Exception e) {
    		txtResultMondo.appendText("Mob. lavoro: "+e.getMessage()+"\n");
		}
    	
    	try {
    		//Pensione
    		double comune = checkPercentuale(txtPensioneAttrComunale.getText());
    		double provincia = checkPercentuale(txtPensioneAttrProvinciale.getText());
    		double regione = checkPercentuale(txtPensioneAttrRegionale.getText());
    		
    		double tot = comune+provincia+regione;
    		if (tot < 1.001 && tot > 1)
    			regione = 1- (comune + provincia);
    		else if (tot <= 1);
    		else
    			throw new Exception("la somma deve essere <= 1!");
    		parMondo.setPercentualeMobilita(AggregationType.COMUNE, AgeGroup.PENSIONE, comune);
    		parMondo.setPercentualeMobilita(AggregationType.PROVINCIA, AgeGroup.PENSIONE, provincia);
    		parMondo.setPercentualeMobilita(AggregationType.REGIONE, AgeGroup.PENSIONE, regione);
    	} catch (Exception e) {
    		txtResultMondo.appendText("Mob. pensione: "+e.getMessage()+"\n");
		}
    	
    	handleMondo();
    }
    
    @FXML
    void resetMondo(ActionEvent event) {
    	txtResultMondo.setText("Reset effettuato!");
    	
    	model.setParMondo(new ParametriMondo());
    	handleMondo();
    }
    
    
    
    //SIMULAZIONE
    
    @FXML
    void randomizer() {
    	if (randomNuovi.isSelected()) {
    		boxNuovi.setDisable(true);
    		boxNuovi.setValue("Random!");
    	}
    	else {
    		boxNuovi.setDisable(false);
    		boxNuovi.setValue(null);
    	}
    }
    
    @FXML
    void terminazione() {
    	ReturnCondition rt = boxTerminazione.getValue();
    	ParametriSimulazione parSimulazione = model.getParSimulazione();
    	if (rt == ReturnCondition.ESTINZIONE_VIRUS || rt == ReturnCondition.FINE_EPIDEMIA) {
    		txtTerminazione.setDisable(true);
    		txtTerminazione.clear();
    	}
    	else if (rt == ReturnCondition.NUM_GIORNI) {
    		txtTerminazione.setDisable(false);
    		txtTerminazione.setText(parSimulazione.getNumGiorniHop()+"");
    	}
    	else if (rt == ReturnCondition.NUM_MALATI || rt == ReturnCondition.NUM_MORTI) {
    		txtTerminazione.setDisable(false);
    		txtTerminazione.setText(parSimulazione.getNumStatoHop()+"");
    	}
    	else {
    		txtTerminazione.setDisable(false);
    		txtTerminazione.setText(parSimulazione.getPerStatoHop()+"");
    	}
    }
    
    @FXML
    void tipoSimulazione() {
    	//NON USATO
    }
    
    void handleTipoSimulazione() {
    	ParametriSimulazione parSimulazione = model.getParSimulazione();
    	SimulationType st = parSimulazione.getTipoSimulazione();
    	if (st == SimulationType.LIVE_INTERACTION) {
    		tabLive.setDisable(false);
    		tabParametrizzata.setDisable(true);
    	}
    	else {
    		tabLive.setDisable(true);
    		tabParametrizzata.setDisable(false);
    	}
    }
    
    @FXML
    void addNewCont(ActionEvent event) {
    	txtResultSimulazione.setText("");
    	try {
    		if (txtNuoviScuola.getText().equals(""))
    			txtNuoviScuola.setText((int)(11*Math.random()+1)+"");
    		if (txtNuoviLavoro.getText().equals(""))
    			txtNuoviLavoro.setText((int)(11*Math.random()+1)+"");
    		if (txtNuoviPensione.getText().equals(""))
    			txtNuoviPensione.setText((int)(11*Math.random()+1)+"");
    		
    		
    		Map <AgeGroup, Integer> temp = new HashMap<>();
    		temp.put(AgeGroup.SCUOLA, checkNumeroIntero(txtNuoviScuola.getText()));
    		temp.put(AgeGroup.LAVORO, checkNumeroIntero(txtNuoviLavoro.getText()));
    		temp.put(AgeGroup.PENSIONE, checkNumeroIntero(txtNuoviPensione.getText()));
    		
    		if (boxNuovi.getValue() == null)
    			throw new Exception("Errore: nessun ente inserito!");
    		
    		model.setNuoviContagi(boxNuovi.getValue(), temp);
    	} catch (Exception e) {
    		txtResultSimulazione.appendText("% Nuovi: "+e.getMessage()+"\n");
		}
    	
    	txtNuoviScuola.setText("");
    	txtNuoviLavoro.setText("");
    	txtNuoviPensione.setText("");
    	randomNuovi.setSelected(true);
    	randomizer();
    	
    	txtResultSimulazione.appendText("Focolai giorno 0:\n");
    	for (String s : model.getNuoviContagi()) {
    		txtResultSimulazione.appendText(s+"\n");
    	}
    }
    
    @FXML
    void aggiornaSimulazione(ActionEvent event) {
    	ParametriSimulazione parSimulazione = model.getParSimulazione();
    	txtResultSimulazione.clear();
    	txtResultSimulazione.setText("Aggiornato!\n");
    	
    	SimulationType st = boxTipoSimulazione.getValue();
    	parSimulazione.setTipoSimulazione(st);
    	st = parSimulazione.getTipoSimulazione();
    	handleTipoSimulazione();
    	
    	ReturnCondition rt = boxTerminazione.getValue();
    	parSimulazione.setHopCondition(rt);
    	if (rt == ReturnCondition.ESTINZIONE_VIRUS || rt == ReturnCondition.FINE_EPIDEMIA);
    	else if (rt == ReturnCondition.NUM_GIORNI) {
    		try {
    			parSimulazione.setNumGiorniHop(checkNumeroIntero(txtTerminazione.getText()));
        	} catch (Exception e) {
        		txtResultSimulazione.appendText("# Giorni: "+e.getMessage()+"\n");
    		}
    	}
    	else if (rt == ReturnCondition.NUM_MALATI || rt == ReturnCondition.NUM_MORTI) {
    		try {
    			parSimulazione.setNumStatoHop(checkNumeroIntero(txtTerminazione.getText()));
        	} catch (Exception e) {
        		txtResultSimulazione.appendText("# Persone: "+e.getMessage()+"\n");
    		}
    	}
    	else {
    		try {
    			parSimulazione.setPerStatoHop(checkNumeroVirgola(txtTerminazione.getText()));
        	} catch (Exception e) {
        		txtResultSimulazione.appendText("% Persone: "+e.getMessage()+"\n");
    		}
    	}
    	terminazione();
    	
    	ChanceType ct = boxProbabilita.getValue();
    	parSimulazione.setTipoProbabilita(ct);
    	boxProbabilita.setValue(ct);
    	
    	if (ct == ChanceType.STOCASTICA) {
    		txtResultSimulazione.appendText("Attenzione, questo tipo di simulazione è estremamente pesante\n"
    				+ "Se usata per simulare un'intera pandemia potrebbe richiedere molto tempo!\n");
    	}
    	
    	txtResultSimulazione.appendText("Focolai giorno 0:\n");
    	for (String s : model.getNuoviContagi()) {
    		txtResultSimulazione.appendText(s+"\n");
    	}
    }
    
    @FXML
    void resetSimulazione(ActionEvent event) {
    	txtResultSimulazione.setText("Reset effettuato!");
    	AggregationType agg = model.getParSimulazione().getAggSimulazione();
    	ParametriSimulazione parSimulazione = new ParametriSimulazione();
    	parSimulazione.setAggSimulazione(agg);
    	model.setParSimulazione(parSimulazione);
    	
    	boxTipoSimulazione.setValue(parSimulazione.getTipoSimulazione());
    	tipoSimulazione();
    	boxTerminazione.setValue(parSimulazione.getHopCondition());
    	terminazione();
    	boxProbabilita.setValue(parSimulazione.getTipoProbabilita());
    	
    	randomNuovi.setSelected(true);
    	randomizer();
		
		model.resetNuoviContagi();
    }
    

    
    //PARAMETRIZZATA
    
    void handleParametrizzata() {
    	Status[] stati = {Status.MALATO, Status.MORTO};
    	
    	boxStatusChiusura.getItems().clear();
    	boxStatusChiusura.getItems().addAll(stati);
    	boxStatusChiusura.setValue(Status.MORTO);
    	
    	boxStatusHandling.getItems().clear();
    	boxStatusHandling.getItems().addAll(stati);
    	boxStatusHandling.setValue(Status.MORTO);
    	
    	boxHandling.getItems().clear();
    	boxHandling.getItems().addAll(HandlingType.values());
    	boxHandling.getItems().remove(HandlingType.NO_ACTS);
    	boxHandling.setValue(HandlingType.REDUCED);
    }
    
    @FXML
    void addChiusura(ActionEvent event) {
    	txtResultParametrizzata.clear();
    	ParametriSimulazione parSim = model.getParSimulazione();
    	double value;
    	String raw = txtValueChiusura.getText();
    	
    	try {
    		if (isPercentualeChiusura.isSelected())
    			value = checkPercentuale(raw);
    		else
    			value = checkNumeroIntero(raw);
    		
    		Utility u = new Utility(parSim.getAggSimulazione(), boxStatusChiusura.getValue(),
    				isPercentualeChiusura.isSelected(), value);
    		parSim.addChiusura(u);
    	}
    	catch (Exception e) {
    		txtResultParametrizzata.setText("Aggiunta chiusura: "+e.getMessage());
    	}
    	handleChiusureHandling();
    }
    
    @FXML
    void removeChiusura(ActionEvent event) {
    	txtResultParametrizzata.clear();
    	ParametriSimulazione parSim = model.getParSimulazione();
    	parSim.removeChiusura(parSim.getAggSimulazione());
    	handleChiusureHandling();
    }
    
    @FXML
    void addHandling(ActionEvent event) {
    	txtResultParametrizzata.clear();
    	ParametriSimulazione parSim = model.getParSimulazione();
    	double value;
    	String raw = txtValueHandling.getText();
    	
    	try {
    		if (isPercentualeHandling.isSelected())
    			value = checkPercentuale(raw);
    		else
    			value = checkNumeroIntero(raw);
    		
    		Utility u = new Utility(parSim.getAggSimulazione(), boxStatusHandling.getValue(),
    				isPercentualeHandling.isSelected(), value);
    		parSim.addHandingType(boxHandling.getValue(), u);
    	}
    	catch (Exception e) {
    		txtResultParametrizzata.setText("Aggiunta handling: "+e.getMessage());
    	}
    	handleChiusureHandling();
    }
    
    @FXML
    void removeHandling(ActionEvent event) {
    	txtResultParametrizzata.clear();
    	ParametriSimulazione parSim = model.getParSimulazione();
    	parSim.removeHandling(parSim.getAggSimulazione(), boxHandling.getValue());
    	handleChiusureHandling();
    }
    
    void handleChiusureHandling() {
    	//STAMPA HANDLING E CHIUSURE INSERITE
    	ParametriSimulazione parSim = model.getParSimulazione();
    	txtResultParametrizzata.appendText("- Chiusure:\n");
    	for (String s : parSim.getChiusure()) {
    		txtResultParametrizzata.appendText(s);
    	}
    	txtResultParametrizzata.appendText("- Contromisure:\n");
    	for (String s : parSim.getHandList()) {
    		txtResultParametrizzata.appendText(s);
    	}
    }
    
    @FXML
    void resetParametrizzata(ActionEvent event) {
    	utilNewSimulazione(event);
    	
    	handleParametrizzata();
    	handleChiusureHandling();
    	txtResultParametrizzata.setText("Reset effettuato!");
    }
    
    @FXML
    void startSimulazione(ActionEvent event) {
    	if (!model.isSimulazioneAttiva()) {
    		model.startSimulazione();
    	}
    	model.flushNuovi();
    	while (model.verificaContinuazione()) {
    		model.run();
    	}
    	txtResultParametrizzata.setText("Giorno fine: "+model.getGiornoCorrente()+"\n");
    	for (String s : model.getParSimulazione().getReport())
    		txtResultParametrizzata.appendText(s+"\n");

    	prepareRisultato(event);
    	
    	utilSimulazioneIniziata(event);
    }
    
    
    
    //LIVE
    
    void handleLive() {
    	AggregationType agg = model.getParSimulazione().getAggSimulazione();
    	
    	boxHandlingLive.getItems().clear();
    	boxHandlingLive.getItems().addAll(HandlingType.values());
    	boxHandlingLive.getItems().remove(HandlingType.NO_ACTS);
    	boxHandlingLive.setValue(HandlingType.REDUCED);
    	
    	LinkedList<AggregationType> prova = new LinkedList<>();
    	
    	prova.add(AggregationType.REGIONE);
    	if (agg != AggregationType.REGIONE)
    		prova.add(AggregationType.PROVINCIA);
    	if (agg == AggregationType.COMUNE)
    		prova.add(AggregationType.COMUNE);
    	
    	boxTipoEnteHandlingLive.getItems().clear();
    	boxTipoEnteHandlingLive.getItems().addAll(prova);
    	
    	boxTipoEnteChiusuraLive.getItems().clear();
    	boxTipoEnteChiusuraLive.getItems().addAll(prova);
    	
    	boxTipoEnteChiusuraLive.setValue(agg);
    	boxTipoEnteHandlingLive.setValue(agg);
    	
    	tipoEnteChiusuraLive();
    	tipoEnteHandlingLive();
    	
    }
    
    @FXML
    void tipoEnteChiusuraLive() {
    	boxEnteChiusuraLive.getItems().clear();
    	if (boxTipoEnteChiusuraLive.getValue() != null)
    		boxEnteChiusuraLive.getItems().addAll(model.getEnti(boxTipoEnteChiusuraLive.getValue()));
    }

    @FXML
    void tipoEnteHandlingLive() {
    	boxEnteHandlingLive.getItems().clear();
    	if (boxTipoEnteHandlingLive.getValue() != null)
    		boxEnteHandlingLive.getItems().addAll(model.getEnti(boxTipoEnteHandlingLive.getValue()));
    }

    @FXML
    void addChiusuraLive(ActionEvent event) {
    	try {
			model.addChiudiLive(boxTipoEnteChiusuraLive.getValue(), boxEnteChiusuraLive.getValue());
			txtResultLive.appendText("Chiuso "+boxEnteChiusuraLive.getValue()+"\n");
		} catch (Exception e) {
			txtResultLive.appendText("Add chiusura "+e.getMessage()+"\n");
		}
    }

    @FXML
    void addHandlingLive(ActionEvent event) {
    	try {
			model.addHandlingLive(boxTipoEnteHandlingLive.getValue(), boxEnteHandlingLive.getValue(), boxHandlingLive.getValue());
			txtResultLive.appendText(boxEnteHandlingLive.getValue()+" settato su "+boxHandlingLive.getValue()+"\n");
		} catch (Exception e) {
			txtResultLive.appendText("Add contromisura "+e.getMessage()+"\n");
		}
    }

    @FXML
    void removeChiusuraLive(ActionEvent event) {
    	try {
			model.removeChiudiLive(boxTipoEnteChiusuraLive.getValue(), boxEnteChiusuraLive.getValue());
			txtResultLive.appendText("Aperto "+boxEnteChiusuraLive.getValue()+"\n");
		} catch (Exception e) {
			txtResultLive.appendText("Remove chiusura "+e.getMessage()+"\n");
		}
    }

    @FXML
    void removeHandlingLive(ActionEvent event) {
    	try {
			model.removeHandlingLive(boxTipoEnteHandlingLive.getValue(), boxEnteHandlingLive.getValue());
			txtResultLive.appendText("Rimosse restrizioni per "+boxEnteHandlingLive.getValue()+"\n");
		} catch (Exception e) {
			txtResultLive.appendText("Remove contromisura "+e.getMessage()+"\n");
		}
    }

    @FXML
    void resetParametriLive(ActionEvent event) {
    	utilNewSimulazione(event);
    	model.inizializzaMappeStatistiche();
    	//TERMINA
    	handleLive();
    	//RIMUOVERE TUTTI I 
    	handleTipoSimulazione();
    }

    @FXML
    void startLive(ActionEvent event) {
    	if (!model.isSimulazioneAttiva()) {
    		model.startSimulazione();
    	}
    	model.flushNuovi();
    	while (model.verificaContinuazione()) {
    		model.run();
    	}
    	txtResultLive.setText("Giorno fine: "+model.getGiornoCorrente()+"\n");
    	for (String s : model.getParSimulazione().getReport())
    		txtResultLive.appendText(s+"\n");
    	
    	prepareRisultato(event);
    	
    	utilSimulazioneIniziata(event);
    }
    
    
    
    // RUSULTATO
    
    void prepareRisultato(ActionEvent event) {
    	tabRisultati.setDisable(false);
    	nazionale.setSelected(true);
    	isNazionale(event);
    	handleBoxRisultato(event);
    }
    
    void handleBoxRisultato(ActionEvent event) {
    	AggregationType agg = model.getParSimulazione().getAggSimulazione();
    	LinkedList<AggregationType> prova = new LinkedList<>();

    	prova.add(AggregationType.REGIONE);
    	if (agg != AggregationType.REGIONE)
    		prova.add(AggregationType.PROVINCIA);
    	if (agg == AggregationType.COMUNE)
    		prova.add(AggregationType.COMUNE);
    	
    	boxScegliAggregazioneRisultato.getItems().clear();
    	boxScegliAggregazioneRisultato.getItems().addAll(prova);
    	boxScegliAggregazioneRisultato.setValue(agg);
    }
    
    @FXML
    void scegliAggregazioneRisultato(ActionEvent event) {
    	boxScegliEnteRisultato.getItems().clear();
    	AggregationType agg = boxScegliAggregazioneRisultato.getValue();
    	if (agg != null) {
    		boxScegliEnteRisultato.getItems().addAll(model.getEnti(agg));
    		scegliEnteRisultato(event);
    	}
    }

    @FXML
    void scegliEnteRisultato(ActionEvent event) {
    	StatisticheEnte stats;
    	AggregationType agg = boxScegliAggregazioneRisultato.getValue();
    	if (agg != null) {
    		try {
    			stats = model.getStatistiche(boxScegliAggregazioneRisultato.getValue(), boxScegliEnteRisultato.getValue());
    		} catch (Exception e) {
    			stats = model.getStatsNazione();
    			nazionale.setSelected(true);
    		}
    	}
    	else
    		stats = null;
    	
    	if (stats == null) {
    		stats = model.getStatsNazione();
    	}
    	
    	txtIsTerminata.setText(""+!model.isSimulazioneAttiva());
    	txtGornoRisultato.setText(model.getGiornoCorrente()+"");
    	
    	Integer[][] info = stats.getInformazioni();
    	result00.setText(info[0][0]+"");
    	result01.setText(info[0][1]+"");
    	result02.setText(info[0][2]+"");
    	result03.setText(info[0][3]+"");
    	result04.setText(info[0][4]+"");
    	result05.setText(info[0][5]+"");
    	
    	result10.setText(info[1][0]+"");
    	result11.setText(info[1][1]+"");
    	result12.setText(info[1][2]+"");
    	result13.setText(info[1][3]+"");
    	result14.setText(info[1][4]+"");
    	result15.setText(info[1][5]+"");
    	
    	result20.setText(info[2][0]+"");
    	result21.setText(info[2][1]+"");
    	result22.setText(info[2][2]+"");
    	result23.setText(info[2][3]+"");
    	result24.setText(info[2][4]+"");
    	result25.setText(info[2][5]+"");
    	
    	
    	Series <String, Number> malati = new Series<String, Number>(stats.getNuoviContainer().get(Status.MALATO).getData());
    	Series <String, Number> morti = new Series<String, Number>(stats.getNuoviContainer().get(Status.MORTO).getData());
    	
    	malatiGrafico.getData().clear();
    	malatiGrafico.getData().add(malati);
    	
    	mortiGrafico.getData().clear();
    	mortiGrafico.getData().add(morti);
    	
    	if (stats.getGiornoArrivoContagio() == null)
    		txtResultRisultati.setText("Il contagio non è ancora arrivato qui!\n\n");
    	else if (stats.getGiornoArrivoContagio() == 0)
    		txtResultRisultati.setText("Il contagio è iniziato da qui!\n\n");
    	else
    		txtResultRisultati.setText("Il contagio è arrivato qui durante il "+stats.getGiornoArrivoContagio()+"° giorno\n\n");
    	txtResultRisultati.appendText(Status.CONTAGIOSO+":\n	Raggiunto il picco giornaliero massimo di "+stats.getMaxValues().get(
    			Status.CONTAGIOSO)+" persone durante il "+stats.getMaxDates().get(Status.CONTAGIOSO)+"° giorno\n\n");
    	txtResultRisultati.appendText(Status.MALATO+":\n	Raggiunto il picco giornaliero massimo di "+stats.getMaxValues().get(
    			Status.MALATO)+" persone durante il "+stats.getMaxDates().get(Status.MALATO)+"° giorno\n\n");
    	txtResultRisultati.appendText(Status.MORTO+":\n	Raggiunto il picco giornaliero massimo di "+stats.getMaxValues().get(
    			Status.MORTO)+" persone durante il "+stats.getMaxDates().get(Status.MORTO)+"° giorno\n\n");
    	
    	grafico1.getData().clear();
    	grafico1.setTitle("Contagiosi, malati e morti a confronto:");
    	grafico1.getData().add(stats.getNuoviContainer().get(Status.CONTAGIOSO));
    	grafico1.getData().add(stats.getNuoviContainer().get(Status.MALATO));
    	grafico1.getData().add(stats.getNuoviContainer().get(Status.MORTO));
    	
    	grafico4.getData().clear();
    	grafico4.setTitle("Valori finali:");
    	Status[] stati = {Status.GUARITO, Status.CURATO, Status.MORTO};
    	
    	for (AgeGroup ag : AgeGroup.values()) {
    		
    		XYChart.Series <String, Number> series= new XYChart.Series <String, Number> ();
            series.setName(ag.toString()); 
    		
    		for (Status st : stati) {
    			series.getData().add(new Data <String, Number> (st.toString(), stats.getNumStato(st).get(ag)));
    		}
    		
    		grafico4.getData().add(series);
    	}
    	grafico4.setLegendVisible(true);
    }
    
    @FXML
    void isNazionale(ActionEvent event) {
    	boolean isNazionale = nazionale.isSelected();
    	if (isNazionale) {
    		boxScegliAggregazioneRisultato.setValue(null);
    		boxScegliEnteRisultato.setValue(null);
    		boxScegliAggregazioneRisultato.setDisable(true);
    		boxScegliEnteRisultato.setDisable(true);
    	}
    	else {
    		boxScegliAggregazioneRisultato.setDisable(false);
    		boxScegliEnteRisultato.setDisable(false);

        	boxScegliAggregazioneRisultato.setValue(model.getParSimulazione().getAggSimulazione());
    		scegliAggregazioneRisultato(event);
    	}
    }
    
    
    //BASE
    
    void utilSimulazioneIniziata(ActionEvent event) {
      	tabHome.setDisable(true);
    	tabVirus.setDisable(true);
    	tabContromisure.setDisable(true);
    	tabMondo.setDisable(true);
    	
    	tabGrafici.setDisable(false);
    	tabGrafici2.setDisable(false);
    	
    	boxTipoSimulazione.setDisable(true);
    	boxProbabilita.setDisable(true);
    	btnResetSimulazione.setDisable(true);
    	
    	if (!model.isSimulazioneAttiva()) {
    		btnStartLive.setDisable(true);
    		btnStartSimulazione.setDisable(true);
    	}
    }
    
    void utilNewSimulazione(ActionEvent event) {
    	model.terminaSimulazione();
    	resetSimulazione(event);
    	
    	tabHome.setDisable(false);
    	tabVirus.setDisable(false);
    	tabContromisure.setDisable(false);
    	tabMondo.setDisable(false);
    	tabSimulazione.setDisable(false);
    	handleTipoSimulazione();
    	tabRisultati.setDisable(true);
    	tabGrafici.setDisable(true);
    	tabGrafici2.setDisable(true);
    	
    	boxTipoSimulazione.setDisable(false);
    	boxProbabilita.setDisable(false);
    	btnResetSimulazione.setDisable(false);
    	
    	btnStartLive.setDisable(false);
		btnStartSimulazione.setDisable(false);
    }
    
    Model model;
    public void setModel(Model model) {
    	this.model = model;
    	ParametriSimulazione parSimulazione = model.getParSimulazione();
    	// HOME
    	boxAggregazione.getItems().addAll(AggregationType.values());
    	AggregationType agg = parSimulazione.getAggSimulazione();
    	boxAggregazione.setValue(agg);
    	handleSetAggregazione(agg);
    	//VIRUS
    	handleVirus();
    	//CONTROMISURE
    	handleContromisure();
    	//MONDO
    	handleMondo();
    	//SIMULAZIONE
    	boxTipoSimulazione.getItems().addAll(SimulationType.values());
    	boxTipoSimulazione.setValue(parSimulazione.getTipoSimulazione());
    	handleTipoSimulazione();
    	boxTerminazione.getItems().addAll(ReturnCondition.values());
    	boxTerminazione.setValue(parSimulazione.getHopCondition());
    	terminazione();
    	boxProbabilita.getItems().addAll(ChanceType.values());
    	boxProbabilita.setValue(parSimulazione.getTipoProbabilita());
    	randomNuovi.setSelected(true);
    	randomizer();
    	//PARAMETRIZZATA
    	handleParametrizzata();
    	//LIVE
    	
    	tabContromisure.setDisable(true);
    	tabLive.setDisable(true);
    	tabMondo.setDisable(true);
    	tabParametrizzata.setDisable(true);
    	tabRisultati.setDisable(true);
    	tabVirus.setDisable(true);
    	tabSimulazione.setDisable(true);
    	tabGrafici.setDisable(true);
    	tabGrafici2.setDisable(true);
    }
    
    public int checkNumeroIntero(String raw) throws Exception {
    	int i;
    	try {
    		i = Integer.parseInt(raw);
    	}catch (Exception e) {
			throw new Exception("\""+raw+"\" non è in numero intero!");
		}
    	return i;
    }
    
    public double checkNumeroVirgola(String raw) throws Exception {
    	double i;
    	try {
    		i = Double.parseDouble(raw);
    	}catch (Exception e) {
			throw new Exception("\""+raw+"\" non è in numero!");
		}
    	return i;
    }
    
    public double checkPercentuale(String raw) throws Exception {
    	double i;
    	try {
    		i = Double.parseDouble(raw);
    	}catch (Exception e) {
			throw new Exception("\""+raw+"\" non è in numero!");
		}
    	if (i > 1 || i < 0)
    		throw new Exception(raw+" non è compreso tra 0 e 1!");
    	return i;
    }
    
    @FXML
    void initialize() {
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabHome != null : "fx:id=\"tabHome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAggregazione != null : "fx:id=\"boxAggregazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tPrec != null : "fx:id=\"tPrec\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tEsec != null : "fx:id=\"tEsec\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tPrep != null : "fx:id=\"tPrep\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabVirus != null : "fx:id=\"tabVirus\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtTContagio != null : "fx:id=\"txtTContagio\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtTMalattia != null : "fx:id=\"txtTMalattia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtB0 != null : "fx:id=\"txtB0\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtToMalatoScuola != null : "fx:id=\"txtToMalatoScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtToMortoPensione != null : "fx:id=\"txtToMortoPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtToMortoLavoro != null : "fx:id=\"txtToMortoLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtToMortoScuola != null : "fx:id=\"txtToMortoScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtToMalatoPensione != null : "fx:id=\"txtToMalatoPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtToMalatoLavoro != null : "fx:id=\"txtToMalatoLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultVirus != null : "fx:id=\"txtResultVirus\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAggiornaVirus != null : "fx:id=\"btnAggiornaVirus\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetVirus != null : "fx:id=\"btnResetVirus\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabContromisure != null : "fx:id=\"tabContromisure\" was not injected: check your FXML file 'Scene.fxml'.";
        assert noScuolaScuola != null : "fx:id=\"noScuolaScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lockLavoro != null : "fx:id=\"lockLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert reducedLavoro != null : "fx:id=\"reducedLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert reducedScuola != null : "fx:id=\"reducedScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert noActPensione != null : "fx:id=\"noActPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lockScuola != null : "fx:id=\"lockScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lockPensione != null : "fx:id=\"lockPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert noActLavoro != null : "fx:id=\"noActLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert noActScuola != null : "fx:id=\"noActScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert reducedPensione != null : "fx:id=\"reducedPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert noScuolaPensione != null : "fx:id=\"noScuolaPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert noScuolaLavoro != null : "fx:id=\"noScuolaLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultContromisure != null : "fx:id=\"txtResultContromisure\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAggiornaContromisure != null : "fx:id=\"btnAggiornaContromisure\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetContromisure != null : "fx:id=\"btnResetContromisure\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabMondo != null : "fx:id=\"tabMondo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPercScuola != null : "fx:id=\"txtPercScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPercPensione != null : "fx:id=\"txtPercPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPercLavoro != null : "fx:id=\"txtPercLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtScuolaAttrComunale != null : "fx:id=\"txtScuolaAttrComunale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPensioneAttrProvinciale != null : "fx:id=\"txtPensioneAttrProvinciale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtLavoroAttrProvinciale != null : "fx:id=\"txtLavoroAttrProvinciale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtScuolaAttrProvinciale != null : "fx:id=\"txtScuolaAttrProvinciale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPensioneAttrComunale != null : "fx:id=\"txtPensioneAttrComunale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtLavoroAttrComunale != null : "fx:id=\"txtLavoroAttrComunale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPensioneNoShift != null : "fx:id=\"txtPensioneNoShift\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtLavoroNoShift != null : "fx:id=\"txtLavoroNoShift\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtScuolaNoShift != null : "fx:id=\"txtScuolaNoShift\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPensioneAttrRegionale != null : "fx:id=\"txtPensioneAttrRegionale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtLavoroAttrRegionale != null : "fx:id=\"txtLavoroAttrRegionale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtScuolaAttrRegionale != null : "fx:id=\"txtScuolaAttrRegionale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultMondo != null : "fx:id=\"txtResultMondo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAggiornaMondo != null : "fx:id=\"btnAggiornaMondo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetMondo != null : "fx:id=\"btnResetMondo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabSimulazione != null : "fx:id=\"tabSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxTipoSimulazione != null : "fx:id=\"boxTipoSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetSimulazione != null : "fx:id=\"btnResetSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAggiornaSimulazione != null : "fx:id=\"btnAggiornaSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxTerminazione != null : "fx:id=\"boxTerminazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtTerminazione != null : "fx:id=\"txtTerminazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxProbabilita != null : "fx:id=\"boxProbabilita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultSimulazione != null : "fx:id=\"txtResultSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNuoviScuola != null : "fx:id=\"txtNuoviScuola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNuoviPensione != null : "fx:id=\"txtNuoviPensione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNuoviLavoro != null : "fx:id=\"txtNuoviLavoro\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxNuovi != null : "fx:id=\"boxNuovi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert randomNuovi != null : "fx:id=\"randomNuovi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabParametrizzata != null : "fx:id=\"tabParametrizzata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetParametrizzata != null : "fx:id=\"btnResetParametrizzata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnStartSimulazione != null : "fx:id=\"btnStartSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultParametrizzata != null : "fx:id=\"txtResultParametrizzata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtValueChiusura != null : "fx:id=\"txtValueChiusura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxStatusChiusura != null : "fx:id=\"boxStatusChiusura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAddChiusura != null : "fx:id=\"btnAddChiusura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRemoveChiusura != null : "fx:id=\"btnRemoveChiusura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxHandling != null : "fx:id=\"boxHandling\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtValueHandling != null : "fx:id=\"txtValueHandling\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxStatusHandling != null : "fx:id=\"boxStatusHandling\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAddHandling != null : "fx:id=\"btnAddHandling\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRemoveHandling != null : "fx:id=\"btnRemoveHandling\" was not injected: check your FXML file 'Scene.fxml'.";
        assert isPercentualeChiusura != null : "fx:id=\"isPercentualeChiusura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert isPercentualeHandling != null : "fx:id=\"isPercentualeHandling\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabLive != null : "fx:id=\"tabLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetParametriLive != null : "fx:id=\"btnResetParametriLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnStartLive != null : "fx:id=\"btnStartLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultLive != null : "fx:id=\"txtResultLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxTipoEnteChiusuraLive != null : "fx:id=\"boxTipoEnteChiusuraLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxEnteChiusuraLive != null : "fx:id=\"boxEnteChiusuraLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAddChiusuraLive != null : "fx:id=\"btnAddChiusuraLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRemoveChiusuraLive != null : "fx:id=\"btnRemoveChiusuraLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxHandlingLive != null : "fx:id=\"boxHandlingLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxTipoEnteHandlingLive != null : "fx:id=\"boxTipoEnteHandlingLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxEnteHandlingLive != null : "fx:id=\"boxEnteHandlingLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAddHandlingLive != null : "fx:id=\"btnAddHandlingLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRemoveHandlingLive != null : "fx:id=\"btnRemoveHandlingLive\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabRisultati != null : "fx:id=\"tabRisultati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGornoRisultato != null : "fx:id=\"txtGornoRisultato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtIsTerminata != null : "fx:id=\"txtIsTerminata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxScegliAggregazioneRisultato != null : "fx:id=\"boxScegliAggregazioneRisultato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxScegliEnteRisultato != null : "fx:id=\"boxScegliEnteRisultato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result03 != null : "fx:id=\"result03\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result02 != null : "fx:id=\"result02\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result01 != null : "fx:id=\"result01\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result00 != null : "fx:id=\"result00\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result04 != null : "fx:id=\"result04\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result24 != null : "fx:id=\"result24\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result15 != null : "fx:id=\"result15\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result05 != null : "fx:id=\"result05\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result14 != null : "fx:id=\"result14\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result23 != null : "fx:id=\"result23\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result13 != null : "fx:id=\"result13\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result22 != null : "fx:id=\"result22\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result12 != null : "fx:id=\"result12\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result21 != null : "fx:id=\"result21\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result11 != null : "fx:id=\"result11\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result20 != null : "fx:id=\"result20\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result10 != null : "fx:id=\"result10\" was not injected: check your FXML file 'Scene.fxml'.";
        assert result25 != null : "fx:id=\"result25\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultRisultati != null : "fx:id=\"txtResultRisultati\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}

