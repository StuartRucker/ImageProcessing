package Quantification;

import java.util.Map;

import NerveTree.NerveNode;
import Storage.Constants;
import Storage.Image;

/*
 * 
 */
public class Length{
	private double totalLength;
	
	//for counting density as well
	public int magorNerveCount = 0;
	public int difVariable = 0;
	public NerveNode start;
	
	public Image img;
	double val;
	
	Map<NerveNode,Boolean> used;
	public Length(Image s) {
		img = s;
		process();
		
	}


	private int getLength(NerveNode root) {
		int sum = 0;
		
		
		if(start != null && ((Math.abs(root.x-start.x) > (double) img.getWidth()*6 || Math.abs(root.y-start.y) > (double) img.getHeight()*.6))){
			magorNerveCount++;	
			start = null;
			
		}	
		
		
		for(NerveNode child: root.next){
			sum += 1 + getLength(child);
		}
		return sum;
	}

	public double getTotalLength(){
		return totalLength;
	}

	
	public void process() {
		double vectorCnt = 0;
		for(NerveNode root: img.nc.roots){
			start = root;
			vectorCnt += getLength(root);
		}
		vectorCnt *= Constants.RADIUS_CHECK * Constants.MMSizeOfPixel/ img.imageSize;
		val = vectorCnt;
	}
	
	public double getVal(){
		return val;
	}

	
}
