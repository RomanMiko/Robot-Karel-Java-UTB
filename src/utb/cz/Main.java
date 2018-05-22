package utb.cz;

import utb.cz.Karel;

public class Main {

	public static void main(String[] args) {
		Karel k=new Karel("karel.save");
		k.zacni();
		while (!k.jeZdeZnacka()) {
			while(k.jePredemnouVolno()) {
				k.polozZnacku();
				k.krok();
			}
			k.vleloVbok();
		}
	}

}
