package logic;

public class Ambulancia {
	final static int combustivel_max = 30;
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
		combustivel -= km;
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
	
	public int getEspacoDisponivel(){
		return capacidade_ocupantes - nrOcupantes;
	}
	
	public int getOcupantes(){
		return nrOcupantes;
	}
	
	public boolean isFull(){
		if( nrOcupantes == capacidade_ocupantes )
			return true;
		else return false;
	}
	
	public void retirar(int n){
		if( n <= nrOcupantes ){
			nrOcupantes -= n;
		}
	}
	
	public Ambulancia clone(){
		return new Ambulancia(capacidade_ocupantes);
	}
}
