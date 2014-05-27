package logic;

import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class GraphBulider {

	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade;
	public GraphBulider(
			ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade) {
		this.cidade = cidade;
	}
	public ListenableUndirectedWeightedGraph<Edificio, Estrada> build() {
		return cidade;
	}

}
