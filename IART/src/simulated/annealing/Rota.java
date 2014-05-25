package simulated.annealing;

import java.util.Vector;

import logic.Edificio;

public class Rota {
	private Vector<Edificio> rota = new Vector<Edificio>();
	public int distanciaTotal = 0;
	public int pacientesRestantes = 0;
	
	public Rota(){}
	
	public void adicionarEdificio(Edificio edf){
		rota.add(edf);
	}

	public int getDistanciaTotal() {
		return distanciaTotal;
	}

	public int getPacientesRestantes() {
		return pacientesRestantes;
	}
	
	
}
