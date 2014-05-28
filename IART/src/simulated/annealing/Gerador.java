package simulated.annealing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

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
		this.nrPacientesInciais = nrPacientes;
		this.nrPacientesRestantes = nrPacientes;
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
			/* Estatísticas
			System.out.println("Combustível Disponível: "+ambulancia.combustivel_restante());
			System.out.println("Nr. Pacientes Restantes: "+nrPacientesRestantes);
			System.out.println(" - - - - - - - - - - - - - - ");
			*/
			
			// todas as estradas que saem do edificio
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
				int nOcupantes=0, espDisponivel=0;
				/* parece desnecessário mas é essencial o controlo
				  devido à sucursal poder não ter espaço suficiente */
				if((espDisponivel=atual.getEspacoDisponivel()) < (nOcupantes=ambulancia.getOcupantes())){
					ambulancia.retirar(espDisponivel);
					((Sucursal) atual).adicionaOcupantes(espDisponivel);
					nrPacientesRestantes -= espDisponivel;
				}
				else {
					ambulancia.retirar(nOcupantes);
					((Sucursal) atual).adicionaOcupantes(nOcupantes);
					nrPacientesRestantes -= nOcupantes;
				}
			} else if(atual instanceof Bomba){
				ambulancia.abastecer();
				prioridadeBomba = false;
			}
			
			rota.adicionarEdificio(new Edificio(atual)); // adicionar o edificio atual à rota antes de progredir
			
			boolean gotIt = false;
			double cons=0;
			if( prioridadeBomba ){ // escolhe a 1a Bomba disponivel
				for(int i = 0; i < estradas.size(); i++){
					Edificio edfT = cidade.getEdgeTarget(estradas.get(i));
					Edificio edfS = cidade.getEdgeSource(estradas.get(i));
					if(edfT instanceof Bomba && edfT != atual){
						atual = edfT;
						rota.addDistancia((cons=cidade.getEdgeWeight(estradas.get(i))));
						
						gotIt = true;
						prioridadeBomba = false;
						break;
					}
					else if(edfS instanceof Bomba && edfS != atual){
						atual = edfS;
						rota.addDistancia((cons=cidade.getEdgeWeight(estradas.get(i))));
						gotIt = true;
						prioridadeBomba = false;
						break;
					}
				}
			}
			else if( prioridadeSucursal ){// escolhe a 1a Sucursal com espaço disponivel
				for(int i = 0; i < estradas.size(); i++){
					Edificio edfT = cidade.getEdgeTarget(estradas.get(i));
					Edificio edfS = cidade.getEdgeSource(estradas.get(i));
					if(edfT instanceof Sucursal && edfT != atual){
						if(edfT.getEspacoDisponivel() == 0)
							continue;
						atual = edfT;
						rota.addDistancia((cons=cidade.getEdgeWeight(estradas.get(i))));
						gotIt = true;
						prioridadeSucursal = false;
						break;
					}
					else if(edfS instanceof Sucursal && edfS != atual){
						if(edfS.getEspacoDisponivel() == 0)
							continue;
						atual = edfS;
						rota.addDistancia((cons=cidade.getEdgeWeight(estradas.get(i))));
						gotIt = true;
						prioridadeSucursal = false;
						break;
					}
				}
			}
			
			if( !gotIt ){
				Edificio temp = null;
				int rng2;
				Vector<Edificio> tmpEdfs = new Vector<Edificio>();
				
				//Procura todos os nos possiveis para proximos
				for(int i=0; i<estradas.size(); i++){
					Edificio e1 = cidade.getEdgeTarget(estradas.get(i)),
							e2 = cidade.getEdgeSource(estradas.get(i));
					if(e1 != atual) tmpEdfs.add(e1);
					else tmpEdfs.add(e2);
				}
				
				do{
					rng2 = (int)(Math.random() * tmpEdfs.size());
					temp = tmpEdfs.get(rng2);
				}while(atual == temp );
				atual = temp;
				rota.addDistancia((cons=cidade.getEdgeWeight(estradas.get(rng2))));
			}
			ambulancia.consumir(cons);
		}while(nrPacientesRestantes > 0);
		
		/* Estatísticas Finais
		System.out.println("Combustível Disponível: "+ambulancia.combustivel_restante());
		System.out.println("Nr. Pacientes Restantes: "+nrPacientesRestantes);
		System.out.println(" - - - - - - - - - - - - - - ");
		*/
		reset();
		return rota;
	}

	private double getMaxDistancia(Edificio atual) {
		double max = 0;

		for( Estrada e : cidade.edgesOf(atual) ){
			double t = cidade.getEdgeWeight(e);
			Edificio edf=null;
			if((edf = cidade.getEdgeTarget(e)) == atual)
				edf = cidade.getEdgeSource(e);

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
		ListenableUndirectedWeightedGraph<Edificio, Estrada> city = Clinica.parseGrafoCidade("grafoCidade.txt");
		Gerador g = new Gerador(new Ambulancia(8), 8, city);
		//Rota r = g.geraRota();
		//r.print();
		SimulatedAnnealing sm = new SimulatedAnnealing(100, 0.03, 2, g);
		sm.run();
	}
}
