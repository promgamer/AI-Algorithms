package geneticAlgorithm;

import java.util.Random;
import java.util.Vector;

public class EvoluiPopulacao {
	
	private static final double escolhaCrossover = 0.5;
    private static final double probabilidadeMutacao = 0.015;
    private static final int tamanhoPopRandom = 5;
    
    
    /** 
     * Promove a evolucao da populacao
     * 
     * Recebe a populacao atual e devolve a nova populacao resultante
     */
    public static Populacao evoluiPopulacao(Populacao populacaoAntiga){
    	Populacao novaPopulacao = new Populacao(populacaoAntiga.getSize());
    	
    	//TODO: Pensar nisto -> para ja mantemos uma politica elitista
    	// so para o primeiro individuo (tal como o stor faz nos testes :p )
    	// pensar nisto!
    	novaPopulacao.setIndividuo(0, populacaoAntiga.getMelhorAdaptado());
    	
    	/** Crossover Aleatorio **/
    	for(int i = 1; i < populacaoAntiga.getSize(); i++){
    		Individuo indiv1 = selecionaIndividuo(populacaoAntiga);
    		Individuo indiv2 = selecionaIndividuo(populacaoAntiga);
    		
    		Individuo novo = crossover(indiv1, indiv2);
    		novaPopulacao.setIndividuo(i, novo);
    	}
    	
    	/** Faz as mutacoes aleatorias ; o individuo "perfeito" nao sobre mutacao **/
    	for(int i = 1; i < novaPopulacao.getSize(); i++)
    		mutacao(novaPopulacao.getIndividuo(i));
    	
    
    	return novaPopulacao;
    }
    
    /** Escolhe um individuo para o crossover 
     * 
     * De um grupo de elementos, selecciona o elemento mais bem adaptado
     * 
     * TODO: talvez implementar antes roleta??
     * 
     * **/
    private static Individuo selecionaIndividuo(Populacao pop) {
        
        Populacao temp = new Populacao(tamanhoPopRandom);
        
        for (int i = 0; i < tamanhoPopRandom; i++) {
            int random = (int) (Math.random() * pop.getSize());
            temp.setIndividuo(i, pop.getIndividuo(random));
        }
        // Get the fittest
        Individuo melhor = temp.getMelhorAdaptado();
        return melhor;
    }
    
    /** Gera uma mutacao **/
    private static void mutacao(Individuo indiv) {
    	
        for (int i = 0; i < indiv.getSize(); i++) {
            if (Math.random() <= probabilidadeMutacao) {
                indiv.setGene(i, indiv.geraGene());
            }
        }
    }
    
    /** Faz o crossover 
     * 
     * O crossover é calculado gene a gene...
     * 
     * **/
    private static Individuo crossover(Individuo indiv1, Individuo indiv2) {
    	Vector<Integer> genes = new Vector<Integer>();
    	
        for (int i = 0; i < indiv1.getSize(); i++) {
            // Crossover
            if (Math.random() <= escolhaCrossover) {
                genes.add(i, indiv1.getGene(i));
            } else {
            	genes.add(i, indiv2.getGene(i));
            }
        }
        
        Individuo novo = new Individuo(genes);
        return novo;
    }

}
