package Quantification;

import Storage.Image;

public abstract class Quantifier {
	double val;
	Image img;
	public Quantifier(Image s){
		img = s;
		process();
	}
	public abstract void process();
	public double getVal(){
		return val;
	}
}
