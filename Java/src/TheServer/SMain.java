package TheServer;

import java.util.ArrayList;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SMain {
	public static Firebase serverStatus;
	public static void main(String[] args){
		//handler server status
		serverStatus = new Firebase("https://opencv.firebaseio.com/");
		serverStatus.child("serverstatus").setValue("online");
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		    	serverStatus.child("serverstatus").setValue("offline");
		    	System.out.println("closing");
		    }
		});
		
		
		System.out.println("starting ");
		
		Firebase ref = new Firebase("https://opencv.firebaseio.com/tasks");
		
		
		ref.addChildEventListener(new ChildEventListener() {
		   
			// Retrieve new posts as they are added to the database
		    @Override
		    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
		    	try{
//		    		if(snapshot.child("type").getValue().toString().equals("avislow")){
//		    			new avislowTask(snapshot);
//		    		}else{
		    			new Task(snapshot);
//		    		}
			    	
		    	}catch(Exception e){}		

		    }
		    //... ChildEventListener also defines onChildChanged, onChildRemoved,
		    //    onChildMoved and onCanceled, covered in later sections.

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(true);
	}
}	
