package geneticAlgorithm;

import graphicInterface.Clinica;

import java.util.Random;
import java.util.Vector;

import logic.Ambulancia;
import logic.Bomba;
import logic.Edificio;
import logic.Estrada;
import logic.Habitacao;
import logic.Sucursal;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Ambiente {

	/** Static Members **/
	private static int capacidade_ambulancia;
	public static final int NoInicial = 1;
	private static final int PONTOS_POR_TRANSPORTAR = 2;
	private static final double PONTOS_DISTANCIA = 0.4;
	private static final double PONTOS_ENTREGUE = 1.5;
	private static final int PONTOS_RECOLHIDO = 1;
	private static String graphPath;
	
	/** Non-static Members **/
	private Vector<Integer> rota;
	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade;
	private Ambulancia ambulancia;
	
	/** Variavel Especial Usada apenas para o mostrar o resultado final**/
	private Rota r;
	private Vector<String> percurso;
	private boolean mostra_melhor;
	
	/** Construtor Default **/
	@SuppressWarnings("unchecked")
	public Ambiente(Vector<Integer> r){
		cidade = Clinica.parseGrafoCidade(graphPath);
		this.rota = (Vector<Integer>) r.clone();
		mostra_melhor = false;
	}
	
	/** Construtor para mostrar o melhor resultado **/
	@SuppressWarnings("unchecked")
	public Ambiente(Vector<Integer> r, boolean mostrar){ 
		cidade = Clinica.parseGrafoCidade(graphPath);
		this.rota = (Vector<Integer>) r.clone();
		mostra_melhor = mostrar;
		
		if( mostra_melhor )
			percurso = new Vector<String>();
	}
	
	
	public double calculaAdaptacao() {
		
		// Cria uma nova ambulancia
		ambulancia = new Ambulancia(capacidade_ambulancia);
		
		/* Variaveis de contagem */
		double pacientes_recolhidos = 0;
		double pacientes_entregues = 0;
		double distancia_percorrida = 0;
		int total_pacientes = pacientesPorTransportar();
		
		/* Variveis de verificacao de rota */
		int idAtual = NoInicial;
		int idAntigo = 0;
		
		if( mostra_melhor)
			percurso.add(obtemVertice(idAtual).nome);

		// CICLO
		while(ambulancia.combustivel_restante() > 0 && rota.size() != 0){
			
			//guarda o antigo
			idAntigo = idAtual;
						
			//obtem o no atual
			idAtual = rota.remove(0);
			
			if(idAntigo == idAtual)
				continue;

			Edificio edAtual = obtemVertice(idAtual);
			Edificio edAntigo = obtemVertice(idAntigo);
			
			//Verifica se existe uma ligacao entre os dois edificios
			if( !cidade.containsEdge(edAntigo, edAtual) )
				break;
			
			//calcula a distancia percorrida
			double distancia = calculaDistancia(edAntigo, edAtual);
			ambulancia.consumir(distancia);
			
			//verificação dupla da distancia a percorrer
			if( ambulancia.combustivel_restante() < 0)
				break;
			
			// update do contador da distancia
			distancia_percorrida += distancia;
			
			// Verifica se é fim de rota
			if( verificaFimDeRota(edAtual) == true )
				break;

			if( mostra_melhor)
				percurso.add(obtemVertice(idAtual).nome);
			
			
			/** Verifica as habitações e faz ações consoante o seu tipo **/
			
			if( edAtual instanceof Habitacao ){
				
				if( !ambulancia.isFull()){ //verifica se não esta cheia
					
					int retirados = edAtual.retirarOcupantes( ambulancia.getEspacoDisponivel() );
					ambulancia.ocupar( retirados );
					
					// aumenta o contador
					pacientes_recolhidos += retirados;
				}
					
			} else if( edAtual instanceof Bomba ){
				
				ambulancia.abastecer();
				
			} else if( edAtual instanceof Sucursal ){
				int adicionados = ((Sucursal) edAtual).adicionaOcupantes( ambulancia.getOcupantes() );
				ambulancia.retirar(adicionados);
				
				//aumenta o contador
				pacientes_entregues += adicionados;
				
			} else { System.out.println("ERRO! Classe Inválida: " + edAtual.getClass()); }
			
		} // FIM DO WHILE
		
		// Calcula o fitness deste individuo
		double fitness;
		
		if(distancia_percorrida != 0)
			fitness = PONTOS_RECOLHIDO * pacientes_recolhidos + PONTOS_ENTREGUE * pacientes_entregues - PONTOS_DISTANCIA * distancia_percorrida - PONTOS_POR_TRANSPORTAR * pacientesPorTransportar();
		else fitness = 0;
		
		if( fitness < 0)
			fitness = (PONTOS_RECOLHIDO * pacientes_recolhidos + PONTOS_ENTREGUE * pacientes_entregues) * 0.001;
		
		
		
		/** Utilizado apenas para a impressao do resultado final **/
		if( mostra_melhor)
			r = new Rota(pacientes_entregues, pacientes_recolhidos, distancia_percorrida, fitness, percurso, total_pacientes);
		
		return fitness;
		
	}
	
	private Edificio obtemVertice(int ID) {
		Vector<Edificio> edificios = new Vector<>(cidade.vertexSet());
		
		for(int i = 0; i < edificios.size(); i++){
			if(edificios.elementAt(i).ID == ID)
				return edificios.elementAt(i);
		}
		
		return null;
	}
	
	/** Devolve o numero de pacientes que ainda se encontram em edificios **/
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
	
	/** Atualiza o caminho para o grafo a utilizar **/
	public static void setGraphPath(String path){
		graphPath = path;
	}


	public void imprimeMelhor() {
		System.out.println(r);
	}

}
