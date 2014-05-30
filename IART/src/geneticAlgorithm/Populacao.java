package geneticAlgorithm;

import java.util.Random;
import java.util.Vector;

public class Populacao {
	
	private Vector<Individuo> individuos;
	
	/** Construtor para gerar uma populacao **/
	public Populacao(int tamanhoPopulacao, int tamanhoGenes, int opcoesGenes){
		individuos = new Vector<Individuo>();
		
		/** Cria Individuos para essa populuacao **/
		 for (int i = 0; i < tamanhoPopulacao; i++) {
             Individuo novoIndividuo = new Individuo(tamanhoGenes, opcoesGenes);
             individuos.add(novoIndividuo);
         }
	}
	
	/** Construtor para criar uma populacao vazia **/
	public Populacao(int tamanhoPopulacao){
		individuos = new Vector<Individuo>();
	}
	
	/** Obtem um individuo na populacao **/
	public Individuo getIndividuo(int pos){
		return individuos.elementAt(pos);
	}
	
	/** Modifica/adiciona um individuo à populacao **/
	public void setIndividuo(int pos, Individuo i){
		if( pos >= individuos.size()){
			individuos.add(i);
		} else individuos.set(pos, i);
	}
	
	/** Get do tamanho do vector de individuos **/
	public int getSize(){
		return individuos.size();
	}
	
	/** Obtem o individuo da populacao melhor adaptado **/
	public Individuo getMelhorAdaptado(){
		Individuo melhor = individuos.elementAt(0);
		
		for (int i = 0; i < individuos.size(); i++) {
            if (melhor.getAdaptacao() < individuos.elementAt(i).getAdaptacao()) {
                melhor = individuos.elementAt(i);
            }
        }
		
		if(melhor.getAdaptacao() == 0){
			Random r = new Random();
			int rand = r.nextInt(individuos.size());
			return individuos.elementAt(rand);
		}
		
		return melhor;
	}

	public void imprimePopulacao() {
		for(int i = 0; i < individuos.size(); i++)
			individuos.elementAt(i).imprimeGenes();
	}
}
