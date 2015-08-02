package threeD;






import java.util.ArrayList;


import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import NerveTree.NerveNode;

 
public class Sphere {
	public static double TOTAL_ANGLE = 90;
	public static final double EYE_RADIUS = 100;
	public static int WIDHT = 800;
	public static int HEIGHT = 600;
	
	//for mouse input
	private static float mxspd = -.005f;
	private static float myspd = -.005f;
	
	public Player p;
	public ArrayList<DrawingVein> drawings;
	public ArrayList<float[]> background;
	
	/*
	 * @params 2d array of 3 colors 
	 */
	
	public void start(ArrayList<NerveNode> roots, int[][] colors) {
		Mouse.setGrabbed(true);
		drawings = DrawingVein.generate(roots);
		background = getCoordinates(colors);
		
		p = new Player();
		
      try {
  	    Display.setDisplayMode(new DisplayMode(WIDHT,HEIGHT));
  	    Display.create();
  	} catch (LWJGLException e) {
  	    e.printStackTrace();
  	    System.exit(0);
  	}
   
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GLU.gluPerspective(60,((float)WIDHT/(float)HEIGHT),1,1500);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
			GL11.glLoadIdentity();
			p.look();
			
			GL11.glColor3f(1f, 1f, 1f);
			Sphere s = new Sphere();
//			s.draw(5, 25, 25);
			
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				p.move(Player.LEFT);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				p.move(Player.RIGHT);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				p.move(Player.BACK);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				p.move(Player.FORWARD);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
				p.move(Player.UP);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				p.move(Player.DOWN);
			}
			p.rotatex((float) (Mouse.getDX()) * mxspd);
			p.rotatey((float) (Mouse.getDY()) * myspd);

			
			
			
			
	
			
			// Clear the screen and depth buffer
			GL11.glBegin(GL11.GL_QUADS);
			for(DrawingVein v: drawings){
				GL11.glColor3f(1f, 0f,0f);
				
				    GL11.glVertex3f(v.p1[0],v.p1[1],v.p1[2]);
				    GL11.glVertex3f(v.p2[0],v.p2[1],v.p2[2]);
				    GL11.glVertex3f(v.p3[0],v.p3[1],v.p3[2]);
				    GL11.glVertex3f(v.p4[0],v.p4[1],v.p4[2]);
				
			}
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_POINTS);
			for(float[] p: background){
				GL11.glColor3f(p[3], p[4],p[5]);
				GL11.glVertex3f(p[0],p[1],p[2]);
			}
			GL11.glEnd();

	    	
	
	     
	    	
	    	
		    Display.update();
		}
	 
		Display.destroy();
    }
	
	
//    public static void main(String[] argv) {
//        Sphere sphere = new Sphere();
//        
//        int[][] addMe = PictureLoader.loadInt("/Users/Stuart/Desktop/illuminati.jpg");
//        int[][][] startMe = new int[addMe.length][addMe[0].length][3];
//        for(int x = 0; x < addMe.length; x ++){
//        	for(int y = 0; y < addMe[0].length; y ++){
//        		int asdf[] = {addMe[x][y]/3,addMe[x][y]/3,addMe[x][y]/3};
//        		startMe[x][y] = asdf;
//        	}
//        }
//        
//        sphere.start(startMe);
//    }
   
    /*
     * @params array of 2d array of pixels with 3 colors 
     * @returns ArrayList of coordinates and colors {x,y,z,r,g,b}
     */
    public ArrayList<float[]> getCoordinates(int[][] colors){
		ArrayList<float[]> returnMe = new ArrayList<float[]>();
    	
    	//convert map 2d point to surface of sphere
		double increment = TOTAL_ANGLE/colors.length;
    	
    	
    	float lastZ = 0;
    	
    	for(int angleXY = -90; angleXY < 90; angleXY += 4){
    		for(int angleZ = -180; angleZ < 180; angleZ += 4){
    	
    			int zCoord =  (int) (EYE_RADIUS * Math.cos(Math.toRadians(angleZ)));
    			
    			double ZRadiusScale = EYE_RADIUS * Math.sin(Math.toRadians(angleZ));
    			
    			
    			int xCoord = (int) (ZRadiusScale*Math.cos(Math.toRadians(angleXY)));
    			int yCoord = (int) (ZRadiusScale*Math.sin(Math.toRadians(angleXY)));
    			
    			float addMe[];
    			if(angleXY < DrawingVein.TOTAL_ANGLE && angleZ >= 0 && angleZ < DrawingVein.TOTAL_ANGLE){
	    			float[] addMe1= {xCoord, yCoord, zCoord, 
	    					.6f, .6f, .6f};
	    			addMe = addMe1;
	    		}else{
	    			float[] addMe1= {xCoord, yCoord, zCoord, 
	    					1f, 1f, 1f};
	    			addMe = addMe1;
	    		}
    			returnMe.add(addMe);
    		}
    	}
    	
    	return returnMe;
    }
    
  
    
}

