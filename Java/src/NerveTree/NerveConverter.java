package NerveTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Storage.Constants;
import Storage.Image;
import Veingraph.Bresenhams;
public class NerveConverter {
	public static final int INITWIDTH = 10;
	public int mergings = 0;
	public int branchings = 0;
	public Image img;
	public ArrayList<NerveNode> roots;
	
	//3d dimensions because multiple points can take up the same square
	public PixelInfo[][] locus;
	Map<NerveNode, NerveNode> mergeMap;
	ArrayList<int[]> mergePairs;
	Map<NerveNode, Boolean> used;
	
	public NerveConverter(Image s){
		mergePairs = new ArrayList<int[]>();
		used = new HashMap<NerveNode,Boolean>();
		
		roots = new ArrayList<NerveNode>();
		img = s;
		
		for(ArrayList<int[]> strand: img.veins ){
			
			if(strand.size() != 0) {
				int id = strand.get(0)[3];
				
				NerveNode root = new NerveNode(strand.get(0)[0],strand.get(0)[1],id);//index 3 is the id
				root.setParents();
				root.isRoot = true;
				
				
				NerveNode last = root;
				roots.add(root);
				for(int i = 1; i < strand.size(); i ++){
					NerveNode current = new NerveNode(strand.get(i)[0],strand.get(i)[1],strand.get(i)[3]);
					last.addChild(current);
					last = current;
				}
				root.length = strand.size();
	
			}
		}
		//set the parents to see if the parents are set correctly
		for(NerveNode root: roots){
			root.setParents();
		}
		locus = initLocus();
		merge();
	}	
	
	/*
	 * Initializes the locus  by giving each marking each pixel
	 * near a certain vector by that tree's id
	 * @returns the initialized locus
	 */
	private PixelInfo[][] initLocus() {
		PixelInfo[][] returnMe = new PixelInfo[img.getWidth()][img.getHeight()];
		
		for(int x = 0; x < returnMe.length; x ++){
			for(int y = 0; y < returnMe[0].length; y ++){
				returnMe[x][y] = new PixelInfo();
			}
		}
		
			for(NerveNode root: roots){
			initTree(root, returnMe);
			expandLocus(root,returnMe);
		}
		return returnMe;
	}
	
	/*recursively initilizes the tree
	 * by making parrallel lines across vectors of INITWIDTH * 2 and marking those
	 * markde by the tree's id
	 */
	public void initTree(NerveNode n, PixelInfo[][] adding){
		for(NerveNode nxt: n.next){
			double[] vector = {nxt.x - n.x,nxt.y - n.y};
			double scale = INITWIDTH/Math.sqrt(vector[0]*vector[0] + vector[1] *vector[1]);
			double perpendicular[] = {-vector[1]*scale,vector[0]*scale};
			
			
			ArrayList<int[]> bPoints = Bresenhams.Bresenham(n.x,n.y,nxt.x,nxt.y);
			int cnt = 0;
			for(int[] displaceMent: Bresenhams.Bresenham(
					(int)(-perpendicular[0]),
					(int)(-perpendicular[1]),
					(int)(perpendicular[0]),
					(int)(perpendicular[1])
			)){
				cnt ++;
				
				for(int[] points: bPoints){
					
					int x = points[0] + displaceMent[0];
					int y = points[1] + displaceMent[1];
					if(x >= 0 && y >= 0 && x < adding.length && y < adding[0].length){
						adding[x][y].add(n);
					}
				}
			}
			initTree(nxt, adding);
		}
	}
	
	/*
	 * Expands the 	 of points for each tree (around the leaves/root) based
	 * on the lenght
	 * @params adding an array of points marked by the tree's id
	 */
	public void expandLocus(NerveNode root, PixelInfo[][] adding){
		if(root.length >= 2){
			int locusLength = Constants.RADIUS_CHECK + 5*(int) Math.sqrt(root.length);
			int locusHeight = Constants.RADIUS_CHECK/3 + 2*(int) Math.sqrt(root.length);
			
			//extend the front vector
			for(NerveNode child: root.next){
				double vector[] = {root.x - child.x, root.y - child.y};
				extendVector(root, vector, locusHeight, locusLength, adding);
			}
			
			//extend the tree's extremeties
			ArrayList<NerveNode> leaves = new ArrayList<NerveNode>();
			root.getLeaves(leaves,root);
			for(NerveNode leaf: leaves){
				
				if(leaf != null && leaf.parent != null){
					double vector[] = {leaf.x-leaf.parent.x, leaf.y-leaf.parent.y};
					extendVector(leaf, vector, locusHeight, locusLength, adding);
				}
			}
		}
	}

	/*
	 * Extends the vector from vein to make the locus of points for the 
	 * merging greater
	 */
	private void extendVector(NerveNode node, double[] vector, int locusHeight, int locusLength, PixelInfo[][] adding) {
		//scale the vector to be locus length long
		
		double scale = locusLength / Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
		vector[0] *= scale;
		vector[1] *= scale;
		
		//find the perpendicular vector
		double perpendicular[] = {-vector[1],vector[0]};
		scale = (double)locusHeight / (double)locusLength;
		perpendicular[0] *= scale;
		perpendicular[1] *= scale;
//		System.out.println("loc: (" + node.x + ", " + node.y + ")");
//		System.out.println("vec: [" + vector[0] + "," + vector[1] + "]");
//		System.out.println("per: [" + perpendicular[0] + "," + perpendicular[1] + "]");
		
		
		ArrayList<int[]> line = Bresenhams.Bresenham(
					(int)(node.x),
					(int)(node.y),
					(int)(node.x + vector[0]),
					(int)(node.y + vector[1]));
		
		ArrayList<int[]> perDisplacement = Bresenhams.Bresenham(
				(int)(-perpendicular[0]),
				(int)(-perpendicular[1]),
				(int)(perpendicular[0]),
				(int)(perpendicular[1]));
		
		for(int[] point: line){
			for(int[] displaceMent:perDisplacement ){
				int addX = point[0] + displaceMent[0];
				int addY = point[1] + displaceMent[1];
				//System.out.println("add: (" + addX + ", " + addY + ")");
				if(addX >= 0 && addX < adding.length && addY >= 0 && addY < adding[0].length){
					adding[addX][addY].add(node);
				}
				
			}
		}	
		
		
		
	}
	
	
	/* 
	* 	Finds overlapping zones of each vein, and merges them trees
	*/
	public void merge(){
		for(int x = 0; x < locus.length; x ++){
			for(int y = 0; y < locus[0].length; y ++){
				if(locus[x][y].size() >= 2){
					NerveNode a = locus[x][y].get(0);
					NerveNode b = locus[x][y].get(1);
					int pair[] = {a.treeId, b.treeId};
					if(!taken(pair)){
						mergePairs.add(pair);
						mergeTrees(a,b);
					}
				}
			}
		}
		for(int i = 0; i < roots.size(); i ++){
			if(roots.get(i).isMerged){
				roots.remove(i++);
				
			}
		}
	}

	/*
	 * @params pair the pair of treeids for the contested merge trees
	 * @returns whether a pair of trees has been merged already
	 */
	private boolean taken(int[] pair) {
		for(int[] p: mergePairs){
	
			if((p[1] == pair[0] && p[0] == pair[1])||
			   (p[0] == pair[0] && p[1] == pair[1])){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Merges the trees 
	 * @params a b the nodes that should be connected
	 */
	private void mergeTrees(NerveNode a, NerveNode b) {
//		System.out.println("merging tree "+ a.treeId + " and " + b.treeId);
		
		
		
		//add node a to b\
		boolean connecting = true;
		NerveNode selectionA = a, selectionB = b;
		if(a.parent != null && a.parent.parent == null){
			selectionA = a.parent;
			connecting = false;
		}
		if(a.next.size() > 0 && a.next.get(0).next.size() == 0){
			selectionA = a.next.get(0);
			connecting = false;
		}
		
		if(b.parent != null && b.parent.parent == null){
			selectionB = b.parent;
			connecting = false;
		}
		if(b.next.size() > 0 && b.next.get(0).next.size() == 0){
			selectionB = b.next.get(0);
			connecting = false;
		}
		
		//only add if the vector from selectionB to selectionB does not overlap any other vector
		
		double[] point = {selectionB.x,selectionB.y};
		double[] vector = {selectionA.x-selectionB.x,selectionA.y-selectionB.y}; 
		boolean hasFoundOverlap = false;
		for(NerveNode root: roots){
			if(overlapAnyThing(point,vector,root)){
				hasFoundOverlap = true;
			}
		}
		
		if(!hasFoundOverlap){
			selectionB.addChild(selectionA);
			if(!connecting){
				mergings++;
			}
		}
	}
	
	/*
	 * goes through roots and checks to see if 
	 * @params vector intersects any of the exisiting vectors
	 * @returns whether the vector overlaps anything
	 */
	public boolean overlapAnyThing(double[] point, double[] vector, NerveNode s){
		if(!used.containsKey(s)){
			used.put(s, true);
			boolean hasOverlap = false;
			for(NerveNode child: s.next){
				double cVector[] = {s.x - child.x, s.y - child.y};
				double cPoint[] = {child.x, child.y};
				if(overlap(point,vector,cPoint,cVector) || overlapAnyThing(point,vector, child)) hasOverlap = true;
			}
			
			return hasOverlap;
		}
		return false;
	}
	
	/*
	 * @returns whether
	 * @params vector1 vector2 overlap
	 */
	public static boolean overlap(double point1[], double[] vector1, double point2[], double[] vector2){
		//use paramteric equations to find overlap
		double t2 = (vector1[1]*(point2[0]-point1[0]) - vector1[0]*(point2[1]-point1[1]))/
							(	(vector1[0]*vector2[1]) - (vector1[1]*vector2[0])	);
		double t1 = (point2[0] + vector2[0]*t2 - point1[0])/vector1[0];
		
		//return whether dem t's in range
		return (Math.abs(t2-.5) < .48) && (Math.abs(t1-.5) < .48);
	}

	public Map<NerveNode, NerveNode> getMergings() {
		
		return null;
	}
	
	//____OVERLAP TESTER_____
//	public static void main(String[] args){
//		double pointTest1[] = {0,0};
//		double pointTest2[] = {3,0};
//		double v1 []= {1,1};
//		double v2 []= {-3,4};
//		System.out.println("do they overlap?: " + overlap(pointTest1,v1,pointTest2,v2));
//	}
	



}
