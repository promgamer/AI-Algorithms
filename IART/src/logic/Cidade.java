package logic;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;
import org.jgrapht.graph.UndirectedWeightedSubgraph;

public class Cidade extends UndirectedWeightedSubgraph<Edificio, Estrada> {

	private static final long serialVersionUID = 8558234603079907103L;

	public Cidade(WeightedGraph<Edificio, Estrada> base1) {
		super(base1, null, null);
	}


	
	

}
