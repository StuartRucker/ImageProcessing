package Quantification;

import Storage.Image;


/* Finds the number of veins 
 * in in an area
 * val = veins pixels per 10x10 grid
 */
public class Density{
	Length l;
	Image img;
	double val;
	public Density(Image s, Length d) {
		this.l = d;
		img = s;
		process();
	}


	public void process() {
		
		val = l.magorNerveCount/img.imageSize;
	}
	
	public double getVal(){
		return val;
	}

}
