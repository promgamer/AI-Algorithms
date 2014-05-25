package simulated.annealing;

import logic.Clinica;

public class SimulatedAnnealing {
	private double temperaturaAtual = 0, temperaturaFinal = 0;
	private double taxaArrefecimento = 0;
	private Gerador gerador = null;
	
    
	public SimulatedAnnealing(double temperaturaInicial, double taxaArrefecimento, double temperaturaFinal, Gerador gerador){
		this.temperaturaAtual = temperaturaInicial;
		this.taxaArrefecimento = taxaArrefecimento;
		this.temperaturaFinal = temperaturaFinal;
		this.gerador = gerador;
	}
	
	private double probabilidadeEscolha(int energia, int energiaProxSolucao, double temperatura) {
        if (energiaProxSolucao < energia) { //a solução nova é melhor
            return 1.0;
        }
        // nova solução é má -> calcular probabilidade de aceitação da solução
        return Math.exp((energia - energiaProxSolucao) / temperatura);
    }
	
	public void run(){
		do {
			
		} while( temperaturaAtual > temperaturaFinal);
	}
	
}
