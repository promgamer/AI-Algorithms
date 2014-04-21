package geneticAlgorithm;

import java.util.Vector;

public class Populacao {
	
	private Vector<Individuo> individuos;
	
	/** Construtor para gerar uma populacao **/
	Populacao(int tamanhoPopulacao, int tamanhoGenes){
		individuos = new Vector<Individuo>(tamanhoPopulacao);
		
		/** Cria Individuos para essa populuacao **/
		 for (int i = 0; i < tamanhoPopulacao; i++) {
             Individuo novoIndividuo = new Individuo(tamanhoGenes);
             individuos.add(novoIndividuo);
         }
	}
	
	/** Construtor para criar uma populacao vazia **/
	Populacao(int tamanhoPopulacao){
		individuos = new Vector<Individuo>(tamanhoPopulacao);
	}
	
	/** Obtem um individuo na populacao **/
	public Individuo getIndividuo(int pos){
		return individuos.elementAt(pos);
	}
	
	/** Modifica/adiciona um individuo � populacao **/
	public void setIndividuo(int pos, Individuo i){
		individuos.set(pos, i);
	}
	
	/** Get do tamanho do vector de individuos **/
	public int getSize(){
		return individuos.size();
	}
	
	/** Obtem o individuo da populacao melhor adaptado
	 * 
	 * TODO: talvez melhorar a pesquisa da melhor solucao??
	 * 
	 **/
	public Individuo getMelhorAdaptado(){
		Individuo melhor = individuos.elementAt(0);
		
		for (int i = 0; i < individuos.size(); i++) {
            if (melhor.getAdaptacao() < individuos.elementAt(i).getAdaptacao()) {
                melhor = individuos.elementAt(i);
            }
        }
		
		return melhor;
	}

}