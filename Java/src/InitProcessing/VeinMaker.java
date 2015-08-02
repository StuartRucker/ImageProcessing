package InitProcessing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Storage.Constants;
import Storage.Image;
import Veingraph.Bresenhams;


/*
 * Creates a list of all of the veins
 */
public class VeinMaker {
	public static int settingSpan = 15;

	
	
	private Image img;
	public PrintWriter pw;
	
	//empty means not processed
	int processedPixels[][];
	public static int EMPTY = 0;
	
	public VeinMaker(Image s) {
		try {
			pw =  new PrintWriter("output.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.img = s;
		
	}
	public ArrayList<ArrayList<int[]>> assemble(){
		
		
		processedPixels = new int[img.getWidth()][img.getHeight()];
		
		ArrayList<ArrayList<int[]>> allVeins = new ArrayList<ArrayList<int[]>>();

		//keep track of the id of each vein
		int id = 1;
		
		for(int x = 0; x < img.getWidth(); x ++){
			for(int y = 0; y < img.getHeight(); y ++){
				if(processedPixels[x][y] == EMPTY && img.rawProcessed[x][y]){
					ArrayList<int[]> punto = new ArrayList<int[]>(); 
					
					trace(x,y,punto,20,id++);
					
					allVeins.add(punto);
				}
			}
		}
		return allVeins;
	}
	
	private void trace(int x, int y, ArrayList<int[]> array, int depth, int id) {
		
		if(depth == 0) return;
		
		setProcessed(x,y, id);
		
		int minScore = Integer.MAX_VALUE;
		int minX = 0;
		int minY = 0;
		

		
		for(int[] point: Bresenhams.circle(x, y, Constants.RADIUS_CHECK)){
			int score = computeScore(x,y,point[0],point[1]);
			if(score < minScore){
				minScore = score;
				minX = point[0];
				minY = point[1];
			}
		}
		int angle = (array.size() == 0)? Integer.MAX_VALUE: (int) Math.toDegrees( Math.atan2(y-array.get(array.size()-1)[0], x-array.get(array.size()-1)[1]));
		int addMe[] = {x,y,angle,id};
		
		array.add(addMe);
		double anlgeDif = (array.size() < 3)? 0: (Math.abs(angle - array.get(array.size()-2)[2]));
		
		boolean isAngleDif = (anlgeDif < 50 ||    array.get(array.size()-2)[2] == Integer.MAX_VALUE);
		boolean isScoreLess = minScore < 255*3-1;
		boolean notProcessed = (Util.Util.inRange(minX, minY, img.colors))? processedPixels[minX][minY] == EMPTY: true;
	//	System.out.println(isAngleDif + " " + isScoreLess + " " +  notProcessed);
		if(	isAngleDif
				&& isScoreLess
				&& notProcessed){
			setProcessed(x,y,minX,minY, id);
			trace(minX,minY,array,(depth-1),id);
		}
		
	}
	
	//compute the average color of the pixels that the line goes through
	private int computeScore(int x, int y, int x2, int y2) {
		double score = 0,count = 0;
		
		int edgeInRow = 0;
		int highInRow = 0;
		for(int[] points: Bresenhams.Bresenham(x, y, x2, y2)){
			if(Util.Util.inRange(points[0], points[1], img.colors)){
				
				if(img.error[points[0]][points[1]]){
					score +=4000;
				}
				
				//don't let the line go through to much white
				score += (processedPixels[points[0]][points[1]] != EMPTY)? 255*3: img.colors[points[0]][points[1]];
				highInRow = (processedPixels[points[0]][points[1]]  >= 252)? highInRow + 1: 0;
				if(highInRow > 5){score += 20;}
				
				//don't let the line go on the edge
				if(points[0] > processedPixels.length-5 || points[1] > processedPixels[1].length-5 || points[0] < 5 || points[1] < 5 ) edgeInRow ++;
				else edgeInRow = 0;
				if(edgeInRow > 4) score += 100;
				
				
				count ++;
			}
		}
		return (count == 0)? Integer.MAX_VALUE : (int) (score / count);
	}
	
	public void setProcessed(int x, int y, int id){

		for(int dx = x - settingSpan; dx < x + settingSpan; dx ++){
			for(int dy = y - settingSpan; dy < y + settingSpan; dy ++){
				if(Util.Util.inRange(dx, dy, img.colors)){
					processedPixels[dx][dy] = id;
				}
			}
		}
	}
	public void setProcessed(int x, int y, int x2, int y2, int id){
		ArrayList<int[]> points = Bresenhams.Bresenham(x,y, x2, y2);
		//don't set the points to close to the end of the line
		for(int i = 0; i < points.size() - settingSpan; i ++){
			setProcessed(points.get(i)[0],points.get(i)[1], id);
		}
	}
	
	public int[][] getProcesedPixels(){
		return processedPixels;
	}
}


