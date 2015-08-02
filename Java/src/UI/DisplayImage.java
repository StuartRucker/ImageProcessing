package UI;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import NerveTree.NerveNode;
import Storage.Image;
import Veingraph.Bresenhams;




public class DisplayImage {
	public static int methodCalls = 0;
	
	public static final boolean LOCUST = true;
	
	public BufferedImage img;
	public Image raw;
	public int x,y;
	public DisplayImage(int x, int y , BufferedImage img, Image raw){
		this.x = x;
		this.y = y;
		this.img = img;
		this.raw = raw;
	}

	/*
	 * overlay points from the vectorization
	 * @returns an arraylist of all points in the vector
	 */
	public ArrayList<int[]> getOverlay() {
		ArrayList<int[]> returnMe = new ArrayList<int[]>();
		for(NerveNode root: raw.nc.roots){
			addPoints(root, returnMe);
		}
//		for(int x = 0; x < img.getWidth(); x ++){
//			for(int y = 0; y < img.getHeight(); y ++){
//				if(!raw.nc.locus[x][y].isEmpty()){
//					int[] addMe = {x,y,0,255-raw.nc.locus[x][y].get().treeId*18,raw.nc.locus[x][y].get().treeId*18};
//					returnMe.add(addMe);
//				}
//			}
//		}
//		for(int x = 0; x < img.getWidth(); x ++){
//			for(int y = 0; y < img.getHeight(); y ++){
//				int addMe[] = {x,y,0,0,0}; 
//				if(raw.rawProcessed[x][y]){
//					for(int i = 2; i < 5; i ++){
//						addMe[i] = 255;
//					
//					}
//				}
//				returnMe.add(addMe);
//			}
//		}
	
		return returnMe;
	}
	public void addPoints(NerveNode n, ArrayList<int[]> punts){
		for(NerveNode child: n.next){
			for(int[] points: Bresenhams.Bresenham(n.x, n.y, child.x, child.y)){
				int[] addMe = {points[0],points[1],255,0,0};
				punts.add(addMe);
			}
			addPoints(child,punts);
		}
	}

}
