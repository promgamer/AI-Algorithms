package simulated.annealing;

import graphicInterface.Clinica;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Vector;

import logic.Ambulancia;
import logic.Bomba;
import logic.Edificio;
import logic.Estrada;
import logic.Habitacao;
import logic.Sucursal;

import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Gerador {

	private Ambulancia ambulanciaInicial = null;
	private Ambulancia ambulancia = null;
	private int nrPacientesInciais = 0;
	private int nrPacientesRestantes = 0;
	private String graphPath = null;
	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;
	private boolean rotaReutilizada = false;
	public Gerador(Ambulancia ambulancia, int nrPacientes, String graphPath) throws IOException{
		this.ambulanciaInicial  = ambulancia.clone();
		this.ambulancia = ambulancia.clone();
		this.nrPacientesInciais = nrPacientes;
		this.nrPacientesRestantes = nrPacientes;
		this.graphPath = graphPath;
		this.cidade = Clinica.parseGrafoCidade(graphPath);
	}

	public Rota geraRota(Rota rotaAtual) throws IOException {
		
		Rota rota = null;
		Edificio atual = null;
		// boleanos para controlar o combustivel e pacientes "deixados para tras"
		boolean prioridadeSucursal = false, prioridadeBomba = false;
		
		ArrayList<Edificio> edificios = new ArrayList<Edificio>( cidade.vertexSet() );
		
		if(rotaAtual!=null){
			rotaReutilizada = true;
			//escolher uma posição aleatória para alterar
			int edificioRandom = (int)(Math.random() * rotaAtual.getRota().size());
			rota = new Rota(rotaAtual, edificioRandom);
			nrPacientesRestantes = rota.getUltimoNrPacientes();
			prioridadeBomba = rota.getUltimoEstado().getKey();
			prioridadeSucursal = rota.getUltimoEstado().getValue();
			ambulancia.consumir(ambulancia.combustivel_restante()-rota.getUltimoEstadoAmbulancia().getValue());
			ambulancia.ocupar(rota.getUltimoEstadoAmbulancia().getKey());
			
			//pré-processar o grafo da cidade para despovoar as habitações necessárias
			Vector<Edificio> tmpEdfs = rota.getRota();
			for(int i=0;i<tmpEdfs.size();i++){
				Edificio tE = tmpEdfs.get(i);
				if(!(tE instanceof Bomba)){
					for(int k=0; k<edificios.size(); k++){
						if(tE.ID == edificios.get(k).ID){
							edificios.get(k).setOcupantes(tE.getOcupantes());
							if(edificios.get(k).ID == rota.getUltimoEdificio().ID){
								atual = edificios.get(k);
							}
							break;
						}
					}
				}
			}
		}else{
			rota = new Rota();
			
			//Escolher edificio inicial -> tem de ser uma sucursal(apenas sucursais têm garagem e a ambulancia)
			do{
				int rng = (int)(Math.random() * edificios.size());
				atual = edificios.get( rng );
			}while(!(atual instanceof Sucursal));

		}

		/* ---- Fazer a rota até todos os pacientes estarem em sucursais ---- */

		do{
			/* Estatísticas
			System.out.println("Combustível Disponível: "+ambulancia.combustivel_restante());
			System.out.println("Pacientes na ambulancia: "+ambulancia.getOcupantes());
			System.out.println("Nr. Pacientes Restantes: "+nrPacientesRestantes);
			System.out.println(" - - - - - - - - - - - - - - ");
			 */

			// todas as estradas que saem do edificio
			ArrayList<Estrada> estradas = new ArrayList<Estrada>(cidade.edgesOf(atual));

			if (!rotaReutilizada) {
				//obter o maximo de distancia possivel percorrer nos proximos 2 nós -> previsão de combustivel
				double max = getMaxDistancia(atual);
				if (ambulancia.combustivel_restante() <= max)
					prioridadeBomba = true;
				if (atual instanceof Habitacao) {
					if (!ambulancia.ocupar(atual.getOcupantes())) {
						int esp = ambulancia.getEspacoDisponivel();
						atual.retirarOcupantes(esp);
						ambulancia.ocupar(esp);
						prioridadeSucursal = true;
					} else {
						atual.esvaziaEdificio();
						if (ambulancia.getEspacoDisponivel() == 0)
							prioridadeSucursal = true;
					}
				} else if (atual instanceof Sucursal) {
					int nOcupantes = 0, espDisponivel = 0;
					/* parece desnecessário mas é essencial o controlo
					  devido à sucursal poder não ter espaço suficiente */
					if ((espDisponivel = atual.getEspacoDisponivel()) < (nOcupantes = ambulancia
							.getOcupantes())) {
						ambulancia.retirar(espDisponivel);
						((Sucursal) atual).adicionaOcupantes(espDisponivel);
						nrPacientesRestantes -= espDisponivel;
					} else {
						ambulancia.retirar(nOcupantes);
						((Sucursal) atual).adicionaOcupantes(nOcupantes);
						nrPacientesRestantes -= nOcupantes;
					}
				} else if (atual instanceof Bomba) {
					ambulancia.abastecer();
					prioridadeBomba = false;
				}
				
				// adicionar o edificio atual à rota antes de progredir
				rota.adicionarEdificio(atual, nrPacientesRestantes);
				rota.addEstado(new AbstractMap.SimpleEntry<Boolean, Boolean>(prioridadeBomba,prioridadeSucursal));
				rota.addEstadoAmbulancia(
						new AbstractMap.SimpleEntry<Integer, Double>(
								ambulancia.getOcupantes(),
								ambulancia.combustivel_restante()));
			}
			
			rotaReutilizada = false;
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

	private void reset() throws IOException {
		this.ambulancia = ambulanciaInicial.clone();
		this.cidade = Clinica.parseGrafoCidade(graphPath);
		nrPacientesRestantes = nrPacientesInciais;
		rotaReutilizada = false;
	}

	public static void main(String[] args) throws IOException{
		Gerador g = new Gerador(new Ambulancia(8), 18, "grafoCidade.txt");
		SimulatedAnnealing sm = new SimulatedAnnealing(10000, 0.0003, 0.001, g);
		long startTime = System.nanoTime();
		sm.run(false);
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/Math.pow(10, 9);
		System.out.println("\nTempo de Execução: "+duration+" segundos.");
	}
}
