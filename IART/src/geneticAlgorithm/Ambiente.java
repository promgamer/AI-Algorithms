package geneticAlgorithm;

import java.util.Vector;

import logic.Ambulancia;
import logic.Bomba;
import logic.Edificio;
import logic.Habitacao;
import logic.Sucursal;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Ambiente {

	private static ListenableUndirectedWeightedGraph<Edificio, DefaultEdge> cidade;
	private static ListenableUndirectedWeightedGraph<Edificio, DefaultEdge> cidade_backup;
	private static Ambulancia ambulancia;
	
	public static int NoInicial = 1;
	
	public static int calculaAdaptacao(Vector<Integer> rota) {
		
		// Verifica se a rota é possivel
		// isto é, se existem edges que ligam os vertices
		// se a rota é inválida, então este é um mau individuo neste ambiente
		if( verificaRota(rota) == false)
			return 0;
		
		//
		while(ambulancia.combustivel_restante() != 0 && rota.size() != 0){
			
			//obtem o no atual
			int idAtual = rota.remove(0);
			Edificio e = obtemVertice(idAtual);
			
			if( e instanceof Habitacao ){
				//verifica se pode levar todos os pacientes desta habitacao
				if(ambulancia.){
					
				}
				
				
				
			} else if( e instanceof Bomba ){
				
			} else if( e instanceof Sucursal ){
				
			}
			
			
			
			
			
			
			
			
			
			
		}
		
	}
	
	/** Verifica se uma rota é valida 
	 * 
	 * Para uma rota ser válida, é necessário que existam
	 * ligaçoes entre todos os pares de vertices
	 * 
	 * **/
	private static boolean verificaRota(Vector<Integer> rota){
		
		for(int i = 0; i < rota.size()-1; i++){
			
			//obtem um par de vertices
			Edificio e1 = obtemVertice(i);
			Edificio e2 = obtemVertice(i+1);
			
			// verifica se existe uma ligacao entre os dois edificios
			if( !cidade.containsEdge(e1,e2) )
				return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	private static Edificio obtemVertice(int ID) {
		Vector<Edificio> edificios = (Vector<Edificio>) cidade.vertexSet();
		
		for(int i = 0; i < edificios.size(); i++){
			if(edificios.elementAt(i).ID == ID)
				return edificios.elementAt(i);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked" )
	private static int pacientesPorTransportar(){
		Vector<Edificio> edificios = (Vector<Edificio>) cidade.vertexSet();
		
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
	private static boolean verificaFimDeRota(Edificio e){
		if(pacientesPorTransportar() == 0 && e instanceof Sucursal && ambulancia.getOcupantes() == 0)
			return true;
		
		return false;
	}

}
