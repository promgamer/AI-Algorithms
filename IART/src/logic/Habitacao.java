package logic;

public class Habitacao extends Edificio {

	Habitacao(String nome, int ocupantes) {
		super(nome, ocupantes);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
