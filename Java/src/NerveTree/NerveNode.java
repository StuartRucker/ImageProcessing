package NerveTree;

import java.util.ArrayList;

public class NerveNode {
	//only make length + if this is root 
	public int length = Integer.MIN_VALUE;
	
	public ArrayList<NerveNode> next;
	public int x, y;
	public int treeId;
	
	
	public boolean hasFoundChildren;
	public ArrayList<NerveNode> Allchildren;
	public NerveNode parent;

	public boolean isRoot = false;
	public boolean isMerged = false;
	
	
	public NerveNode(int x, int y, int id){
		next = new ArrayList<NerveNode>();
		this.x = x; 
		this.y = y;
		this.treeId = id;
	}
	public NerveNode(int x, int y, int id, NerveNode nn){
		this(x,y, id);
		next.add(nn);
	}
	
	public void addChild(NerveNode nn){
		next.add(nn);
	}
	
	public ArrayList<NerveNode> getChildren(){
		if(!hasFoundChildren){
			Allchildren = new ArrayList<NerveNode>();
			getChildrenHelper(this,Allchildren);
			
			hasFoundChildren = true;
		}
		return Allchildren;
	}
	
	private void getChildrenHelper(NerveNode n, ArrayList<NerveNode> allChildren){
		
		allChildren.add(this);
		for(NerveNode child: n.next){
			getChildrenHelper(child,allChildren);
		}
	}
	
	/*
	 * recursively sets parents of all children
	 */
	public void setParents(){
		
		for(NerveNode child: next){
			
			child.parent = this;
			child.setParents();
		}
	}
	
	/*
	 * @params nodes empty array list to be filled with leaves
	 */
	public void getLeaves(ArrayList<NerveNode> nodes, NerveNode root) {
		if(root.next.size() == 0){
			nodes.add(root);
		}else{
			for(NerveNode c: root.next){
				//System.out.println("going down the tree: parent: " + (c.parent == null));
				getLeaves(nodes,c);
				
			}
		}
	}
	

//	public void checkParents() {
//		//System.out.println("null parent?: " + (this.parent == null));
//		for(NerveNode c: next){
//			c.checkParents();
//		}
//		
//	}
}
