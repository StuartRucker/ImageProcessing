package threeD;

import java.util.ArrayList;



import NerveTree.NerveNode;

public class DrawingVein {
	public static double TOTAL_ANGLE = 90;
	public static final double EYE_RADIUS = 100;
	
	public float[] p1;
	public float[] p2;
	public float[] p3;
	public float[] p4;
	double increment;
	
	public static int WIDTH = 5;
	public DrawingVein(int startX, int startY, int endX, int endY){
		double[] vector = {endX-startX, endY-startY};
		double scale = WIDTH/(Math.sqrt(vector[0]*vector[0] + vector[1] + vector[1]));
		double[] perpendular = {-vector[1]*scale,vector[0]*scale};
		increment = TOTAL_ANGLE/400;
		
		p1 = getCoords(startX + perpendular[0],startY + perpendular[1]);
		p2 = getCoords(startX - perpendular[0],startY - perpendular[1]);
		p4 = getCoords(endX + perpendular[0],endY + perpendular[1]);
		p3 = getCoords(endX - perpendular[0],endY - perpendular[1]);	
	}
	
	private float[] getCoords(double x, double y) {
		//first convert from the 2d image coords to spherical coordinates, then to XYZ
		double angleXY = x * increment;
		double angleZ = y * increment;
		int zCoord =  (int) (EYE_RADIUS * Math.cos(Math.toRadians(angleZ)));
		
		double ZRadiusScale = EYE_RADIUS * Math.sin(Math.toRadians(angleZ));
		
		
		int xCoord = (int) (ZRadiusScale*Math.cos(Math.toRadians(angleXY)));
		int yCoord = (int) (ZRadiusScale*Math.sin(Math.toRadians(angleXY)));
		

		
		float addMe[] = {xCoord, yCoord, zCoord};
		
		return addMe;
	}
	

	public static ArrayList<DrawingVein> generate(ArrayList<NerveNode> roots){
		ArrayList<DrawingVein> returnMe = new ArrayList<DrawingVein>();
		for(NerveNode rt: roots){
			addNerves(rt, returnMe);
		}
		return returnMe;
	}

	private static void addNerves(NerveNode rt, ArrayList<DrawingVein> returnMe) {
		for(NerveNode child: rt.next){
			returnMe.add(new DrawingVein(rt.x,rt.y,child.x,child.y));
			addNerves(child,returnMe);
		}
		
	}
}
