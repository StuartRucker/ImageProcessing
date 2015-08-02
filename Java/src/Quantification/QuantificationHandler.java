package Quantification;

import Storage.Image;



/*
 *Uses the VeinGraph to then generate
 *tortuosity: curvature of each vein
 *Length: 
 *Width: Width of each vein
 */
public class QuantificationHandler {
	double tortuosity, density, length, totallength, cnbd;
	
	public QuantificationHandler( Image s){

		tortuosity = new Tortuosity(s).getVal();
		Length l = new Length(s);
		System.out.println("magornervecount: " + l.magorNerveCount);
		length = l.getVal();
		
		density = new Density(s,l).getVal();
		
		cnbd = new CNBD(s).getVal();
		
		
//		System.out.println("tortuosity: " + tortuosity);
//		System.out.println("Density: " + density);
//		System.out.println("Length: " + length);
//		System.out.println("total Length: " + totallength);
		
	}
	public double getTortuosity(){
		return Math.abs(tortuosity);
	}
	public double getDensity(){
		return density;
	}
	public double getLength(){
		return length;
	}
	public double getTotalLength(){
		return totallength;
	}
	public double getBranchDensity(){
		return cnbd;
	}
	
}
