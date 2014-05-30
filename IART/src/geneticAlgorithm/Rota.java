package geneticAlgorithm;

import java.util.Vector;


public class Rota{
	
	private double entregues;
	private double recolhidos;
	private double distancia;
	private double fitness;
	private int totais;
	private Vector<String> rota;
	
	public Rota(double pacientes_entregues, double pacientes_recolhidos, double distancia, double fitness, Vector<String> percurso, int totais){
		this.entregues = pacientes_entregues;
		this.recolhidos = pacientes_recolhidos;
		this.distancia = distancia;
		this.fitness = fitness;
		this.rota = percurso;
		this.totais = totais;
	}
	
	@Override
	public String toString() {
		return "*** Resultado *** \n" +
				"Rota:" + mostraRotaMinima() + "\n" +
				"Nr Deslocacoes: " + (rota.size()-1) + " || Fitness: " + fitness + " || Distancia Percorrida: " + distancia + "\n" +
				"Pacientes No Mapa: " + totais + " || Recolhidos: " + recolhidos + " || Entregues: " + entregues;
	}

	private String mostraRotaMinima() {
		String resultado = "";
		
		for( int i = 0 ; i < rota.size(); i++)
			resultado += " " + rota.elementAt(i);
		
		return resultado;
	}


	
	

}
