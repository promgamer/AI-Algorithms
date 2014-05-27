package logic;

public class Edificio {
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static final int DefaultCapacidadeMaxima = 50;
	
	public static int lastID = 1;
	public int ID;
	public String nome;
	protected int ocupantes;
	protected int capacidadeMaxima;
	
	/** Construtor que aplica a capacidade maxima defaul **/
	Edificio(String nome, int ocupantes){
		this.nome = nome;
		this.ocupantes = ocupantes;
		this.capacidadeMaxima = DefaultCapacidadeMaxima;
		ID = lastID++;
	}
	
	/** Construtor que permite definir a capacidade maxima do Edificio **/
	Edificio(String nome, int ocupantes, int capacidadeMaxima){
		this.nome = nome;
		this.ocupantes = ocupantes;
		this.capacidadeMaxima = capacidadeMaxima;
		ID = lastID++;
	}
	
	/** Obtem o numero de ocupantes de um edificio **/
	public int getOcupantes(){
		return ocupantes;
	}
	
	/** Obtem a capacidade Maxima deste edificio **/
	public int getCapacidade(){
		return capacidadeMaxima;
	}
	
	/** Obtem o espaço disponivel na clinica **/
	public int getEspacoDisponivel(){
		return capacidadeMaxima - ocupantes;
	}
	
	/** Retira n ocupantes do edificio **/
	public int retirarOcupantes(int n){
		if( n > ocupantes ){ //nao podem ser retiradas mais pessoas do que aquelas que estao num edificio
			n = ocupantes;
		}
		
		ocupantes -= n;
		
		return n;
	}
	
	/** Retira todos os ocupantes do edificio **/
	public int esvaziaEdificio(){
		return retirarOcupantes(ocupantes);
	}
	
	public String toString(){
		return nome;
	}

}
