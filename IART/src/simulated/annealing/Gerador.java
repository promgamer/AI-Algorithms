package simulated.annealing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import logic.Ambulancia;
import logic.Bomba;
import logic.Clinica;
import logic.Edificio;
import logic.Estrada;
import logic.Habitacao;
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
		Edificio atual = null;

		//Escolher edificio inicial -> tem de ser uma sucursal(apenas sucursais têm garagem e a ambulancia)
		do{
			ArrayList<Edificio> edificios = new ArrayList<Edificio>( cidade.vertexSet() );
			int rng = (int)(Math.random() * edificios.size());
			atual = edificios.get( rng );
		}while(!(atual instanceof Sucursal));

		/* ---- Fazer a rota até todos os pacientes estarem em sucursais ---- */
		
		// boleanos para controlar o combustivel e pacientes "deixados para tras"
		boolean prioridadeSucursal = false, prioridadeBomba = false;

		do{
			ArrayList<Estrada> estradas = new ArrayList<Estrada>( cidade.edgesOf(atual) );

			//obter o maximo de distancia possivel percorrer nos proximos 2 nós -> previsão de combustivel
			double max = getMaxDistancia(atual);
			if( ambulancia.combustivel_restante() <= max)
				prioridadeBomba = true;

			if(atual instanceof Habitacao){
				if(!ambulancia.ocupar(atual.getOcupantes())){
					int esp = ambulancia.getEspacoDisponivel();
					atual.retirarOcupantes(esp);
					ambulancia.ocupar(esp);
					prioridadeSucursal = true;
				} else{
					atual.esvaziaEdificio();
					if(ambulancia.getEspacoDisponivel() == 0)
						prioridadeSucursal = true;
				}
			} else if (atual instanceof Sucursal){
				int nPacientes = 0;
				/* parece desnecessário mas é essencial o controlo
				  devido à sucursal poder não ter espaço suficiente */
				if((nPacientes=atual.getEspacoDisponivel()) < ambulancia.getOcupantes()){
					ambulancia.retirar(nPacientes);
					((Sucursal) atual).adicionaOcupantes(nPacientes);
				}
				else {
					ambulancia.retirar(nPacientes);
					((Sucursal) atual).adicionaOcupantes(nPacientes);
				}
				nrPacientesRestantes -= nPacientes;
			} else if(atual instanceof Bomba){
				ambulancia.abastecer();
				prioridadeBomba = false;
			}
			
			rota.adicionarEdificio(atual); // adicionar o edificio atual à rota antes de progredir
			
			boolean gotIt = false;
			if( prioridadeBomba ){ // escolhe a 1a Bomba disponivel
				for(int i = 0; i < estradas.size(); i++){
					Edificio edf = cidade.getEdgeTarget(estradas.get(i));
					if(edf instanceof Bomba){
						atual = edf;
						rota.addDistancia(cidade.getEdgeWeight(estradas.get(i)));
						gotIt = true;
						prioridadeBomba = false;
						break;
					}
				}
			}
			else if( prioridadeSucursal ){// escolhe a 1a Sucursal com espaço disponivel
				for(int i = 0; i < estradas.size(); i++){
					Edificio edf = cidade.getEdgeTarget(estradas.get(i));
					if(edf instanceof Sucursal){
						if(edf.getEspacoDisponivel() == 0)
							continue;
						atual = edf;
						rota.addDistancia(cidade.getEdgeWeight(estradas.get(i)));
						gotIt = true;
						prioridadeSucursal = false;
						break;
					}
				}
			}
			
			if( !gotIt ){
				Edificio temp = null;
				int rng2;
				do{
					rng2 = (int)(Math.random() * estradas.size());
					temp = cidade.getEdgeTarget(estradas.get(rng2));
				}while(atual == temp );
				atual = temp;
				rota.addDistancia(cidade.getEdgeWeight(estradas.get(rng2)));
			}
		}while(nrPacientesRestantes > 0);

		reset();
		return rota;
	}

	private double getMaxDistancia(Edificio atual) {
		double max = 0;

		for( Estrada e : cidade.edgesOf(atual) ){
			double t = cidade.getEdgeWeight(e);
			Edificio edf = cidade.getEdgeTarget(e);

			double t2 = 0;
			for(Estrada e2 : cidade.edgesOf(edf)){
				double t3 = cidade.getEdgeWeight(e2);
				if( t3 > t2 ) t2 = t3;
			}
			
			if( t + t2 > max) max = t + t2;
		}
		
		return max;
	}

	private void reset() {
		this.ambulancia = ambulanciaInicial.clone();
		nrPacientesRestantes = nrPacientesInciais;
	}

	public static void main(String[] args) throws IOException{
		Clinica cli = new Clinica();
		ListenableUndirectedWeightedGraph<Edificio, Estrada> city = cli.parseGrafoCidade("grafoCidade.txt");
		Gerador g = new Gerador(new Ambulancia(8), 8, city);
		Rota r = g.geraRota();
		r.print();

	}
}
