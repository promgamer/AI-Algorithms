package logic;

import geneticAlgorithm.Ambiente;
import geneticAlgorithm.EvoluiPopulacao;
import geneticAlgorithm.Individuo;
import geneticAlgorithm.Populacao;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;

public class Clinica extends JApplet {

	private static final long serialVersionUID = 8528919979874509607L;

	Vector<Sucursal> sucursais;
	public static ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = null;
	
	private static final Dimension DEFAULT_SIZE = new Dimension(1024, 728);

	private JGraphXAdapter<Edificio, Estrada> jgxAdapter;


	public static void main(String [] args) throws IOException
	{
		Clinica clinica = new Clinica();
		clinica.init(args[0]);


		/* Prepara a janela */
		JFrame frame = new JFrame();
		frame.getContentPane().add(clinica);
		frame.setTitle("B4. Optimização do Transporte de Pacientes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		/* Calculos do algoritmo genetico */
		int contador = 1;
		
		Populacao pop = new Populacao(4, 8, cidade.vertexSet().size());;
		
		/*
		while( contador != 2){
			pop = EvoluiPopulacao.evoluiPopulacao(pop);
			
			
			System.out.println("Geracao " + contador + ": " + pop.getMelhorAdaptado().toString());
			contador++;
		}*/
		
		for(int i = 0; i < pop.getSize(); i++)
			pop.getIndividuo(i).imprimeGenes();
		
		pop.getMelhorAdaptado().imprimeGenes();
		//pop.getMelhorAdaptado().toString();
	
	}


	public void init(String filepath) throws IOException {
		cidade = parseGrafoCidade(filepath);

		Ambiente.setCapacidadeAmbulancia(15);
		
		// create a visualization using JGraph, via an adapter
		jgxAdapter.setEdgeLabelsMovable(false);
		jgxAdapter.setAllowDanglingEdges(false);
		getContentPane().add(new mxGraphComponent(jgxAdapter));
		resize(DEFAULT_SIZE);

		// positioning via jgraphx layouts
		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());

	}

	public ListenableUndirectedWeightedGraph<Edificio, Estrada> parseGrafoCidade(String filepath) throws IOException{
		ListenableUndirectedWeightedGraph<Edificio, Estrada> cidade = 
				new ListenableUndirectedWeightedGraph<Edificio, Estrada>(Estrada.class);
		jgxAdapter = new JGraphXAdapter<Edificio, Estrada>(cidade);

		Vector<Edificio> edificios = new Vector<Edificio>();
		Vector<Map<Integer,Integer>> destinos = new Vector<Map<Integer,Integer>>();

		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line = null;
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
		reader.close();

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
		return cidade;
	}
}



