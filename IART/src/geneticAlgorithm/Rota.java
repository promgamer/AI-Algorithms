package geneticAlgorithm;


public class Rota{
	
	private double entregues;
	private double recolhidos;
	private double distancia;
	private double fitness;
	
	public Rota(double pacientes_entregues, double pacientes_recolhidos, double distancia, double fitness){
		this.entregues = pacientes_entregues;
		this.recolhidos = pacientes_recolhidos;
		this.distancia = distancia;
		this.fitness = fitness;
	}
	
	@Override
	public String toString() {
		return "Recolhidos: " + recolhidos + " || Entregues: " + entregues + " || Distancia: " + distancia + " || Fitness: " + fitness;
	}


	
	

}
