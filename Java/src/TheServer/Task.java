package TheServer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Storage.Constants;
import Util.FileManager;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;



public class Task{
	public long startTime;
	public int totalTime;
	
	public static Firebase postBackRef;
	public static Firebase OGRef;
	public static boolean initpostBackRef = false;
	String encodedImage = "oops";
	String taskId;
	ArrayList<String> imgLinks;
	boolean p;
	
	int[] quantification;
	double[] data;
	BufferedImage img, rawStart;
	String type;

	public Task(DataSnapshot snapshot){
		startTime = System.currentTimeMillis();
		
		SMain.serverStatus.child("serverstatus").setValue("Processing");
		taskId = snapshot.getKey();
		
		imgLinks = new ArrayList<String>();
		type = snapshot.child("type").getValue().toString();
		
		String asdf = snapshot.child("p").getValue().toString();
		if(asdf.equals("yes")){
			p = true;
		}
		else{
			p = false;
		}
		
		for(DataSnapshot child: snapshot.child("src").getChildren()){
			imgLinks.add(child.getValue().toString());
		}
		
		try{
			BufferedImage[] igs;
			if(type.equals("aviquick") && imgLinks.size() > 0){
				System.out.println("avi stitching");
				FileManager.init();
				igs = FileManager.getStitchingAndProcessed(imgLinks.get(0));
				img = igs[0];
				rawStart = igs[1];
			}else if(type.equals("jpg") && imgLinks.size() >1){
				System.out.println("jpg stitching multipe");
				FileManager.init();
				igs = FileManager.getStitchingAndProcessedImges(imgLinks);
				img = igs[0];
				rawStart = igs[1];
			}else if(type.equals("jpg") && imgLinks.size() == 1){
				System.out.println("jpg stitching single");
				FileManager.init();
				igs = FileManager.getStitchingAndProcessedImges(imgLinks.get(0));
				img = igs[0];
				rawStart = igs[1];
			}
		}catch(Exception e){}
			
			
	
		
		
		execute();
		totalTime = (int) ((System.currentTimeMillis()-startTime)/1000);
		postBack();
	}

	private void execute() {
		TaskHelper th = new TaskHelper(img, rawStart);
		data = th.getData();
		TaskHelper.handleAverage(data[0], data[1], data[2], data[3]);
		encodedImage = th.getImage(p);
		System.out.println("encodedImage: " + encodedImage);
		
//		// TODO Auto-generated method stub
//		JFrame frame = new JFrame();
//		frame.getContentPane().setLayout(new FlowLayout());
//		frame.getContentPane().add(new JLabel(new ImageIcon(img)));
//		frame.pack();
//		frame.setVisible(true);
	}

	/*
	 *Base64 encodes image and puts it back on firebase
	 *removes task from firebase
	 */
	private void postBack() {
		if(!initpostBackRef){
			postBackRef = new Firebase("https://opencv.firebaseio.com/completion");
			OGRef = new Firebase("https://opencv.firebaseio.com/");
			initpostBackRef = true;
		}
//		if(!taskId.equals("testing"))
//			OGRef.child("tasks").child(taskId).removeValue();
		
		postBackRef.child(taskId).child("tortuosity").setValue(data[0]);
		postBackRef.child(taskId).child("length").setValue(data[1]);
		postBackRef.child(taskId).child("density").setValue(data[2]);
		postBackRef.child(taskId).child("CNBD").setValue(data[3]);
		
		postBackRef.child(taskId).child("time").setValue(totalTime);
		postBackRef.child(taskId).child("imagearea").setValue(img.getWidth()*img.getHeight()*Constants.MMSizeOfPixel * Constants.MMSizeOfPixel);

		
		postBackRef.child(taskId).child("src").child("0").setValue(encodedImage);
		
		SMain.serverStatus.child("serverstatus").setValue("online");
	}
}
