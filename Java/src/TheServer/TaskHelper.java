package TheServer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import NerveTree.NerveNode;
import Storage.Image;
import Util.PictureLoader;
import Veingraph.Bresenhams;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.utilities.Base64;
import com.firebase.client.utilities.Base64.OutputStream;

public class TaskHelper {
	Image img;
	BufferedImage pImg, rawImg;
	static Firebase ref;
	static boolean initRef = false;
	public static int[] vals;
	
	boolean error = false;
	public TaskHelper(BufferedImage b, BufferedImage rawStart){
		if(!initRef){
			ref = new Firebase("https://opencv.firebaseio.com/data/average");
			vals = new int[3];
			initRef = true;
		}
		
		if(b == null){
			error = true;
			System.out.println("error image" );
		}
		if(!error){
			img = new Image(PictureLoader.loadInt(b), PictureLoader.loadInt(rawStart));
			rawImg = rawStart;
		}
	
	}
	
	/*
	 * @returns a base64 encoded image with the vectors on it
	 */
	public String getImage(boolean p){
		try{
			//add those nerves
			
			ArrayList<int[]> returnMe = new ArrayList<int[]>();
			for(NerveNode root: img.nc.roots){
				addPoints(root, returnMe);
			}
			
			
			Color[][] overlay = new Color[img.getWidth()][img.getHeight()];
			for(int x = 0; x < overlay.length; x ++){
				for(int y = 0; y < overlay[0].length; y ++){

					overlay[x][y] = new Color(rawImg.getRGB(x, y));
				}
			}
			
			if(p){
				for(int[] point: returnMe){
					if(point[0] >= 0 && point[1] >= 0 && point[1] < overlay[0].length && overlay.length > point[0])
						overlay[point[0]][point[1]] = new Color(255,0,0);
				}
			}
			
			pImg = PictureLoader.getPic(overlay);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStream b64 = new Base64.OutputStream(os);
			ImageIO.write(pImg, "png", b64);
			return os.toString("UTF-8");
		}
		catch(Exception e){
			return "oops";
		}
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
	
	public double[] getData(){
		if(!error){
			double[] data = { img.getTortuosity(), img.getLength(), img.getDensity(), img.getBranchDensity()};
			return data;
		}else{
			double[] data = {-1,-1,-1,-1};
			return data;
		}
	}
	
	public static void handleAverage(final double data, final double data2, final double data3, final double data4){

		ref.addListenerForSingleValueEvent(new ValueEventListener() {
		  
		    public void onDataChange(DataSnapshot snapshot) {
		        String names[] = {"tortuosity","length","density", "CNBD"};
		        double[] values = {data,data2,data3, data4};

		        for(int i = 0; i < names.length; i ++){
					
					double cnt  = Double.parseDouble(snapshot.child(names[i]).child("cnt").getValue().toString());
			        double prevAve  = Double.parseDouble(snapshot.child(names[i]).child("val").getValue().toString());
			        double total = (prevAve*cnt) + values[i];
			  
			        cnt ++;
			        double newAve = total/cnt;
			        ref.child(names[i]).child("val").setValue(newAve);
			        ref.child(names[i]).child("cnt").setValue(cnt);
		        }
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		    }
		});
	}
	

}
