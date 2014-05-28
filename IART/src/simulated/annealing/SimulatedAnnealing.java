package simulated.annealing;

import java.io.IOException;

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
	
	private double probabilidadeEscolha(double energiaSolucaoAtual, double energiaNovaSolucao, double temperatura) {
        if (energiaNovaSolucao < energiaSolucaoAtual) { //a solução nova é melhor
            return 1.0;
        }
        // nova solução é má -> calcular probabilidade de aceitação da solução
        return Math.exp((energiaSolucaoAtual - energiaNovaSolucao) / temperatura);
    }
	
	public void run() throws IOException{
		Rota solucaoAtual = gerador.geraRota();
		Rota melhorSolucao = new Rota(solucaoAtual);
		
		do {
			Rota novaSolucao = gerador.geraRota();
			
            double energiaSolucaoAtual = solucaoAtual.getDistanciaTotal();
            double energiaNovaSolucao = novaSolucao.getDistanciaTotal();

            if (probabilidadeEscolha(energiaSolucaoAtual, energiaNovaSolucao, temperaturaAtual) > Math.random()) {
                solucaoAtual = new Rota(novaSolucao);
            }

            // Guardar a melhor solução até agr encontrada
            if (solucaoAtual.getDistanciaTotal() < melhorSolucao.getDistanciaTotal()) {
                melhorSolucao = new Rota(solucaoAtual);
            }
            
            temperaturaAtual *= 1-taxaArrefecimento;
            //System.out.println("Temperatura: " + temperaturaAtual);
		} while( temperaturaAtual > temperaturaFinal);
		
		System.out.println("Melhor Solucao:");
		melhorSolucao.print();
		
		System.out.println("\nSolucao Atual:");
		solucaoAtual.print();
	}
	
}
