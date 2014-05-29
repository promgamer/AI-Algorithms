package graphicInterface;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel {
	
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel optionPanel;
	private JRadioButton rdbtnGenetico;
	private JRadioButton rdbtnSimulado;

	/**
	 * Create the panel.
	 */
	public OptionPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		
		rdbtnGenetico = new JRadioButton("Algoritmo Genético");
		rdbtnGenetico.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				GeneticOptions go = new GeneticOptions();
				optionPanel.add(go);
			}
		});
		
		topPanel.add(rdbtnGenetico);
		buttonGroup.add(rdbtnGenetico);
		
		rdbtnSimulado = new JRadioButton("Arrefecimento Simulado");
		topPanel.add(rdbtnSimulado);
		buttonGroup.add(rdbtnSimulado);
		
		optionPanel = new JPanel();
		add(optionPanel, BorderLayout.CENTER);

	}

}
