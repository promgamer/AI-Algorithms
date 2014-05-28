package geneticAlgorithm;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import logic.Clinica;

public class Individuo {
	
	private Vector<Integer> genes;
	private static int genesOpt;
	
	/** Adaptacao deste individuo ao meio **/
	private double adaptacao;
	
	/** Construtor default **/
	public Individuo(int genesSize, int genesOption){
		adaptacao = 0;
		genesOpt = genesOption;
		genes = new Vector<Integer>();
		
		
		/** Genes random **/
		for (int i = 0; i < genesSize; i++) 
            genes.add(geraGene()); // Intervalo [1,genesOpt]
	}

	public int geraGene() {
		Random r = new Random();
		return r.nextInt(genesOpt) + 1;
	}
	
	/** Construtor para um individuo com genes especificos **/
	public Individuo(Vector<Integer> genes){
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
	public double getAdaptacao(){
		if (adaptacao == 0){ // calcula adaptacao primeiro, se ainda nao tiver sido calculada
			
			
			Ambiente e;
			try {
				e = new Ambiente(Clinica.parseGrafoCidade("C:\\Users\\Miguel\\Documents\\GitHub\\IART\\IART\\grafoCidade2.txt"), genes);
				adaptacao = e.calculaAdaptacao();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
		return adaptacao;
	}

	@Override
	public String toString() {
		return new Double(adaptacao).toString();
	}
	
	public void imprimeGenes(){
		for(int i = 0; i < genes.size(); i++)
			System.out.print(genes.elementAt(i) + " ");
		
		System.out.println("");
	}

}
