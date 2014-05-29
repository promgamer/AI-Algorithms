package simulated.annealing;

import java.util.Vector;

import logic.Edificio;

public class Rota {
	private Vector<Edificio> rota = null;
	private Vector<Integer> nrPacientes = null;

	public double distanciaTotal = 0;
	
	public Rota(){
		rota = new Vector<Edificio>();
		nrPacientes = new Vector<Integer>();
	}
	
	@SuppressWarnings("unchecked")
	public Rota(Rota r){
		this.rota = (Vector<Edificio>) r.getRota().clone();
		this.distanciaTotal = r.getDistanciaTotal();
	}
	
	public Rota(Rota r, int n){
		rota = new Vector<Edificio>();
		nrPacientes = new Vector<Integer>();
		Vector<Edificio> tmpE = r.getRota();
		Vector<Integer> tmpP = r.getNrPacientes();
		for(int i=0; i<n; i++){
			rota.add(tmpE.get(i));
			nrPacientes.add(tmpP.get(i));
		}
	}
	
	public void adicionarEdificio(Edificio edf){
		rota.add(edf);
	}
	
	public void adicionarEdificio(Edificio edf, Integer n){
		rota.add(edf);
		nrPacientes.add(n);
	}
	
	public Vector<Edificio> getRota(){
		return rota;
	}

	public Vector<Integer> getNrPacientes() {
		return nrPacientes;
	}

	public double getDistanciaTotal() {
		return distanciaTotal;
	}

	public Edificio getUltimoEdificio(){
		return rota.lastElement();
	}
	
	public int getUltimoNrPacientes(){
		return nrPacientes.lastElement();
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
