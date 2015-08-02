package UI;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

import threeD.Sphere;
import Storage.Constants;
import Storage.Image;
import Util.FileManager;
import Util.PictureLoader;


public class Handler {
	public JOptions jo;
	public JDisplay jd;
	private ArrayList<DisplayImage> images;
	private Image latest;
	int[][][] pic3D;
	
 	public Handler(JOptions jo1, JDisplay jd1){
		this.jo = jo1;
		this.jd = jd1;	
		images = new ArrayList<DisplayImage>();
		
		
		//chage to loading a jpg file loadImage
	}
	
	public void loadImage(){
		//replace with getting image from exe file

		//load dat image
		FileManager.init();
		BufferedImage proc =  FileManager.getStitchingAndProcessedImges("http://i.imgur.com/zy6JH4l.jpg")[0];
		latest = new Image(PictureLoader.loadInt( FileManager.getStitchingAndProcessedImges("http://i.imgur.com/zy6JH4l.jpg")[0]));
		
//		Sphere sp = new Sphere();
//		sp.start(latest.nc.roots, latest.colors);
//		
		
		BufferedImage img = PictureLoader.getPic(latest.colors);
		images.add(new DisplayImage(20,20, img, latest));
		
		
		
	}
	
	public ArrayList<DisplayImage> getImages(){
		return images;
	}
	
public double getTortuosity(){
		return (latest == null)? 0: latest.getTortuosity();
	}
	public double getDensity(){
		return (latest == null)? 0:latest.getDensity();
	}
	public double getLength(){
		return (latest == null)? 0: latest.getLength();
	}
	public double getTotalLength(){
		//TODO
		return -1;
	}
	
}
