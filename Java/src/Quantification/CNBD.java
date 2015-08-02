package Quantification;

import Storage.Image;
	
public class CNBD extends Quantifier{
	public CNBD(Image s){
		super(s);
	}

	@Override
	public void process() {

		val = img.nc.mergings / img.imageSize;
		System.out.println("cndb: " + val);
	}
	
}
