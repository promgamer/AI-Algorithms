package geneticAlgorithm;

import java.io.IOException;
import java.util.Vector;

import logic.Ambulancia;
import logic.Bomba;
import logic.Cidade;
import logic.Clinica;
import logic.Edificio;
import logic.Estrada;
import logic.Habitacao;
import logic.Sucursal;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Ambiente {

	/** Static Members **/
	private static int capacidade_ambulancia;
	
	public static int NoInicial = 1;
	
	/** Non-static Members **/
	private Vector<Integer> rota;
	private Cidade cidade;
	private Ambulancia ambulancia;
	
	public Ambiente(Cidade c, Vector<Integer> r){
		this.cidade = c;
		this.rota = r;
	}
	
	
	public double calculaAdaptacao() {
		
		// Cria uma nova ambulancia
		ambulancia = new Ambulancia(capacidade_ambulancia);
		
		/* Variaveis de contagem */
		double pacientes_recolhidos = 0;
		double pacientes_entregues = 0;
		double distancia_percorrida = 0;
		
		/* Variveis de verificacao de rota */
		int idAtual = NoInicial;
		int idAntigo;
		
		
		// Verifica se a rota é possivel
		// isto é, se existem edges que ligam os vertices
		// se a rota é inválida, então este é um mau individuo neste ambiente
		if( verificaRota(rota) == false){
			System.out.println("Vai sair");
			return 0;
		}
		

		// CICLO
		while(ambulancia.combustivel_restante() != 0 && rota.size() != 0){
			
			// Guarda o no atual como antigo
			idAntigo = idAtual;
			
			//obtem o no atual
			idAtual = rota.remove(0);
			
			if(idAntigo == idAtual)
				continue;

			
			Edificio e = obtemVertice(idAtual);
			
			
			//calcula a distancia percorrida
			distancia_percorrida += calculaDistancia(obtemVertice(idAntigo), e);
			
			// Verifica se é fim de rota
			if( verificaFimDeRota(e) == true ){
				System.out.println("break");
				break;
			}
				
			
			/** Verifica as habitações e faz ações consoante o seu tipo **/
			
			if( e instanceof Habitacao ){
				
				if( !ambulancia.isFull()){ //verifica se não esta cheia
					
					int retirados = e.retirarOcupantes( ambulancia.getEspacoDisponivel() );
					ambulancia.ocupar( retirados );
					
					// aumenta o contador
					pacientes_recolhidos += retirados;
				}
					
			} else if( e instanceof Bomba ){
				
				ambulancia.abastecer();
				
			} else if( e instanceof Sucursal ){
				System.out.println("aqui");
				int adicionados = ((Sucursal) e).adicionaOcupantes( ambulancia.getOcupantes() );
				ambulancia.retirar(adicionados);
				
				//aumenta o contador
				pacientes_entregues += adicionados;
				
			} else { System.out.println("ERRO! Classe Inválida"); }
			
		} // FIM DO WHILE
		
		// Calcula o fitness deste individuo
		double fitness;
		if(distancia_percorrida != 0)
			 fitness = (pacientes_entregues + pacientes_recolhidos) / distancia_percorrida;
		else fitness = 0;
		
		System.out.println("entregues:" + pacientes_entregues +" ; recolhidos: " + pacientes_recolhidos + "; distancia: " + distancia_percorrida + " ; fitness: " + fitness);
		
		return fitness;
		
	}
	
	/** Verifica se uma rota é valida 
	 * 
	 * Para uma rota ser válida, é necessário que existam
	 * ligaçoes entre todos os pares de vertices
	 * 
	 * **/
	private boolean verificaRota(Vector<Integer> rota){
		
		for(int i = 0; i < rota.size()-1; i++){
			
			//obtem um par de vertices
			Edificio e1 = obtemVertice(i);
			Edificio e2 = obtemVertice(i+1);
			
			// verifica se existe uma ligacao entre os dois edificios
			if( !cidade.containsEdge(e1,e2) && e1 != null && e2 != null ){
				System.out.println("E1: " + e1.nome + "  E2: " + e2.nome);
				return false;
			}
		}
		
		return true;
	}

	private Edificio obtemVertice(int ID) {
		Vector<Edificio> edificios = new Vector<>(cidade.vertexSet());
		
		for(int i = 0; i < edificios.size(); i++){
			if(edificios.elementAt(i).ID == ID)
				return edificios.elementAt(i);
		}
		
		return null;
	}
	
	private int pacientesPorTransportar(){
		Vector<Edificio> edificios = new Vector<>(cidade.vertexSet());
		
		int contador = 0;
		
		for(int i = 0; i < edificios.size();i++){
			if(edificios.elementAt(i) instanceof Habitacao)
				contador += edificios.elementAt(i).getOcupantes();
		}
		
		return contador;
	}
	
	/** Verifica se esta sucursal é fim de rota, isto é
	 * se no momento que a ambulancia visita esta no,
	 * todos os pacientes foram transportados
	 * **/
	private boolean verificaFimDeRota(Edificio e){
		if(pacientesPorTransportar() == 0 && e instanceof Sucursal && ambulancia.getOcupantes() == 0)
			return true;
		
		return false;
	}
	
	/** Calcula a distancia percorrida entre dois nos **/
	private int calculaDistancia(Edificio N1, Edificio N2){
		return (int) cidade.getEdgeWeight(cidade.getEdge(N1, N2));
	}
	
	/** Define a capacidade da ambulancia **/
	public static void setCapacidadeAmbulancia(int i) {
		capacidade_ambulancia = i;
		
	}

}
