package geneticAlgorithm;

import java.util.Random;
import java.util.Vector;

public class EvoluiPopulacao {
	
	private static final double escolhaCrossover = 0.5;
    private static final double probabilidadeMutacao = 0.15;
    private static final int tamanhoPopRandom = 5;
    private static boolean elitismo;
    
    
    /** 
     * Promove a evolucao da populacao
     * 
     * Recebe a populacao atual e devolve a nova populacao resultante
     */
    public static Populacao evoluiPopulacao(Populacao populacaoAntiga){
    	Populacao novaPopulacao = new Populacao(populacaoAntiga.getSize());
    	
    	int i = 1;
    	
    	// Politica elitista: manter sempre o melhor
    	if(elitismo == true){
    		Individuo ind = populacaoAntiga.getMelhorAdaptado();
    		
    		if( ind.getAdaptacao() > 0)
    			novaPopulacao.setIndividuo(0, ind);
    		else i = 0;
    		
    	} else i = 0; // nao guarda o melhor
    	
    	/** Crossover Aleatorio **/
    	for( ; i < populacaoAntiga.getSize(); i++){
    		Individuo indiv1 = selecionaIndividuo(populacaoAntiga);
    		Individuo indiv2 = selecionaIndividuo(populacaoAntiga);
    		
    		Individuo novo = crossover(indiv1, indiv2);
    		novaPopulacao.setIndividuo(i, novo);
    	}
    	
    	if(elitismo == true)
    		i = 1;
    	else i = 0;
    	
    	/** Faz as mutacoes aleatorias ; o individuo "perfeito" nao sobre mutacao **/
    	for( ; i < novaPopulacao.getSize(); i++)
    		mutacao(novaPopulacao.getIndividuo(i));
    	
    
    	return novaPopulacao;
    }
    
    /** Escolhe um individuo para o crossover 
     * 
     * Utilizando o método de Torneio
     * 
     * **/
    private static Individuo selecionaIndividuo(Populacao pop) {
        
        Populacao temp = new Populacao(tamanhoPopRandom);
        
        for (int i = 0; i < tamanhoPopRandom; i++) {
        	Random r = new Random();
        	int random = r.nextInt(pop.getSize());
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
    
    public static void setElitismo(boolean e){
    	elitismo = e;
    }

}
