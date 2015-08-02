package UI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static final int FILE_TO = 0;
	public static final int FILE_DESINATION = 1;
	public static final int EDIT = 2;
	public static final int START = 3;
	
	private static  View ft;
	private static View fd;
	private static  View ed;
	private static  View st;
	
	private static View current;
	private static JFrame f;
	private static int view = EDIT;	
	public static void main(String[] args){
		//init Jpanels
		ft = new FileTo();
		fd = new FileDestination();
		ed = new Edit();
		st = new Start();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		
		f = new JFrame("Eye detection");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setView(view);
		f.setSize(width-100,height-200);
		f.setVisible(true);
		
	}

	public static void setView(int viewType){
		view = viewType;   
	   	 ft.set(false);	
	   	 fd.set(false);
	   	 ed.set(false);
	   	 st.set(false);
		switch (view) {
	         case FILE_TO:
	        	 current = ft;
	        	 ft.set(true);	
	        	 break;
	         case FILE_DESINATION:
	        	 current = fd;
	        	 fd.set(true);
	        	 break;
	         case EDIT:
	        	 current = ed;
	        	 ed.set(true);
	             break;
	         case START:
	        	 current = st;
	        	 st.set(true);
	             break;
	       
	         default:
	        	 System.err.println("invalid view");
	        	 break;
		}
		f.setContentPane(current);
	}
		
}
