package Veingraph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Bresenhams {
	
	/*Uses bresenhams line drawing algorithm
	 * to return an ArrayLIst of points which the line would go through
	 * @params startx starty where the coordinates of the starting point
	 * @returns the arraylist of points which the lines would go through
	 */
	public static ArrayList<int[]> Bresenham(int startX, int startY, int endX, int endY){
		storedPoints = new ArrayList<int[]>();
		Bresenham1(startX,startY,endX,endY);
		return (ArrayList<int[]>) storedPoints.clone();
	}
	
	/*Uses bresenhams circle drawing algorithm
	 * to return an ArrayLIst of points which the line would go through
	 * @params x y center of the circle coordinates
	 * @returns the arraylist of points which the circle would go through
	 */
	public static ArrayList<int[]> circle(int x, int y, int radius){
		storedPoints = new ArrayList<int[]>();
		circleHelp(x,y,radius);
		return (ArrayList<int[]>) storedPoints.clone();
	}
	
	
	
	static int asx; 
	static ArrayList<int[]> storedPoints;
	private static void Bresenham2(int startX, int startY, int endX, int endY, boolean ref){
		   
		   if(endX<startX){int tmp = startX; startX = endX; endX = tmp;}
		   if(endY<startY){int tmp = startY; startY = endY; endY = tmp;}
		   int A = 2*(endY-startY);
		   int B  = A - 2*(endX-startX);
		   int P = A - (endX-startX);
		   for(int i = startX; i < endX; i ++){
		     if(P<0){
		       if(ref)point(asx*2-i, startY);
		       else point(i, startY);
		       P += A;
		     }
		     else{
		      P += B;
		       if (ref) point(asx*2-i, ++startY); 
		      else point(i, ++startY);
		     }   
		     
		   }
		 }
	private static void point(int x, int y) {
		storedPoints.add(new int[]{x,y});
		
	}
	private static void Bresenham3(int startX, int startY, int endX, int endY, boolean ref){
		   if(endX<startX){int tmp = startX; startX = endX; endX = tmp;}
		   if(endY<startY){int tmp = startY; startY = endY; endY = tmp;}
		   int A = 2*(endX-startX);
		   int B  = A - 2*(endY-startY);
		   int P = A - (endY-startY);
		   for(int i = startY; i < endY; i ++){
		     if(P<0){
		       if(ref) point(asx*2-startX, i);
		       else point(startX, i);
		       P += A;
		     }
		     else{
		      P += B;
		      if(!ref) point(startX++,i); 
		       else   point(asx*2- (startX++),i);
		     }   
		     
		   }
		 }
	private static void Bresenham1(int startX, int startY, int endX, int endY){
		   boolean reflect = false;
		   asx = startX;
		   if((endX > startX && endY < startY) || (endX < startX && endY > startY)){
		     
		     reflect = true;
		     endX = 2*startX-endX;
		   }
		   
		   
		   if(Math.abs(endX-startX)>=Math.abs(startY-endY)) Bresenham2(startX,startY,endX,endY,reflect);
		   else Bresenham3(startX,startY,endX,endY,reflect);
		}
	public static void circleHelp(int xCent, int yCent, int r) {

		int d = (5 / 4) * r;
		int x = 0;
		int y = r;
		do {

			point(y + xCent, x + yCent);
			point(x + xCent, y + yCent);
			point(x + xCent, -y + yCent);
			point(y + xCent, -x + yCent);
			point(-y + xCent, -x + yCent);
			point(-x + xCent, -y + yCent);
			point(-x + xCent, y + yCent);
			point(-y + xCent, x + yCent);

			if (d < 0) {
				d = d + 2 * x + 3;
			} else {
				d = d + 2 * (x - y) + 5;
				y = y - 1;
			}
			x = x + 1;
		} while (x < y);

	}

}
