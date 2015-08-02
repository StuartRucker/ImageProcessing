package Quantification;

import java.util.ArrayList;

import Storage.Image;

public class Tortuosity extends Quantifier{

	public Tortuosity(Image s) {
		super(s);
	}

	@Override
	public void process() {
		double totalBend = 0;
		int cnt = 0;
		for(ArrayList<int[]> data: img.veins){
			boolean first = true;
			for(int[] p: data){
				if(!first){
					totalBend += p[2]; //angle is stored from when the nerve was made
					cnt ++;
				}
				first = false;
			}
		}
		val = totalBend/ cnt;
	}
}