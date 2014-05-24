package simulated.annealing;

import logic.Clinica;

public class SimulatedAnnealing {
	private double temperaturaAtual = 0, temperaturaFinal = 0;
	private double taxaArrefecimento = 0;
	private Clinica clinica = null;
	
    
	public SimulatedAnnealing(double temperaturaInicial, double taxaArrefecimento, double temperaturaFinal, Clinica clinica){
		this.temperaturaAtual = temperaturaInicial;
		this.taxaArrefecimento = taxaArrefecimento;
		this.temperaturaFinal = temperaturaFinal;
		this.clinica = clinica;
	}
	
	private double probabilidadeEscolha(int energia, int energiaProxSolucao, double temperatura) {
        if (energiaProxSolucao < energia) { //a solu��o nova � melhor
            return 1.0;
        }
        // nova solu��o � m� -> calcular probabilidade de aceita��o da solu��o
        return Math.exp((energia - energiaProxSolucao) / temperatura);
    }
	
	public void run(){
		do {
			
		} while( temperaturaAtual > temperaturaFinal);
	}
	
}
