package graphicInterface;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;

@SuppressWarnings("serial")
public class GeneticOptions extends JPanel {

	/**
	 * Create the panel.
	 */
	public GeneticOptions() {
		//setLayout(null);
		
		JLabel lblTamanhoPopulacao = new JLabel("Tamanho População");
		lblTamanhoPopulacao.setBounds(10, 11, 109, 32);
		add(lblTamanhoPopulacao);
		
		JLabel lblTamanhoGenes = new JLabel("Tamanho Genes");
		lblTamanhoGenes.setBounds(10, 54, 95, 14);
		add(lblTamanhoGenes);
		
		JSpinner spinnerPopulacao = new JSpinner();
		spinnerPopulacao.setBounds(129, 17, 29, 20);
		add(spinnerPopulacao);
		
		JSpinner spinnerGenes = new JSpinner();
		spinnerGenes.setBounds(129, 51, 29, 20);
		add(spinnerGenes);

	}
}
