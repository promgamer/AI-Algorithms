package logic;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;

public class Clinica extends JApplet {
	
	private static final long serialVersionUID = 8528919979874509607L;
	
	Vector<Sucursal> sucursais;
	
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 728);

    private JGraphXAdapter<Edificio, DefaultEdge> jgxAdapter;

	
	public static void main(String [] args)
    {
		Clinica clinica = new Clinica();
		clinica.init();
		
		
		/* Prepara a janela */
        JFrame frame = new JFrame();
        frame.getContentPane().add(clinica);
        frame.setTitle("B4. Optimização do Transporte de Pacientes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
	
	
	public void init()
    {
        // create a JGraphT graph
        ListenableGraph<Edificio, DefaultEdge> cidade = new ListenableDirectedGraph<Edificio, DefaultEdge>(DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<Edificio, DefaultEdge>(cidade);

        getContentPane().add(new mxGraphComponent(jgxAdapter));
        resize(DEFAULT_SIZE);

        Sucursal v1 = new Sucursal("1",10);
        Sucursal v2 = new Sucursal("2",10);
        Sucursal v3 = new Sucursal("3",10);
        Bomba v4 = new Bomba("4");
        
        cidade.addVertex(v1);
        cidade.addVertex(v2);
        cidade.addVertex(v3);
        cidade.addVertex(v4);

        cidade.addEdge(v1, v2);
        cidade.addEdge(v2, v3);
        cidade.addEdge(v3, v1);
        cidade.addEdge(v4, v3);

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        // that's all there is to it!...
    }
	
	@SuppressWarnings("unchecked")
	public ListenableUndirectedWeightedGraph<Edificio, DefaultEdge> parseGrafoCidade(String filepath) throws IOException{
		ListenableUndirectedWeightedGraph<Edificio, DefaultEdge> cidade = 
				new ListenableUndirectedWeightedGraph<Edificio, DefaultEdge>(DefaultEdge.class);
		
		Map<Edificio, Map<Integer,Integer>> graphPrep = new HashMap<Edificio, Map<Integer,Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    String [] split = line.split("#");
		    String [] infoEdificio = split[0].trim().split("-"); // Nome - Tipo - Capacidade
		    Edificio novoEdf = null;
		    if(infoEdificio[1].trim().equals("S")){
		    	novoEdf = new Sucursal(infoEdificio[0].trim(), Integer.parseInt(infoEdificio[2].trim()));
		    }else if(infoEdificio[1].trim().equals("B")){
		    	novoEdf = new Bomba(infoEdificio[0].trim());
		    }else if(infoEdificio[1].trim().equals("H")){
		    	novoEdf = new Habitacao(infoEdificio[0].trim(), Integer.parseInt(infoEdificio[2].trim()));
		    }else{
		    	reader.close();
		    	return null;
		    }
		    
		    String [] infoDestinos = split[1].trim().split("-"); // idEdifico,Distancia - ....
		    Map<Integer, Integer> destinos = new HashMap<Integer,Integer>();
		    for(String info : infoDestinos){
		    	String[] specific = info.trim().split(",");
		    	destinos.put(Integer.parseInt(specific[0].trim()), Integer.parseInt(specific[1].trim()));
		    }
		    graphPrep.put(novoEdf, destinos);
		}
		reader.close();
		
		//Build Graph
		for(Map.Entry<Edificio, Map<Integer,Integer>> entry : graphPrep.entrySet()){
			cidade.addVertex(entry.getKey());
		}
		
		Map.Entry<Edificio, Map<Integer,Integer>>[] listaEdfs = (Entry<Edificio, Map<Integer, Integer>>[]) graphPrep.entrySet().toArray();
		for(Map.Entry<Edificio, Map<Integer,Integer>> entry : graphPrep.entrySet()){ // para cada edificio
			for(Map.Entry<Integer,Integer> entry2 : entry.getValue().entrySet()){ // adicionar ligação
				DefaultEdge e = cidade.addEdge(entry.getKey(), listaEdfs[entry2.getKey()].getKey()); // edificio - edificio
				cidade.setEdgeWeight(e, entry2.getValue()); // peso
			}
		}
		
		return cidade;
	}
}



