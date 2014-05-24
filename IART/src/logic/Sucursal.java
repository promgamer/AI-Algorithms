package logic;

import logic.Edificio;

public class Sucursal extends Edificio{
	
	public Sucursal(String nome, int capacidade){
		super(nome, 0, capacidade);

	}
	
	/** Adiciona n ocupantes ao edificio **/
	public int adicionaOcupantes(int n){
		if( n > getEspacoDisponivel() )
			n = getEspacoDisponivel();
		
		ocupantes += n;
		return n;
	}

}
