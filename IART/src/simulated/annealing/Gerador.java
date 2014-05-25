package simulated.annealing;

import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import logic.Ambulancia;
import logic.Edificio;
import logic.Estrada;

public class Gerador {
	
	private Ambulancia ambulancia = null;
	private int nrPacientesRestantes = 0;
	private Rota rota = null;
	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;

	public Gerador(Ambulancia ambulancia, int nrPacientes, ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade){
		rota = new Rota();
		this.ambulancia = ambulancia;
		this.nrPacientesRestantes = nrPacientes;
		this.cidade = cidade;
	}

	public Rota geraRota() {
		
		return rota;
	}
}
