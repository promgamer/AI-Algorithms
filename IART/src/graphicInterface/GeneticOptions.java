package graphicInterface;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class GeneticOptions extends JPanel {

	private static final int DEFAULT_AMBULANCE_CAP = 10;
	private static final int DEFAULT_GENES_SIZE = 12;
	private static final int DEFAULT_POPULATION_SIZE = 50;
	private static final int DEFAULT_COMBUSTIVEL = 10;
	private static final int DEFAUlT_GENERATIONS = 100;
	private Clinica clinica;
	/**
	 * Create the panel.
	 */
	public GeneticOptions(Clinica c) {
		
		clinica = c;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {97, 41, 0, 85};
		gridBagLayout.rowHeights = new int[] {41, 41, 41, 41, 41, 41, 0, 0, 0, 41};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblAmbulncia = new JLabel("Ambul\u00E2ncia");
		lblAmbulncia.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAmbulncia.setBounds(98, 27, 66, 14);
		GridBagConstraints gbc_lblAmbulncia = new GridBagConstraints();
		gbc_lblAmbulncia.gridwidth = 4;
		gbc_lblAmbulncia.fill = GridBagConstraints.VERTICAL;
		gbc_lblAmbulncia.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmbulncia.gridx = 0;
		gbc_lblAmbulncia.gridy = 0;
		add(lblAmbulncia, gbc_lblAmbulncia);
		
		JLabel lblCapacidade = new JLabel("Capacidade");
		lblCapacidade.setBounds(84, 52, 56, 14);
		GridBagConstraints gbc_lblCapacidade = new GridBagConstraints();
		gbc_lblCapacidade.fill = GridBagConstraints.BOTH;
		gbc_lblCapacidade.insets = new Insets(0, 0, 5, 5);
		gbc_lblCapacidade.gridx = 0;
		gbc_lblCapacidade.gridy = 1;
		add(lblCapacidade, gbc_lblCapacidade);
		
		final JSpinner spinnerCapacidadeAmbulancia = new JSpinner();
		spinnerCapacidadeAmbulancia.setBounds(30, 24, 29, 20);
		spinnerCapacidadeAmbulancia.setValue(DEFAULT_AMBULANCE_CAP);
		GridBagConstraints gbc_spinnerCapacidadeAmbulancia = new GridBagConstraints();
		gbc_spinnerCapacidadeAmbulancia.gridwidth = 2;
		gbc_spinnerCapacidadeAmbulancia.fill = GridBagConstraints.BOTH;
		gbc_spinnerCapacidadeAmbulancia.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerCapacidadeAmbulancia.gridx = 2;
		gbc_spinnerCapacidadeAmbulancia.gridy = 1;
		add(spinnerCapacidadeAmbulancia, gbc_spinnerCapacidadeAmbulancia);
		
		JLabel lblCombustivelAmbulancia = new JLabel("Combust\u00EDvel");
		lblCombustivelAmbulancia.setBounds(9, 77, 58, 14);
		GridBagConstraints gbc_lblCombustivelAmbulancia = new GridBagConstraints();
		gbc_lblCombustivelAmbulancia.gridwidth = 2;
		gbc_lblCombustivelAmbulancia.fill = GridBagConstraints.BOTH;
		gbc_lblCombustivelAmbulancia.insets = new Insets(0, 0, 5, 5);
		gbc_lblCombustivelAmbulancia.gridx = 0;
		gbc_lblCombustivelAmbulancia.gridy = 2;
		add(lblCombustivelAmbulancia, gbc_lblCombustivelAmbulancia);
		
		final JSpinner spinnerCombustivel = new JSpinner();
		spinnerCombustivel.setBounds(64, 24, 29, 20);
		spinnerCombustivel.setValue(DEFAULT_COMBUSTIVEL);
		GridBagConstraints gbc_spinnerCombustivel = new GridBagConstraints();
		gbc_spinnerCombustivel.gridwidth = 2;
		gbc_spinnerCombustivel.fill = GridBagConstraints.BOTH;
		gbc_spinnerCombustivel.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerCombustivel.gridx = 2;
		gbc_spinnerCombustivel.gridy = 2;
		add(spinnerCombustivel, gbc_spinnerCombustivel);
		
		JLabel labelPopulacao = new JLabel("Popula\u00E7\u00E3o");
		labelPopulacao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelPopulacao.setBounds(21, 52, 58, 14);
		GridBagConstraints gbc_labelPopulacao = new GridBagConstraints();
		gbc_labelPopulacao.fill = GridBagConstraints.VERTICAL;
		gbc_labelPopulacao.gridwidth = 4;
		gbc_labelPopulacao.insets = new Insets(0, 0, 5, 5);
		gbc_labelPopulacao.gridx = 0;
		gbc_labelPopulacao.gridy = 3;
		add(labelPopulacao, gbc_labelPopulacao);
		
		JLabel lblTamanhoGenes = new JLabel("Tamanho Genes");
		lblTamanhoGenes.setBounds(109, 5, 77, 14);
		GridBagConstraints gbc_lblTamanhoGenes = new GridBagConstraints();
		gbc_lblTamanhoGenes.gridwidth = 2;
		gbc_lblTamanhoGenes.fill = GridBagConstraints.BOTH;
		gbc_lblTamanhoGenes.insets = new Insets(0, 0, 5, 5);
		gbc_lblTamanhoGenes.gridx = 0;
		gbc_lblTamanhoGenes.gridy = 4;
		add(lblTamanhoGenes, gbc_lblTamanhoGenes);
		
		final JSpinner spinnerTamanhoGenes = new JSpinner();
		spinnerTamanhoGenes.setBounds(145, 49, 29, 20);
		spinnerTamanhoGenes.setValue(DEFAULT_GENES_SIZE);
		GridBagConstraints gbc_spinnerTamanhoGenes = new GridBagConstraints();
		gbc_spinnerTamanhoGenes.gridwidth = 2;
		gbc_spinnerTamanhoGenes.fill = GridBagConstraints.BOTH;
		gbc_spinnerTamanhoGenes.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTamanhoGenes.gridx = 2;
		gbc_spinnerTamanhoGenes.gridy = 4;
		add(spinnerTamanhoGenes, gbc_spinnerTamanhoGenes);
		
		JLabel lblTamanhoPopulacao = new JLabel("Tamanho População");
		lblTamanhoPopulacao.setBounds(8, 5, 96, 14);
		GridBagConstraints gbc_lblTamanhoPopulacao = new GridBagConstraints();
		gbc_lblTamanhoPopulacao.gridwidth = 2;
		gbc_lblTamanhoPopulacao.fill = GridBagConstraints.BOTH;
		gbc_lblTamanhoPopulacao.insets = new Insets(0, 0, 5, 5);
		gbc_lblTamanhoPopulacao.gridx = 0;
		gbc_lblTamanhoPopulacao.gridy = 5;
		add(lblTamanhoPopulacao, gbc_lblTamanhoPopulacao);
		
		final JSpinner spinnerTamanhoPop = new JSpinner();
		spinnerTamanhoPop.setBounds(72, 74, 29, 20);
		spinnerTamanhoPop.setValue(DEFAULT_POPULATION_SIZE);
		GridBagConstraints gbc_spinnerTamanhoPop = new GridBagConstraints();
		gbc_spinnerTamanhoPop.gridwidth = 2;
		gbc_spinnerTamanhoPop.fill = GridBagConstraints.BOTH;
		gbc_spinnerTamanhoPop.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTamanhoPop.gridx = 2;
		gbc_spinnerTamanhoPop.gridy = 5;
		add(spinnerTamanhoPop, gbc_spinnerTamanhoPop);
		
		JLabel lblGeraes = new JLabel("Gera\u00E7\u00F5es");
		lblGeraes.setBounds(106, 77, 45, 14);
		GridBagConstraints gbc_lblGeraes = new GridBagConstraints();
		gbc_lblGeraes.fill = GridBagConstraints.BOTH;
		gbc_lblGeraes.insets = new Insets(0, 0, 5, 5);
		gbc_lblGeraes.gridx = 0;
		gbc_lblGeraes.gridy = 6;
		add(lblGeraes, gbc_lblGeraes);
		
		final JSpinner spinnerGeracoes = new JSpinner();
		spinnerGeracoes.setBounds(156, 74, 29, 20);
		spinnerGeracoes.setValue(DEFAUlT_GENERATIONS);
		GridBagConstraints gbc_spinnerGeracoes = new GridBagConstraints();
		gbc_spinnerGeracoes.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerGeracoes.gridwidth = 2;
		gbc_spinnerGeracoes.fill = GridBagConstraints.BOTH;
		gbc_spinnerGeracoes.gridx = 2;
		gbc_spinnerGeracoes.gridy = 6;
		add(spinnerGeracoes, gbc_spinnerGeracoes);
		
		final JToggleButton elistismoButton = new JToggleButton("Elitismo");
		GridBagConstraints gbc_elistismoButton = new GridBagConstraints();
		gbc_elistismoButton.insets = new Insets(0, 0, 5, 5);
		gbc_elistismoButton.gridx = 2;
		gbc_elistismoButton.gridy = 7;
		add(elistismoButton, gbc_elistismoButton);
		GridBagConstraints gbc_btnStartGenetico = new GridBagConstraints();
		gbc_btnStartGenetico.gridwidth = 6;
		gbc_btnStartGenetico.gridx = 0;
		gbc_btnStartGenetico.gridy = 8;
		
		JButton btnStartGenetico = new JButton("Correr Algoritmo!");
		btnStartGenetico.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int capacidadeAmbulancia = (int) spinnerCapacidadeAmbulancia.getValue();
				int combustivel = (int) spinnerCombustivel.getValue();
				int tamanhoPop = (int) spinnerTamanhoPop.getValue();
				int tamanhoGenes = (int) spinnerTamanhoGenes.getValue();
				int geracoes = (int) spinnerGeracoes.getValue();
				boolean elitismo = elistismoButton.isSelected();
				
				clinica.startGenetic(capacidadeAmbulancia, combustivel, tamanhoPop, tamanhoGenes, geracoes, elitismo);
			}
		});
		
		
		add(btnStartGenetico, gbc_btnStartGenetico);

	}
}
