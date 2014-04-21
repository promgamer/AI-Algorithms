package logic;

import logic.Edificio;

public class Sucursal extends Edificio{
	
	public Sucursal(String nome, int capacidade){
		super(nome, 0, capacidade);

	}
	
	/** Adiciona n ocupantes ao edificio **/
	public boolean adicionaOcupantes(int n){
		if(ocupantes + n > capacidadeMaxima)
			return false;
		
		ocupantes += n;
		return true;
	}

}
