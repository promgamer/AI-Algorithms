package graphicInterface;

import geneticAlgorithm.Ambiente;
import geneticAlgorithm.EvoluiPopulacao;
import geneticAlgorithm.Individuo;
import geneticAlgorithm.Populacao;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.jgrapht.Graphs;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import simulated.annealing.Gerador;
import simulated.annealing.SimulatedAnnealing;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import logic.Ambulancia;
import logic.Bomba;
import logic.Edificio;
import logic.Estrada;
import logic.Habitacao;
import logic.Sucursal;

public class Clinica extends JApplet {
	public Clinica() {
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		
		OptionPanel op = new OptionPanel(this);
		panel.add(op);
	}

	private static final long serialVersionUID = 8528919979874509607L;

	Vector<Sucursal> sucursais;
	public static ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;
	
	private static final Dimension DEFAULT_SIZE = new Dimension(1024, 728);

	private JGraphXAdapter<Edificio, Estrada> jgxAdapter;
	public JFrame frame;


	public static void main(String [] args) throws IOException
	{
		Clinica clinica = new Clinica();
		clinica.init(args[0]);


		/* Prepara a janela */
		clinica.frame = new JFrame();
		clinica.frame.getContentPane().add(clinica);
		clinica.frame.setTitle("B4. Optimização do Transporte de Pacientes");
		clinica.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clinica.frame.pack();
		clinica.frame.setVisible(true);
	
	}


	public void init(String filepath) throws IOException {
		cidade = parseGrafoCidade(filepath);
		
		// Atribui os caminhos
		Ambiente.setGraphPath(filepath);
		Gerador.setGraphPath(filepath);
		
		jgxAdapter = new JGraphXAdapter<Edificio, Estrada>(cidade);
		// create a visualization using JGraph, via an adapter
		jgxAdapter.setEdgeLabelsMovable(false);
		jgxAdapter.setAllowDanglingEdges(false);
		getContentPane().add(new mxGraphComponent(jgxAdapter));
		resize(DEFAULT_SIZE);

		// positioning via jgraphx layouts
		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());

	}

	public static ListenableUndirectedWeightedGraph<Edificio, Estrada> parseGrafoCidade(String filepath){
		ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = new ListenableUndirectedWeightedGraph<Edificio, Estrada>(Estrada.class);
		

		Vector<Edificio> edificios = new Vector<Edificio>();
		Vector<Map<Integer,Integer>> destinos = new Vector<Map<Integer,Integer>>();
		
		Edificio.lastID = 1; //stupid hardedcoded line

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String [] split = line.split(" # ");
				String [] infoEdificio = split[0].trim().split(" - "); // Nome - Tipo - Capacidade
				Edificio novoEdf = null;
				if(infoEdificio[1].trim().equals("S")){
					novoEdf = new Sucursal(infoEdificio[0].trim(), Integer.parseInt(infoEdificio[2].trim()));
				}else if(infoEdificio[1].trim().equals("B")){
					novoEdf = new Bomba(infoEdificio[0].trim());
				}else if(infoEdificio[1].trim().equals("H")){
					novoEdf = new Habitacao(infoEdificio[0].trim(), Integer.parseInt(infoEdificio[2].trim()));
				}else{
					System.out.println("Error on file parsing - Invalid Syntax!");
					reader.close();
					return null;
				}
				edificios.add(novoEdf);

				String [] infoDestinos = split[1].trim().split(" - "); // idEdifico,Distancia - ....
				Map<Integer, Integer> destinosAux = new HashMap<Integer,Integer>();
				for(String info : infoDestinos){
					String[] specific = info.trim().split(",");
					destinosAux.put(Integer.parseInt(specific[0].trim()), Integer.parseInt(specific[1].trim()));
				}
				destinos.add(destinosAux);
			}
		} catch (NumberFormatException | IOException e1) {
			e1.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Build Graph
		for(Edificio edf : edificios){
			cidade.addVertex(edf);
		}

		int i = 0;
		for(Map<Integer,Integer> dsts : destinos){ // para cada edificio	
			for(Map.Entry<Integer, Integer> entry : dsts.entrySet()){
				Edificio edf1 = edificios.elementAt(i), edf2 = edificios.elementAt(entry.getKey()-1);
				Estrada e = cidade.addEdge(edf1, edf2); // edificio - edificio
				cidade.setEdgeWeight(e, entry.getValue()); // peso
			}
			i++;
		}

		return cidade;
	}
	
	public static ListenableUndirectedWeightedGraph<Edificio, Estrada> getCidade(){//problema do caralho
		ListenableUndirectedWeightedGraph<Edificio, Estrada> c = new ListenableUndirectedWeightedGraph<Edificio, Estrada>(Estrada.class);
		
		Graphs.addGraph(c, cidade);
		
		return c;
	}


	public void startGenetic(int capacidadeAmbulancia, int combustivel, int tamanhoPop, int tamanhoGenes, int geracoes) {
		/* Calculos do algoritmo genetico */
		int contador = 1;
		
		Ambulancia.setMaxCombustivel(combustivel);
		Ambiente.setCapacidadeAmbulancia((capacidadeAmbulancia));

		long startTime = System.nanoTime();
		Populacao pop = new Populacao(tamanhoPop, tamanhoGenes, cidade.vertexSet().size());;
		
		while( contador != geracoes){
			pop = EvoluiPopulacao.evoluiPopulacao(pop);
			
			System.out.print("Geracao " + contador + ": " + pop.getMelhorAdaptado().toString() + " : ");
			pop.getMelhorAdaptado().imprimeGenes();
			contador++;
		}
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/Math.pow(10, 9);
		
		// Obtem o melhor individuo
		Individuo melhor = pop.getMelhorAdaptado();
		Ambiente e = new Ambiente(melhor.getGenes(), true);
		e.calculaAdaptacao();
		e.imprimeMelhor();
		System.out.println("\nTempo de Execução: "+duration+" segundos.");
	}


	public void startSimulated(int capacidadeAmbulancia, int combustivel, double tempInicial, double tempFinal, double taxaArrefecimento, boolean controlo) {
		
		Ambulancia.setMaxCombustivel(combustivel);
		
		Gerador g = null;
		try {
			g = new Gerador(new Ambulancia(capacidadeAmbulancia));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SimulatedAnnealing sm = new SimulatedAnnealing(tempInicial, tempFinal, taxaArrefecimento, g);
		
		long startTime = System.nanoTime();
		try {
			sm.run(controlo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/Math.pow(10, 9);
		System.out.println("\nTempo de Execução: "+duration+" segundos.");
		
	}
}



