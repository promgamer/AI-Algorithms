package logic;

public class Sucursal extends Edificio{
	final private int capacidade_pacientes;
	private int nrPacientes;
	
	public Sucursal(int capacidade){
		capacidade_pacientes = capacidade;
		nrPacientes = 0;

	}
	
	public int encher(int nr){
		if( nrPacientes + nr > capacidade_pacientes)
			return -1;
		else{
			nrPacientes += nr;
			return nrPacientes;
		}
	}
	
	public void retirar(int nr){
		nrPacientes -= nr;
	}
	
	public int esvaziar(){
		int res = nrPacientes;
		nrPacientes = 0;
		return res;
	}
	
	public int pacientes_presentes(){
		return nrPacientes;
	}
}
