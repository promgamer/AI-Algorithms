package simulated.annealing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import logic.Ambulancia;
import logic.Clinica;
import logic.Edificio;
import logic.Estrada;
import logic.Sucursal;

public class Gerador {
	
	private Ambulancia ambulanciaInicial = null;
	private Ambulancia ambulancia = null;
	private int nrPacientesInciais = 0;
	private int nrPacientesRestantes = 0;
	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;
	
	public Gerador(Ambulancia ambulancia, int nrPacientes, ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade){
		this.ambulanciaInicial  = ambulancia.clone();
		this.ambulancia = ambulancia.clone();
		this.nrPacientesInciais = this.nrPacientesRestantes = nrPacientes;
		this.cidade = cidade;
	}

	public Rota geraRota() {
		Rota rota = new Rota();
		// boleanos para controlar o combustivel e pacientes "deixados para tras"
		boolean prioridadeSucursal = false, prioridadeBomba = false;
		
		Edificio atual = null;
		//Escolher edificio inicial -> tem de ser uma sucursal(apenas sucursais têm garagem e a ambulancia)
		do{
			ArrayList<Edificio> edificios = new ArrayList<Edificio>( cidade.vertexSet() );
			int rng = (int)(Math.random() * edificios.size());
			atual = edificios.get( rng );
		}while(!(atual instanceof Sucursal));
		
		//Fazer a rota até todos os pacientes estarem em sucursais
		do{
			ArrayList<Estrada> estradas = new ArrayList<Estrada>( cidade.outgoingEdgesOf(atual) );
			int rng = (int)(Math.random() * estradas.size());
			
			
		}while(nrPacientesRestantes > 0);
		
		reset();
		return rota;
	}

	private void reset() {
		this.ambulancia = ambulanciaInicial.clone();
		nrPacientesRestantes = nrPacientesInciais;
	}
	
	public static void main(String[] args) throws IOException{
		Clinica cli = new Clinica();
		ListenableUndirectedWeightedGraph<Edificio, Estrada> city = cli.parseGrafoCidade("grafoCidade.txt");
		Gerador g = new Gerador(new Ambulancia(8), 40, city);
		//Rota r = g.geraRota();
		ArrayList<Edificio> edificios = new ArrayList<Edificio>( city.vertexSet() );
		ArrayList<Estrada> est1 = new ArrayList<Estrada>(city.edgesOf(edificios.get(1)));
		//ArrayList<Estrada> est2 = new ArrayList<Estrada>(city.incomingEdgesOf(edificios.get(1)));
		System.out.println(edificios.get(1).nome);
		//System.out.println(est2);
		for (int i=0; i<est1.size(); i++){
			System.out.println(
					city.getEdgeSource(est1.get(i)).nome+" - "+
					city.getEdgeTarget(est1.get(i)).nome);
		}
		
	}
}
