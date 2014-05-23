package geneticAlgorithm;

import org.jgrapht.Graph;

import logic.Edificio;
import logic.Estrada;

public class GeraPopulacao {

	public static Populacao gerar(Graph<Edificio, Estrada> cidade, int tamanho){
		Populacao inicial = new Populacao(tamanho);
		
		return inicial;
	}
}
