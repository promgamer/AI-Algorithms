package logic;

public class Ambulancia {
	final static int combustivel_max = 80; // 80 litros
	final static float consumo_km = 0.08f; // 8 l / 100 km
	final int capacidade_ocupantes;
	private int nrOcupantes;
	private float combustivel;
	
	public Ambulancia(int capacidade){
		capacidade_ocupantes = capacidade;
		nrOcupantes = 0;
		combustivel = combustivel_max;
	}
	
	public void abastecer(){
		combustivel = combustivel_max;
	}
	
	public void consumir(int km){
		combustivel -= km*consumo_km;
	}
	
	public float combustivel_restante(){
		return combustivel;
	}
	
	public boolean ocupar(int nr){
		if( nrOcupantes + nr > capacidade_ocupantes)
			return false;

		nrOcupantes += nr;
		return true;

	}
	
	public int getOcupantes(){
		return nrOcupantes;
	}
}
