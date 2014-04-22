package logic;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Estrada extends DefaultWeightedEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6558616997856126947L;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Double.toString(getWeight());
	}

	
}
