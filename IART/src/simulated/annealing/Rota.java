package simulated.annealing;

import java.util.Vector;

import logic.Edificio;

public class Rota {
	private Vector<Edificio> rota = null;
	public int distanciaTotal = 0;
	
	public Rota(){
		rota = new Vector<Edificio>();
	}
	
	@SuppressWarnings("unchecked")
	public Rota(Rota r){
		this.rota = (Vector<Edificio>) r.getRota().clone();
		this.distanciaTotal = r.getDistanciaTotal();
	}
	
	public void adicionarEdificio(Edificio edf){
		rota.add(edf);
	}
	
	public Vector<Edificio> getRota(){
		return rota;
	}

	public int getDistanciaTotal() {
		return distanciaTotal;
	}
	
	public void addDistancia(int d){
		distanciaTotal += d;
	}
	
}
