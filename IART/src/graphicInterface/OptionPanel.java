package graphicInterface;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel {
	
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel optionPanel;
	private JRadioButton rdbtnGenetico;
	private JRadioButton rdbtnSimulado;
	private boolean genetico = false;
	private boolean arrefecimentoSimulado = false;
	private Clinica clinica;

	/**
	 * Create the panel.
	 * @param clinica 
	 */
	public OptionPanel(Clinica c) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		
		clinica = c;
		
		rdbtnGenetico = new JRadioButton("Algoritmo Genético");
		rdbtnGenetico.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if( genetico == false){
					//Remove o que lá estava
					if( optionPanel.getComponentCount() != 0){
						optionPanel.removeAll();
					}
					
					//Adiciona o painel desejado
					GeneticOptions go = new GeneticOptions(clinica);
					optionPanel.add(go);
					
					// troca os booleanos
					genetico = true;
					arrefecimentoSimulado = false;
					
					// faz update
					clinica.frame.pack();
					clinica.frame.repaint();
				}
			}
		});
		
		topPanel.add(rdbtnGenetico);
		buttonGroup.add(rdbtnGenetico);
		
		rdbtnSimulado = new JRadioButton("Arrefecimento Simulado");
		rdbtnSimulado.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if( arrefecimentoSimulado == false){
					//Remove o que lá estava
					if( optionPanel.getComponentCount() != 0){
						optionPanel.removeAll();
					}
					
					//Adiciona o painel desejado
					ArrSimuladoOptions go = new ArrSimuladoOptions(clinica);
					optionPanel.add(go);
					
					// troca os booleanos
					arrefecimentoSimulado = true;
					genetico = false;
					
					//faz update
					// faz update
					clinica.frame.pack();
					clinica.frame.repaint();
				}
			}
		});
		topPanel.add(rdbtnSimulado);
		buttonGroup.add(rdbtnSimulado);
		
		optionPanel = new JPanel();
		add(optionPanel, BorderLayout.CENTER);
		optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}

}
