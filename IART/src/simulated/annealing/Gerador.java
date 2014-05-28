package simulated.annealing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import logic.Ambulancia;
import logic.Bomba;
import logic.Clinica;
import logic.Edificio;
import logic.Estrada;
import logic.Habitacao;
import logic.Sucursal;

public class Gerador {

	private Ambulancia ambulanciaInicial = null;
	private Ambulancia ambulancia = null;
	private int nrPacientesInciais = 0;
	private int nrPacientesRestantes = 0;
	private ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;

	public Gerador(Ambulancia ambulancia, int nrPacientes, ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade){
		this.ambulanciaInicial  = ambulancia.clone();
		this.ambulancia = ambulancia.clone();
		this.nrPacientesInciais = this.nrPacientesRestantes = nrPacientes;
		this.cidade = cidade;
	}

	public Rota geraRota() {

		Rota rota = new Rota();
		Edificio atual = null;

		System.out.println("................................");
		//Escolher edificio inicial -> tem de ser uma sucursal(apenas sucursais têm garagem e a ambulancia)
		do{
			ArrayList<Edificio> edificios = new ArrayList<Edificio>( cidade.vertexSet() );
			int rng = (int)(Math.random() * edificios.size());
			atual = edificios.get( rng );
			System.out.println("DEBUG : "+"Selecao inicial - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
		}while(!(atual instanceof Sucursal));
		System.out.println("................................");
		
		/* ---- Fazer a rota até todos os pacientes estarem em sucursais ---- */
		
		// boleanos para controlar o combustivel e pacientes "deixados para tras"
		boolean prioridadeSucursal = false, prioridadeBomba = false;
		
		do{
			System.out.println("============PROCESSAMENTO=============");
			ArrayList<Estrada> estradas = new ArrayList<Estrada>( cidade.edgesOf(atual) );

			//obter o maximo de distancia possivel percorrer nos proximos 2 nós -> previsão de combustivel
			double max = getMaxDistancia(atual);
			if( ambulancia.combustivel_restante() <= max)
				prioridadeBomba = true;

			if(atual instanceof Habitacao){
				if(!ambulancia.ocupar(atual.getOcupantes())){
					int esp = ambulancia.getEspacoDisponivel();
					System.out.println("DEBUG : "+"Avaliou Habitacao_if (antes retirar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
					atual.retirarOcupantes(esp);
					System.out.println("DEBUG : "+"Avaliou Habitacao_if (apos retirar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
					ambulancia.ocupar(esp);
					prioridadeSucursal = true;
				} else{
					System.out.println("DEBUG : "+"Avaliou Habitacao_else (antes retirar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
					atual.esvaziaEdificio();
					System.out.println("DEBUG : "+"Avaliou Habitacao_else (apos retirar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
					if(ambulancia.getEspacoDisponivel() == 0)
						prioridadeSucursal = true;
				}
			} else if (atual instanceof Sucursal){
				int nPacientes = 0;
				/* parece desnecessário mas é essencial o controlo
				  devido à sucursal poder não ter espaço suficiente */
				if((nPacientes=atual.getEspacoDisponivel()) < ambulancia.getOcupantes()){
					System.out.println("DEBUG : "+"Avaliou Sucursal_if (antes adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
					ambulancia.retirar(nPacientes);
					((Sucursal) atual).adicionaOcupantes(nPacientes);
					System.out.println("DEBUG : "+"Avaliou Sucursal_if (apos adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
				}
				else {
					ambulancia.retirar(nPacientes);
					System.out.println("DEBUG : "+"Avaliou Sucursal_else (antes adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
					((Sucursal) atual).adicionaOcupantes(nPacientes);
					System.out.println("DEBUG : "+"Avaliou Sucursal_else (apos adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
				}
				nrPacientesRestantes -= nPacientes;
			} else if(atual instanceof Bomba){
				System.out.println("DEBUG : "+"Avaliou Bomba - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
				ambulancia.abastecer();
				prioridadeBomba = false;
			}
			
			System.out.println("DEBUG : "+"Adicao Edificio à Rota (antes adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
			rota.adicionarEdificio(new Edificio(atual)); // adicionar o edificio atual à rota antes de progredir
			System.out.println("DEBUG : "+"Adicao Edificio à Rota (depois adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
			System.out.println("DEBUG : "+"Rota Atual - "+rota.getRota());
			
			System.out.println("==========================================");
			System.out.println(" --------------  GERAÇÃO  --------------- ");
			boolean gotIt = false;
			if( prioridadeBomba ){ // escolhe a 1a Bomba disponivel
				for(int i = 0; i < estradas.size(); i++){
					Edificio edf = cidade.getEdgeTarget(estradas.get(i));
					if(edf instanceof Bomba){
						System.out.println("DEBUG : "+"Prioridade Bomba (antes adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
						atual = edf;
						System.out.println("DEBUG : "+"Prioridade Bomba (depois adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
						rota.addDistancia(cidade.getEdgeWeight(estradas.get(i)));
						gotIt = true;
						prioridadeBomba = false;
						break;
					}
				}
			}
			else if( prioridadeSucursal ){// escolhe a 1a Sucursal com espaço disponivel
				for(int i = 0; i < estradas.size(); i++){
					Edificio edf = cidade.getEdgeTarget(estradas.get(i));
					if(edf instanceof Sucursal){
						if(edf.getEspacoDisponivel() == 0)
							continue;
						System.out.println("DEBUG : "+"Prioridade Sucursal (antes adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
						atual = edf;
						System.out.println("DEBUG : "+"Prioridade Sucursal (depois adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
						rota.addDistancia(cidade.getEdgeWeight(estradas.get(i)));
						gotIt = true;
						prioridadeSucursal = false;
						break;
					}
				}
			}
			
			if( !gotIt ){
				Edificio temp = null;
				int rng2;
				do{
					rng2 = (int)(Math.random() * estradas.size());
					temp = cidade.getEdgeTarget(estradas.get(rng2));
					System.out.println("DEBUG : "+"Edificio Random Gen - "+temp.nome+" "+temp.getOcupantes()+" "+temp.getEspacoDisponivel());
				}while(atual == temp );
				System.out.println("DEBUG : "+"Final Gen (antes adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
				atual = temp;
				System.out.println("DEBUG : "+"Final Gen (depois adicionar) - "+atual.nome+" "+atual.getOcupantes()+" "+atual.getEspacoDisponivel());
				rota.addDistancia(cidade.getEdgeWeight(estradas.get(rng2)));
			}
			System.out.println("---------------------------------------");
		}while(nrPacientesRestantes > 0);

		reset();
		return rota;
	}

	private double getMaxDistancia(Edificio atual) {
		double max = 0;

		for( Estrada e : cidade.edgesOf(atual) ){
			double t = cidade.getEdgeWeight(e);
			Edificio edf = cidade.getEdgeTarget(e);

			double t2 = 0;
			for(Estrada e2 : cidade.edgesOf(edf)){
				double t3 = cidade.getEdgeWeight(e2);
				if( t3 > t2 ) t2 = t3;
			}
			
			if( t + t2 > max) max = t + t2;
		}
		
		return max;
	}

	private void reset() {
		this.ambulancia = ambulanciaInicial.clone();
		nrPacientesRestantes = nrPacientesInciais;
	}

	public static void main(String[] args) throws IOException{
		Clinica cli = new Clinica();
		ListenableUndirectedWeightedGraph<Edificio, Estrada> city = cli.parseGrafoCidade("grafoCidade.txt");
		Gerador g = new Gerador(new Ambulancia(8), 8, city);
		Rota r = g.geraRota();
		r.print();

	}
}
