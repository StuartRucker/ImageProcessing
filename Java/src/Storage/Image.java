package Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import NerveTree.NerveConverter;
import NerveTree.NerveNode;
import Quantification.QuantificationHandler;
import InitProcessing.Process;
import InitProcessing.VeinMaker;



public class Image {
	

	public int[][] colors;
	public int[][] ogColors;
	public boolean[][] rawProcessed;

	



	public Map<NerveNode, NerveNode> mergings;
	
	//for black area in stitched images
	public boolean[][] error;

	//making a graph of the nerves
	public NerveConverter nc;
	public ArrayList<ArrayList<int[]>> veins;
	public int nerveIds[][];//represents which the broader area of each nerve strand by id
	
	//for quantification
	QuantificationHandler qh;
	public double imageSize; //in mm^2
	
	public Image(int[][] color, int [][] ogColors){
		this.colors = color;
		this.ogColors = ogColors;
		refresh();
	}
	
	private void addVein(){
		VeinMaker vm = new VeinMaker(this);
		veins = vm.assemble();
		nerveIds = vm.getProcesedPixels();
		nc =  new NerveConverter(this);
		
		mergings = nc.getMergings();
	}

	public void refresh(){
		imageSize = getWidth()*getHeight()*Constants.MMSizeOfPixel*Constants.MMSizeOfPixel;
		rawProcessed = Process.preReimann(colors,254);
		
		
		error = Process.removeUnSurrounded(Process.minEdges(ogColors));
		addVein();
		
		qh = new QuantificationHandler(this);
		
	
	}
	public int getWidth(){
		return colors.length;
	}
	public int getHeight(){
		return colors[0].length;
	}
	

	public double getTortuosity(){
		return qh.getTortuosity();
	}
	public double getDensity(){
		return qh.getDensity();
	}
	public double getLength(){
		return qh.getLength();
	}
	public double getTotalLength(){
		return qh.getTotalLength();
	}
	public double getBranchDensity(){
		return qh.getBranchDensity();
	}
	
}
