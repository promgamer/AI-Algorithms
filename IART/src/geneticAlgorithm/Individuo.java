package geneticAlgorithm;

import java.util.Random;
import java.util.Vector;

public class Individuo {
	
	private Vector<Integer> genes;
	
	/** Adaptacao deste individuo ao meio **/
	private int adaptacao;
	
	/** Construtor default **/
	Individuo(int genesSize){
		adaptacao = 0;
		genes = new Vector<Integer>(genesSize);
		
		
		/** Genes random **/
		for (int i = 0; i < genesSize; i++) 
            genes.add(geraGene()); // Intervalo [1,genesSize]
	}

	public int geraGene() {
		Random r = new Random();
		return r.nextInt() % genes.size() + 1;
	}
	
	/** Construtor para um individuo com genes especificos **/
	Individuo(Vector<Integer> genes){
		adaptacao = 0;
		this.genes = genes;
	}
	
	/** Obtem um gene do vector de genes do individuo **/
	public int getGene(int pos){
		return genes.elementAt(pos);
	}
	
	/** Modifica um gene do individuo **/
	public void setGene(int pos, int novoGene){
		genes.set(pos, novoGene);
		
		adaptacao = 0; // forca a recalcular a adaptacao
	}
	
	/** Obtem o tamanho do vetor **/
	public int getSize(){
		return genes.size();
	}
	
	
	/** Obtem (e calcula) a adaptacao de um individuo **/
	public int getAdaptacao(){
		if (adaptacao == 0) // calcula adaptacao primeiro, se ainda nao tiver sido calculada
			adaptacao = Ambiente.calculaAdaptacao(genes);
		
		return adaptacao;
	}

}
