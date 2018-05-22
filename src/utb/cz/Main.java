package utb.cz;

import utb.cz.Karel;

public class Main {

	public static void main(String[] args) {
		Karel k=new Karel();
		k.zacni();
		while (!(k.pocetZnacek() == 6)) {
			while(k.jePredemnouVolno()) {
				k.polozZnacku();
				k.krok();
			}
			k.vleloVbok();
		}
	}

}
