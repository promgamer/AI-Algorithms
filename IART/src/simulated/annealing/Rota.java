package simulated.annealing;

import java.util.Vector;

import logic.Edificio;
import logic.Habitacao;
import logic.Sucursal;

public class Rota {
	private Vector<Edificio> rota = null;
	public double distanciaTotal = 0;
	
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

	public double getDistanciaTotal() {
		return distanciaTotal;
	}
	
	public void addDistancia(double d){
		distanciaTotal += d;
	}

	public void print() {
		for(int i=0; i<rota.size(); i++){
			System.out.println(rota.elementAt(i).ID + " - " + rota.elementAt(i).nome);
		}
		System.out.println("Distancia Total: "+distanciaTotal);
	}
	
}
