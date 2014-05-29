package simulated.annealing;

import java.util.Map.Entry;
import java.util.AbstractMap;
import java.util.Vector;

import logic.Edificio;

public class Rota {
	private Vector<Edificio> rota = null;
	private Vector<Integer> nrPacientes = null;
	private Vector<Double> distancias = null;
	// (prioridadeBomba, prioridadeSucursal)
	private Vector<Entry<Boolean, Boolean>> estado = null;
	// (nrOcupantes, CombustivelRestante)
	private Vector<Entry<Integer, Double>> estadoAmbulancia = null;

	public double distanciaTotal = 0;
	
	public Rota(){
		rota = new Vector<Edificio>();
		distancias = new Vector<Double>();
		nrPacientes = new Vector<Integer>();
		estado = new Vector<Entry<Boolean, Boolean>>();
		estadoAmbulancia = new Vector<Entry<Integer, Double>>();
	}
	
	@SuppressWarnings("unchecked")
	public Rota(Rota r){
		this.rota = (Vector<Edificio>) r.getRota().clone();
		this.distanciaTotal = r.getDistanciaTotal();
		this.distancias = r.getDistancias();
		this.nrPacientes = r.getNrPacientes();
		this.estado = r.getEstados();
		this.estadoAmbulancia = r.getEstadosAmbulancia();
	}

	public Rota(Rota r, int n){
		rota = new Vector<Edificio>();
		nrPacientes = new Vector<Integer>();
		estado = new Vector<Entry<Boolean, Boolean>>();
		distancias = new Vector<Double>();
		estadoAmbulancia = new Vector<Entry<Integer, Double>>();
		Vector<Edificio> tmpE = r.getRota();
		Vector<Integer> tmpP = r.getNrPacientes();
		Vector<Entry<Boolean, Boolean>> tmpS = r.getEstados();
		Vector<Double> tmpD = r.getDistancias();
		Vector<Entry<Integer, Double>> tmpA = r.getEstadosAmbulancia();

		for(int i=0; i<n; i++){
			rota.add(tmpE.get(i));
			nrPacientes.add(tmpP.get(i));
			estado.add(tmpS.get(i));
			distancias.add(tmpD.get(i));
			estadoAmbulancia.add(tmpA.get(i));
		}
		distanciaTotal = distancias.isEmpty()?0:distancias.lastElement();
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
		return nrPacientes.isEmpty()?0:nrPacientes.lastElement();
	}
	
	public Entry<Boolean, Boolean> getUltimoEstado(){
		return estado.isEmpty()?
				new AbstractMap.SimpleEntry<Boolean, Boolean>(false,false)
				:estado.lastElement();
	}
	
	@SuppressWarnings("unchecked")
	public Entry<Integer, Double> getUltimoEstadoAmbulancia() {
		return estadoAmbulancia.isEmpty()?
				new AbstractMap.SimpleEntry<Integer, Double>(0,0.0)
				:estadoAmbulancia.lastElement();
	}
	
	public Vector<Double> getDistancias(){
		return distancias;
	}
	
	public Vector<Entry<Integer, Double>> getEstadosAmbulancia() {
		return estadoAmbulancia;
	}

	public Vector<Entry<Boolean, Boolean>> getEstados() {
		return estado;
	}

	public void addEstado(Entry<Boolean, Boolean> e){
		estado.add(e);
	}
	
	public void addEstadoAmbulancia(Entry<Integer, Double> e){
		estadoAmbulancia.add(e);
	}
	
	public void addDistancia(double d){
		distanciaTotal += d;
		distancias.add(distanciaTotal);
	}

	public void print() {
		for(int i=0; i<rota.size(); i++){
			System.out.println(rota.elementAt(i).ID + " - " + rota.elementAt(i).nome+
					"\t Em Habitacao: "+nrPacientes.get(i)+
					"\t Distancia Percorrida: "+distancias.get(i)+
					"\t Bomba? "+estado.get(i).getKey()+" | Sucursal? "+estado.get(i).getValue()+
					"\t Ambulancia -> Ocupantes: "+estadoAmbulancia.get(i).getKey()+" | Combustível: "+estadoAmbulancia.get(i).getValue());
		}
		System.out.println("Distancia Total: "+distanciaTotal);
	}	
}
