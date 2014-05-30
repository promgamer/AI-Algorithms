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
	private static String graphPath = null;
	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;
	private boolean first = false;
	private boolean clear = false;
	private boolean aproveita = false;
	
	public Gerador(Ambulancia ambulancia) throws IOException{
		this.cidade = Clinica.parseGrafoCidade(graphPath);
		this.ambulanciaInicial  = ambulancia.clone();
		this.ambulancia = ambulancia.clone();
		this.nrPacientesInciais = nrPacientesTransportar();
		this.nrPacientesRestantes = nrPacientesInciais;
	}
	
	public static void setGraphPath(String path){
		graphPath = path;
	}
	
	private int nrPacientesTransportar(){
		Vector<Edificio> edificios = new Vector<>(cidade.vertexSet());
		
		int contador = 0;
		
		for(int i = 0; i < edificios.size();i++){
			if(edificios.elementAt(i) instanceof Habitacao)
				contador += edificios.elementAt(i).getOcupantes();
		}
		
		return contador;
	}

	@SuppressWarnings("unchecked")
	public Rota geraRota(Rota rotaAtual) throws IOException {
		Rota rota = null;
		Edificio atual = null;
		Vector<Edificio> tmpEdfs = null;
		// boleanos para controlar o combustivel e pacientes "deixados para tras"
		boolean prioridadeSucursal = false, prioridadeBomba = false;
		ArrayList<Edificio> edificios = new ArrayList<Edificio>( cidade.vertexSet() );

		if(rotaAtual!=null){

			first = true;
			aproveita = true;
			//escolher uma posição aleatória para alterar
			int edificioRandom = 1+(int)(Math.random() * rotaAtual.getRota().size());
			rota = new Rota(rotaAtual, edificioRandom);

			tmpEdfs = (Vector<Edificio>) rota.getRota().clone();

			Edificio firstEDF = tmpEdfs.firstElement();
			tmpEdfs.removeElementAt(0);
			for(int j=0; j<edificios.size(); j++){
				if(edificios.get(j).ID == firstEDF.ID){
					atual = edificios.get(j);
					break;
				}
			}

			if( tmpEdfs.size() == 0 ) aproveita = false;
			
		}else{
			rota = new Rota();
			tmpEdfs=new Vector<Edificio>();

			//Escolher edificio inicial -> tem de ser uma sucursal(apenas sucursais têm garagem e a ambulancia)
			do{
				int rng = (int)(Math.random() * edificios.size());
				atual = edificios.get( rng );
			}while(!(atual instanceof Sucursal));

			// adicionar o edificio atual à rota antes de progredir
			rota.adicionarEdificio(atual, nrPacientesRestantes);
			rota.addEstado(new AbstractMap.SimpleEntry<Boolean, Boolean>(prioridadeBomba,prioridadeSucursal));
			rota.addEstadoAmbulancia(
					new AbstractMap.SimpleEntry<Integer, Double>(
							ambulancia.getOcupantes(),
							ambulancia.combustivel_restante()));
			rota.addDistancia(0);
			first = true;
		}

		/* ---- Fazer a rota até todos os pacientes estarem em sucursais ---- */
		
		while(nrPacientesRestantes > 0){

			// todas as estradas que saem do edificio
			ArrayList<Estrada> estradas = new ArrayList<Estrada>(cidade.edgesOf(atual));

			if (!first) {
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
						prioridadeSucursal=false;
					} else {
						ambulancia.retirar(nOcupantes);
						((Sucursal) atual).adicionaOcupantes(nOcupantes);
						nrPacientesRestantes -= nOcupantes;
						prioridadeSucursal=false;
					}
				} else if (atual instanceof Bomba) {
					ambulancia.abastecer();
					prioridadeBomba = false;
				}

				if( !aproveita && clear  ){
					// adicionar o edificio atual à rota antes de progredir
					rota.adicionarEdificio(atual, nrPacientesRestantes);
					rota.addEstado(new AbstractMap.SimpleEntry<Boolean, Boolean>(prioridadeBomba,prioridadeSucursal));
					rota.addEstadoAmbulancia(new AbstractMap.SimpleEntry<Integer, Double>(ambulancia.getOcupantes(),
							ambulancia.combustivel_restante()));
				}
			}

			double cons=0;

			if(!(nrPacientesRestantes > 0)) 
				break;
			else if( aproveita ){ // ainda tem de seguir a rota anterior
				Edificio process = null, firstEDF = tmpEdfs.firstElement();
				tmpEdfs.removeElementAt(0);
				for(int j=0; j<edificios.size(); j++){
					if(edificios.get(j).ID == firstEDF.ID){
						process = edificios.get(j);
						break;
					}
				}
				
				cons=cidade.getEdgeWeight(cidade.getEdge(atual, process));
				atual = process;
				first = false;
				
				if( tmpEdfs.size() == 0 ){ 
					aproveita = false;
					rota.addDistancia(cons);
				}
				
			}
			else{
				// DETERMINAR O NÓ DESTINO
				first = false;
				clear = true;
				boolean gotIt = false;
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
					int rng2;
					Vector<Edificio> tmpEdfs2 = new Vector<Edificio>();

					//Procura todos os nos possiveis para proximos
					for(int i=0; i<estradas.size(); i++){
						Edificio e1 = cidade.getEdgeTarget(estradas.get(i)),
								e2 = cidade.getEdgeSource(estradas.get(i));
						if(e1 != atual) tmpEdfs2.add(e1);
						else tmpEdfs2.add(e2);
					}

					rng2 = (int)(Math.random() * tmpEdfs2.size());
					atual = tmpEdfs2.get(rng2);
					rota.addDistancia((cons=cidade.getEdgeWeight(estradas.get(rng2))));
				}
			}
			ambulancia.consumir(cons);
		}

		/* Estatísticas Finais 
		System.out.println("Combustível Disponível: "+ambulancia.combustivel_restante());
		System.out.println("Nr. Pacientes Restantes: "+nrPacientesRestantes);
		System.out.println(" - - - - - - - END - - - - - - - ");
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
		this.cidade = null;
		this.cidade = Clinica.parseGrafoCidade(graphPath);
		nrPacientesRestantes = nrPacientesInciais;
		first = false;
	}
}
