package logic;

import logic.Edificio;

public class Sucursal extends Edificio{
	
	public Sucursal(String nome, int capacidade){
		super(nome, 0, capacidade);

	}
	
	/** 
	 * Tenta adicionar n ocupantes ao edificio 
	 * e devolve o numero de ocupantes
	 * efetivamente colocados no edificio
	 * **/
	public int adicionaOcupantes(int n){
		int adicionados;
		if( n > getEspacoDisponivel())
			adicionados = getEspacoDisponivel();
		else adicionados = n;
		
		return adicionados;
	}

}
