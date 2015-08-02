package Storage;





public class Constants{
	
	//where the images will all be saved

	
	//for the quantification

	public static double MMSizeOfPixel = .00104166;
	
	//for the file to be opened
	public static String file_to = "/Users/techlab/Desktop/procimg.jpg";
	public static String file_destination = "";
	
	//for the dfs
	public static int pixelCount = 30;
	public static int span = 1;
	public static int areaThreshold = 1;//max value makes it not do anything
	
	//for the basic threshold filter
	public static int AmountAboveAve = 140;
	
	//for local average
	public static int pixelSize = 10;
	
	
	public static int RADIUS_CHECK = 27;
	public static int SQUARE_CHECK = 16;
	public static int traceTheshold = 251*4;
	
	//quad tree stuff
	public static int QuadViewDepth = 15;
	public static int DifThreshold = 752;
	
	public static double const1 = 0;
	public static double const2 = .5;
	public static double const3 = 1;
	public static double const4 = 3;
	public static double const5 = 3;
	
	
	
	public static boolean inBounds(int x, int y, int[][] array){
		return (x >=0 && x < array.length && y >= 0 && y < array[0].length)? true: false;
	}
	
	public static void init(){
		//PATH_TO_RESOURCES = 
	}
}
