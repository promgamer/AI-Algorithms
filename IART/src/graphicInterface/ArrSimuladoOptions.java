package graphicInterface;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

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
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class ArrSimuladoOptions extends JPanel {

	private static final int DEFAULT_AMBULANCE_CAP = 10;
	private static final double DEFAULT_TEMP_INICIAL = 10000;
	private static final double DEFAULT_TEMP_FINAL = 0.001;
	private static final int DEFAULT_COMBUSTIVEL = 10;
	private static final double DEFAULT_TAXA_ARREFECIMENTO = 0.001;
	private Clinica clinica;
	/**
	 * Create the panel.
	 */
	public ArrSimuladoOptions(Clinica c) {
		
		clinica = c;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {97, 41, 0, 85};
		gridBagLayout.rowHeights = new int[] {41, 41, 41, 41, 41, 41, 0, 0, 0, 41};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0};
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
		
		JLabel labelTemperatura = new JLabel("Temperatura");
		labelTemperatura.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelTemperatura.setBounds(21, 52, 58, 14);
		GridBagConstraints gbc_labelTemperatura = new GridBagConstraints();
		gbc_labelTemperatura.fill = GridBagConstraints.VERTICAL;
		gbc_labelTemperatura.gridwidth = 4;
		gbc_labelTemperatura.insets = new Insets(0, 0, 5, 5);
		gbc_labelTemperatura.gridx = 0;
		gbc_labelTemperatura.gridy = 3;
		add(labelTemperatura, gbc_labelTemperatura);
		
		JLabel lblTempInicial = new JLabel("Inicial");
		lblTempInicial.setBounds(109, 5, 77, 14);
		GridBagConstraints gbc_lblTempInicial = new GridBagConstraints();
		gbc_lblTempInicial.gridwidth = 2;
		gbc_lblTempInicial.fill = GridBagConstraints.BOTH;
		gbc_lblTempInicial.insets = new Insets(0, 0, 5, 5);
		gbc_lblTempInicial.gridx = 0;
		gbc_lblTempInicial.gridy = 4;
		add(lblTempInicial, gbc_lblTempInicial);
		
		final JTextField spinnerTempInicial = new JTextField( new String (new Double(DEFAULT_TEMP_INICIAL).toString() ) );
		spinnerTempInicial.setBounds(145, 49, 29, 20);
		GridBagConstraints gbc_spinnerTempInicial = new GridBagConstraints();
		gbc_spinnerTempInicial.gridwidth = 2;
		gbc_spinnerTempInicial.fill = GridBagConstraints.BOTH;
		gbc_spinnerTempInicial.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTempInicial.gridx = 2;
		gbc_spinnerTempInicial.gridy = 4;
		add(spinnerTempInicial, gbc_spinnerTempInicial);
		
		JLabel lblTempFinal = new JLabel("Final");
		lblTempFinal.setBounds(8, 5, 96, 14);
		GridBagConstraints gbc_lblTempFinal = new GridBagConstraints();
		gbc_lblTempFinal.gridwidth = 2;
		gbc_lblTempFinal.fill = GridBagConstraints.BOTH;
		gbc_lblTempFinal.insets = new Insets(0, 0, 5, 5);
		gbc_lblTempFinal.gridx = 0;
		gbc_lblTempFinal.gridy = 5;
		add(lblTempFinal, gbc_lblTempFinal);
		
		final JTextField spinnerTempFinal = new JTextField( new String (new Double(DEFAULT_TEMP_FINAL).toString() ));
		spinnerTempFinal.setBounds(72, 74, 29, 20);
		GridBagConstraints gbc_spinnerTempFinal = new GridBagConstraints();
		gbc_spinnerTempFinal.gridwidth = 2;
		gbc_spinnerTempFinal.fill = GridBagConstraints.BOTH;
		gbc_spinnerTempFinal.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTempFinal.gridx = 2;
		gbc_spinnerTempFinal.gridy = 5;
		add(spinnerTempFinal, gbc_spinnerTempFinal);
		
		JLabel lblTaxaArrefecimento = new JLabel("Taxa Arrefecimento");
		lblTaxaArrefecimento.setBounds(106, 77, 45, 14);
		GridBagConstraints gbc_lblTaxaArrefecimento = new GridBagConstraints();
		gbc_lblTaxaArrefecimento.fill = GridBagConstraints.BOTH;
		gbc_lblTaxaArrefecimento.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaxaArrefecimento.gridx = 0;
		gbc_lblTaxaArrefecimento.gridy = 6;
		add(lblTaxaArrefecimento, gbc_lblTaxaArrefecimento);
		
		final JTextField spinnerTaxaArrefecimento = new JTextField( new String (new Double(DEFAULT_TAXA_ARREFECIMENTO).toString() ));
		spinnerTaxaArrefecimento.setBounds(156, 74, 29, 20);
		GridBagConstraints gbc_spinnerTaxaArrefecimento = new GridBagConstraints();
		gbc_spinnerTaxaArrefecimento.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTaxaArrefecimento.gridwidth = 2;
		gbc_spinnerTaxaArrefecimento.fill = GridBagConstraints.BOTH;
		gbc_spinnerTaxaArrefecimento.gridx = 2;
		gbc_spinnerTaxaArrefecimento.gridy = 6;
		add(spinnerTaxaArrefecimento, gbc_spinnerTaxaArrefecimento);
		
		JLabel lblGeraoDeControlo = new JLabel("Gera\u00E7\u00E3o de Controlo");
		GridBagConstraints gbc_lblGeraoDeControlo = new GridBagConstraints();
		gbc_lblGeraoDeControlo.insets = new Insets(0, 0, 5, 5);
		gbc_lblGeraoDeControlo.gridx = 0;
		gbc_lblGeraoDeControlo.gridy = 7;
		add(lblGeraoDeControlo, gbc_lblGeraoDeControlo);
		
		final JToggleButton tglbtnGeracaoControlo = new JToggleButton("Controlo");
		GridBagConstraints gbc_tglbtnGeracaoControlo = new GridBagConstraints();
		gbc_tglbtnGeracaoControlo.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtnGeracaoControlo.gridx = 2;
		gbc_tglbtnGeracaoControlo.gridy = 7;
		add(tglbtnGeracaoControlo, gbc_tglbtnGeracaoControlo);
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
				double tempInicial = new Double(spinnerTempInicial.getText());
				double tempFinal = new Double(spinnerTempFinal.getText());
				double taxaArrefecimento = new Double(spinnerTaxaArrefecimento.getText());
				boolean controlo = tglbtnGeracaoControlo.isSelected();
				
				clinica.startSimulated(capacidadeAmbulancia, combustivel, tempInicial, tempFinal, taxaArrefecimento, controlo);
			}
		});
		
		add(btnStartGenetico, gbc_btnStartGenetico);

	}
}
