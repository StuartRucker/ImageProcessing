package NerveTree;

import java.util.ArrayList;

/* This class is a effectively a non-generic ArrayList
 * Arraylists can not be put into arrays because they use generics
 * PixelInfos, however, can.
 */
public class PixelInfo {
	private boolean isEmpty = true;
	ArrayList<NerveNode> taken;
	public PixelInfo(){
		taken = new ArrayList<NerveNode>(1);
	}
	public void add(NerveNode n){
		isEmpty = false;
		boolean alreadyHere = false;
		for(NerveNode other: taken){
			if(other.treeId == n.treeId){
				alreadyHere = true;
			}
		}
		
		
		if(!alreadyHere) taken.add(n);
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public NerveNode get(){
		return taken.get(0);
	}
	public NerveNode get(int i ){
		return taken.get(i);
	}
	
	public int size() {
		return taken.size();
	}
	
}
