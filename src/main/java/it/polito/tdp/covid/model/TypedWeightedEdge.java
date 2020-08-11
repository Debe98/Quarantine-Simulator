package it.polito.tdp.covid.model;
import org.jgrapht.graph.DefaultWeightedEdge;

public class TypedWeightedEdge extends DefaultWeightedEdge {

	/**
	 * livello di aggregazione dell'arco
	 * @author lucad
	 *
	 */
	public enum AggregationType {
		COMUNE, PROVINCIA, REGIONE
	}
	
	/**
	 * Cambio di ente all'attraversamento dell'arco
	 * @author lucad
	 *
	 */
	public enum CrossType {
		COMUNALE, PROVINCIALE, REGIONALE;
	}
	
	/**
	 * Tipo di arco
	 * @author lucad
	 *
	 */
	public enum RelationType {
		ADIACENZA, SUBORDINAZIONE, COLLEGAMENTO;
	}
	
	
	private AggregationType livello;
	private CrossType attraversamento;
	private RelationType relazione;

	/**
	 * Costruttore dell'arco personalizzato
	 * @param livello -> Tipo di aggregazione che modella l'arco
	 * @param attraversamento -> Cambio di ente al passaggio
	 * @param relazione -> Relazione tra gli estremi dell'arco
	 */
    public TypedWeightedEdge(AggregationType livello, CrossType attraversamento, RelationType relazione) {
		super();
		this.livello = livello;
		this.attraversamento = attraversamento;
		this.relazione = relazione;
	}

    public AggregationType getLivello() {
		return livello;
	}

	public void setLivello(AggregationType livello) {
		this.livello = livello;
	}

	public CrossType getAttraversamento() {
		return attraversamento;
	}

	public void setAttraversamento(CrossType attraversamento) {
		this.attraversamento = attraversamento;
	}

	public RelationType getRelazione() {
		return relazione;
	}

	public void setRelazione(RelationType relazione) {
		this.relazione = relazione;
	}

	@Override
	public String toString() {
		return "|| " + getSource() + " - " + getTarget() + "\n"+
				"   lv: " + livello + ", cross: " + attraversamento+", rel: " + relazione;
	}
}
