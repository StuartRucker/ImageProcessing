package InitProcessing;



import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import Storage.Constants;



public class Process {

	public static int[][][] getAverages(int[][] colors, boolean [][] error){
		int pixel = Constants.pixelSize;

		
		int[][] localAve = new int[colors.length][colors[0].length];
		int[][] localSD = new int[colors.length][colors[0].length];
		for (int x = 0; x < colors.length; x += pixel) {
			for (int y = 0; y < colors[0].length; y += pixel) {
				int sum = 0;
				int cnt = 0;
				boolean corrupted = false;
				loop: for (int x1 = x; x1 < x + pixel; x1++) {
					for (int y1 = y; y1 < y + pixel; y1++) {
						if (x1 < colors.length && y1 < colors[0].length) {
							cnt++;
							sum += colors[x1][y1];
							if(colors[x1][y1] == 0){
								corrupted = true;
								break loop;
							}
						}
					}
				}
				if(corrupted){
					for (int x1 = x; x1 < x + pixel; x1++) {
						for (int y1 = y; y1 < y + pixel; y1++) {
							if (x1 < colors.length && y1 < colors[0].length) {
								
								error[x1][y1] = true;
							}
						}
					}
				}
				
				sum /= cnt;
				int sqSum = 0;
				cnt = 0;
				for (int x1 = x; x1 < x + pixel; x1++) {
					for (int y1 = y; y1 < y + pixel; y1++) {
						if (x1 < colors.length && y1 < colors[0].length) {
							sqSum += Math.pow((sum - colors[x1][y1]), 2);
							cnt++;
							localAve[x1][y1] = sum;
						}
					}
				}
				int sd = (int) Math.sqrt(sqSum / (cnt - 1));
				for (int x1 = x; x1 < x + pixel; x1++) {
					for (int y1 = y; y1 < y + pixel; y1++) {
						if (x1 < colors.length && y1 < colors[0].length) {
							localSD[x1][y1] = sd;
						}
					}
				}

			}
		}
		return new int[][][] { localAve, localSD };
	}

	public static int[][] increaseContrast(double contrast, int[][] colors,
			int[][] localAve) {
		double factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
		int newPic[][] = new int[colors.length][colors[0].length];
		for (int x = 0; x < colors.length; x++) {
			for (int y = 0; y < colors[0].length; y++) {
				newPic[x][y] = (int) ((factor * (colors[x][y] - localAve[x][y])) + localAve[x][y]) / 3;
			}
		}
		return newPic;
	}

	// weights the pixels along with the pixels near it to identify the whitest
	// pixels
	public static boolean[][] reiman(boolean[][] error, int[][] colors, int[][] localAve) {
		boolean[][] binary = new boolean[colors.length][colors[0].length];

		int posX[] = { 1, 1, 1, 0, 0, 0, -1, -1, -1 };
		int posY[] = { -1, 0, 1, -1, 0, 1, -1, 1, 1 };
		
		int ratings[] = {
				2, 5, 2,
				5, 10, 5,
				2, 5, 2 };
		// int ratings[] = {0,0,0,0,10,0,0,0,0};
		for (int x = 0; x < colors.length; x++) {
			for (int y = 0; y < colors[0].length; y++) {
				int scoreCnt = 0;
				int cnt = 0;
				
				for (int i = 0; i < ratings.length; i++) {
					int XNew = x + posX[i];
					int YNew = y + posY[i];
					
					if (XNew < colors.length && XNew >= 0
							&& YNew < colors[0].length && YNew >= 0) {
						int colDif = colors[XNew][YNew];
						// if(colDif > 0){
						scoreCnt += colDif * ratings[i];
						// }
						cnt += ratings[i];
					}
				}

				// find the local average
				if (cnt != 0
						&& scoreCnt / cnt > localAve[x][y]
								+ Constants.AmountAboveAve) {
					// System.out.println(scoreCnt/cnt);
					binary[x][y] = true;
				} else {
					binary[x][y] = false;
				}

				// check to see if the pixel is to near the edge of the
				// image(for black area

				if(error[x][y]){
					binary[x][y] = false;
				}
				
				
			}
		}
		return binary;
	}

	static int minX, maxX, minY, maxY;

	public static boolean[][] DFS(boolean[][] proc) {

		boolean[][] processed = new boolean[proc.length][proc[0].length];

		// save the coordinates of each connect segment to later assemble veins
		ArrayList<int[]> startPlaces = new ArrayList<int[]>();

		for (int x = 0; x < proc.length; x++) {
			for (int y = 0; y < proc[0].length; y++) {
				if (proc[x][y] && !processed[x][y]) {
					startPlaces.add(new int[] { x, y });
					minX = Integer.MAX_VALUE;
					minY = Integer.MAX_VALUE;
					maxX = Integer.MIN_VALUE;
					maxY = Integer.MIN_VALUE;

					ArrayList<int[]> coords = new ArrayList<int[]>();
					boolean destroy = false;

					int size = DFSRecursive(x, y, coords, Constants.span,
							processed, proc);
					if (size < Constants.pixelCount
							|| Constants.areaThreshold > (maxY - minY)
									* (maxX - minX)) {
						destroy = true;
					}

					for (int i = 0; i < coords.size(); i++) {
						if (destroy) {
							proc[coords.get(i)[0]][coords.get(i)[1]] = false;
						} else {
							proc[coords.get(i)[0]][coords.get(i)[1]] = true;
						}
					}

				}
			}
		}
		return proc;
		
	}

	public static int DFSRecursive(int x, int y, ArrayList<int[]> coords,
			int span, boolean processed[][], boolean[][] binary) {
		// used to find the spread of clump
		if (x < minX)
			minX = x;
		if (y < minY)
			minY = y;
		if (x > maxX)
			maxX = x;
		if (y > maxY)
			maxY = y;

		processed[x][y] = true;
		coords.add(new int[] { x, y });

		// deal with spanning over gaps
		int sumSize;
		if (binary[x][y]) {
			span = Constants.span;
			sumSize = 1;// initialized at 1 to add itself
		} else {
			sumSize = 0;
		}

		// end recursion if you have already spanned over two
		if (span == 0) {
			return sumSize;
		}

		int directionsX[] = { 1, 1, 1, 0, 0, -1, -1, -1 };
		int directionsY[] = { -1, 0, 1, -1, 1, -1, 0, 1 };

		for (int i = 0; i < directionsX.length; i++) {
			int XNew = x + directionsX[i];
			int YNew = y + directionsY[i];
			if (XNew < processed.length && XNew >= 0
					&& YNew < processed[0].length && YNew >= 0) {
				if (!processed[XNew][YNew] && binary[XNew][YNew]) {
					sumSize += DFSRecursive(XNew, YNew, coords, span,
							processed, binary);
				}
				if (!processed[XNew][YNew]) {
					sumSize += DFSRecursive(XNew, YNew, coords, span - 1,
							processed, binary);
				}
			}
		}

		return sumSize;
	}

	public static int[][] criticalPoints(int[][] colors) {
	
		int[][] as = subtraction(colors, gaussianBlur(colors.clone()));
		// as = subtraction(as,gaussianBlur(as));
		// as = subtraction(as,gaussianBlur(as));
		//int[][] subtraction = subtraction(as, gaussianBlur(as));
		return as;

	}

	public static int[][] subtraction(int c[][], int[][] gblur) {

		int[][] gsub = new int[c.length][c[0].length];
		// int highestPoints[] = new int[10];
		// int highestPointsX[] = new int[10];
		// int highestPointsY[] = new int[10];

		// //init
		// for(int i = 0; i < highestPoints.length; i ++){
		// highestPoints[i] = Integer.MIN_VALUE;
		// }

		for (int x = 0; x < gsub.length; x++) {
			for (int y = 0; y < gsub[0].length; y++) {

				int score = (c[x][y] - gblur[x][y]);
				/*
				 * boolean found = false; for(int i = 0; i <
				 * highestPoints.length && !found; i ++){
				 * 
				 * if( score > highestPoints[i]){ found = true; for(int k = i +
				 * 1; k < highestPoints.length; k ++ ){ highestPoints[k] =
				 * highestPoints[k - 1] ; highestPointsX[k] = highestPointsX[k -
				 * 1]; highestPointsY[k] = highestPointsY[k - 1]; }
				 * highestPoints[i] = score; highestPointsX[i] = x;
				 * highestPointsY[i] = y; } }
				 */

				gsub[x][y] = score * 2 + 100;
			}

		}

		// make the key points blue
		/*
		 * for(int i = 0; i < highestPoints.length; i ++){
		 * System.out.println(highestPointsX[i] + " " + highestPointsY[i]);
		 * //color rect for(int x = highestPointsX[i]; x < highestPointsX[i] +
		 * 10 && x < colors.length; x ++){ for(int y = highestPointsY[i]; y <
		 * highestPointsY[i] + 10 && y < colors[0].length; y ++){
		 * 
		 * gsub[x][y] = Integer.MIN_VALUE; } } }
		 */
		return gsub;
	}

	public static int[][] gaussianBlur(int[][] toBlur) {
		int gauss[] = { 1, 14, 91, 364, 1001, 2002, 3003, 3432, 3003, 2002,
				1001, 364, 91, 14, 1 };
		int[][] gblur = new int[toBlur.length][toBlur[0].length];

		for (int x = 0; x < toBlur.length; x++) {
			for (int y = 0; y < toBlur[0].length; y++) {
				int localAve = 0;
				int tot = 0;
				for (int i = 0; i < gauss.length; i++) {

					int pos = y + i - gauss.length / 2;
					if (pos > 0 && pos < toBlur[0].length) {
						localAve += gauss[i] * toBlur[x][pos];
						tot += gauss[i];
					}
				}
				gblur[x][y] = localAve / tot;
			}
		}
		for (int x = 0; x < toBlur.length; x++) {
			for (int y = 0; y < toBlur[0].length; y++) {
				int localAve = 0;
				int tot = 0;
				for (int i = 0; i < gauss.length; i++) {
					int pos = x + i - gauss.length / 2;
					if (pos >= 0 && pos < toBlur.length) {
						localAve += gauss[i] * gblur[pos][y];
						tot += gauss[i];
					}
				}
				gblur[x][y] = localAve / tot;
			}
		}
		return gblur;
	}

	public int[][] lapacian(int colors[][]) {

		int[][] lapacian = new int[colors.length][colors[0].length];

		int posX[] = { 1, 1, 1, 0, 0, 0, -1, -1, -1 };
		int posY[] = { -1, 0, 1, -1, 0, 1, -1, 1, 1 };
		int ratings[] = { 0, 1, 0, 1, -4, 1, 0, 1, 0 };
		// int ratings[] = {0,0,0,0,4,0,0,0,0};

		for (int x = 0; x < colors.length; x++) {
			for (int y = 0; y < colors[0].length; y++) {

				int scoreCnt = 0;
				int cnt = 0;
				for (int i = 0; i < ratings.length; i++) {
					int XNew = x + posX[i];
					int YNew = y + posY[i];
					if (XNew < colors.length && XNew >= 0
							&& YNew < colors[0].length && YNew >= 0) {
						scoreCnt += ratings[i] * colors[XNew][YNew];
						cnt++;
						/*
						 * int colDif = colors[XNew][YNew] -
						 * localAve[XNew][YNew]; if(colDif > 0){ scoreCnt +=
						 * colDif * ratings[i]; } cnt ++;
						 */
					}

					if (cnt != 0)
						scoreCnt /= cnt;
					colors[x][y] = scoreCnt * 50;
				}
			}
		}

		return lapacian;
	}

	
	public static boolean[][] sparse(boolean[][] dfsProcessed) {
		boolean returnMe[][] = new boolean[dfsProcessed.length][dfsProcessed[0].length];
		for(int x = 0; x < dfsProcessed.length; x ++){
			for(int y =0; y < dfsProcessed[0].length; y ++){
				if(dfsProcessed[x][y] && Math.random() < .01){
					returnMe[x][y] = dfsProcessed[x][y];
				}
			}
		}
		return returnMe;
	}

	public static ArrayList<int[]> listFromArray(boolean[][] dfsSparseProcessed) {
		ArrayList<int[]> returnMe = new ArrayList<int[]>();
		for(int x = 0; x < dfsSparseProcessed.length; x ++){
			for(int y =0; y < dfsSparseProcessed[0].length; y ++){
				if(dfsSparseProcessed[x][y]){
					returnMe.add(new int[]{x,y});
				}
			}
		}
		return returnMe;
	}
	
//

	
	/*
	 * Sets the values of the pixels which are black to false
	 * so they are not accidenly recognized by DFS 
	 */
	public static boolean[][] minEdges(int[][] colors) {
		boolean [][]returnMe = new boolean[colors.length][colors[0].length];
		for(int x = 0; x < colors.length;x ++){
			for(int y = 0; y < colors[0].length; y ++){
				if(colors[x][y]  == 0){
					returnMe[x][y] = true;
				}
			}
		}
		return returnMe;
		
	}
	
	/*
	 * Removes error pixels with less the 2 neighbors
	 * Used to remove black pixels on border of stitched images. In the 
	 * localAve method the pixels with a black neighbor were put in error.
	 * Now, this will make only black pixels on rim remain
	 */
	public static boolean[][] removeUnSurrounded(boolean[][] error) {
			boolean[][] returnMe = new boolean[error.length][error[0].length];
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("output.csv");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			for(int x = 0; x < error.length; x ++){
				for(int y = 0; y  < error[0].length; y ++){
					
					returnMe[x][y] = error[x][y];
					//only process error pixels
					if(error[x][y]){
						int neighbors = 0;
						for(int dx = x-8; dx <= x +3; dx ++){
							for(int dy = y -3 ; dy < y +3; dy ++){
								
								if(dx >= 0 && dy >= 0 && dy < error[0].length && dx < error.length){
									if(error[dx][dy]) neighbors++;
								}
							}
						}
						writer.println(x + ", " + y);
						if(neighbors < 2){
							returnMe[x][y] = false;
							System.out.println( x + ", " + y);
						}else if(neighbors > 5){
							returnMe[x][y] = false;
						}
						
						
					}
				}
			}
			writer.close();
			return returnMe;
	}
	
	/*****************
	 * Methods below here are for the preprocessed image
	 * (yes, I know it is a bad coding practice to jam it all in one class)
	 */
	
	public static boolean[][] preReimann(int[][] colors, int thresh){
		boolean binary[][] = new boolean[colors.length][colors[0].length];
		for(int x = 0; x < colors.length; x ++){
			for(int y = 0; y < colors[0].length; y ++){
				if(colors[x][y] < thresh*3) binary[x][y] = true;
			}
		}
		return binary;
	}
	
}
