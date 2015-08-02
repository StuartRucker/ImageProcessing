package Util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class FileManager {
	public static String FILE_PATH;
	public static String RESOURCE_PATH;
	public static String EXE_PATH;
	
	
	
	public static  BufferedImage[] getStitchingAndProcessed(String aviURL){
		BufferedImage imgHalfway = null;
		BufferedImage img = null;
		try{
			
			//Step 1: save the AVI file locally from the URL
			URL website = new URL(aviURL);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(new File(RESOURCE_PATH+"/video.avi"));
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			
			//step2: Get the save the stitched image from the exec to a local file path
			String cmd[] = {EXE_PATH + "/videoStitcher", RESOURCE_PATH + "/video.avi", RESOURCE_PATH,RESOURCE_PATH + "/framesplit"};			
			Cplusplus.invoke(cmd);

			//step3: get the stitched image and process it
			String cmd2[] = {EXE_PATH + "/Edge", RESOURCE_PATH + "/stitchimg.jpg", RESOURCE_PATH };
			
			Cplusplus.invoke(cmd2);
			
			
			//step4: Read the bufferedIMage from the folder
			System.out.println(RESOURCE_PATH);
			imgHalfway = ImageIO.read(new File(RESOURCE_PATH + "/stitchimg.jpg"));
			img = ImageIO.read(new File(RESOURCE_PATH + "/edge.jpg"));
		
			
			//step 5: clean the resource folder
			 File res = new File(RESOURCE_PATH);
			 FileUtils.cleanDirectory(res);
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		BufferedImage returnMe[] = {img, imgHalfway};
		return returnMe;
	}
	
	/*
	 * Creates a file with permissions
	 */
	public static void init(){
		try{
			System.out.println("starting file init");
			String resource = FileManager.class.getName().replace(".", File.separator) + ".class";      
		    URL fileURL = ClassLoader.getSystemClassLoader().getResource(resource);
	
		    String path =  new File(".").getCanonicalPath();
		   
		    int index = path.length();
		    loop: for(int i = 0; i < 0; i ++){
		    	index = path.lastIndexOf("/", index-1);
		    	if(index == -1)break loop;
		    }
		    path = path.substring(0,index);
		    
		    System.out.println(path);
		    
		    String mySubFolder = "yashstitching";
		    String directory  = path + File.separator + mySubFolder;
		    
		    System.out.println("parthL " + directory);
		    FILE_PATH = directory;
		    RESOURCE_PATH = FILE_PATH + "/resources";
		    EXE_PATH = FILE_PATH + "/exe";
		   
		    //File newDir = new File(directory);
		    File master = new File(FILE_PATH);
		    if(!master.exists()){
		    	master.mkdir();
		    }
		    File res = new File(RESOURCE_PATH);
		    if(!res.exists())res.mkdir();
		    FileUtils.cleanDirectory(res);
		    
		    File exe = new File(EXE_PATH);
		    if(!exe.exists()){
		    	 System.err.println("Error: could not find the exe folder");
		    }
		    
		    File  frameSplit = new File(RESOURCE_PATH + "/framesplit");
		    if(!frameSplit.exists())frameSplit.mkdir();
		    FileUtils.cleanDirectory(frameSplit);
		    
		   // newDir.mkdir();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	/*
	 * Stitching multiple images
	 */
	public static BufferedImage[] getStitchingAndProcessedImges( ArrayList<String> imgLinks) {
		BufferedImage imgHalfway = null;
		BufferedImage img = null;
		try{
			System.out.println("trying to do the array");
			File savemages = new File(RESOURCE_PATH + "/preimgstitch");
			savemages.mkdir();
			
			int cnt = 1;
			for(String link: imgLinks){
				System.out.println("attempting: " + link);
				URL website = new URL(link);
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				String pathToWrite = RESOURCE_PATH+"/preimgstitch/img" + (cnt++) + ".jpg";
				FileOutputStream fos = new FileOutputStream(new File(pathToWrite));
				System.out.println(pathToWrite);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				
			}
			
			//step2: Get the save the stitched image from the exec to a local file path
			String cmd[] = {EXE_PATH + "/imgstitch",RESOURCE_PATH+"/preimgstitch", Integer.toString(--cnt),RESOURCE_PATH };			
			System.out.println("args: ************");
			for(String args: cmd){
				System.out.println(args);
			}
			System.out.println("end: ************");
			Cplusplus.invoke(cmd);

			//step3: get the stitched image and process it
			String cmd2[] = {EXE_PATH + "/Edge", RESOURCE_PATH + "/stitchimg.jpg", RESOURCE_PATH };
			Cplusplus.invoke(cmd2);
//			
////			
			//step4: Read the bufferedIMage from the folder
			System.out.println(RESOURCE_PATH);
			imgHalfway = ImageIO.read(new File(RESOURCE_PATH + "/stitchimg.jpg"));
			img = ImageIO.read(new File(RESOURCE_PATH + "/edge.jpg"));
		
//			
//			//step 5: clean the resource folder
//			 File res = new File(RESOURCE_PATH);
//			 FileUtils.cleanDirectory(res);
			
			
		}catch(Exception e){
			
		}
		BufferedImage returnMe[] = {img, imgHalfway};
		return returnMe;
	}
	
	
	/*Used for a single image*/
	public static BufferedImage[] getStitchingAndProcessedImges(String link) {
		BufferedImage imgHalfway=null,img=null;
		try{
			
			System.out.println("single image");
		//Step 1: save the AVI file locally from the URL
			URL website = new URL(link);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(new File(RESOURCE_PATH+"/picture.jpg"));
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			
			String cmd2[] = {EXE_PATH + "/Edge", RESOURCE_PATH + "/picture.jpg", RESOURCE_PATH };
			Cplusplus.invoke(cmd2);
			
			//step4: Read the bufferedIMage from the folder
			System.out.println(RESOURCE_PATH);
			imgHalfway = ImageIO.read(new File(RESOURCE_PATH + "/picture.jpg"));
			img = ImageIO.read(new File(RESOURCE_PATH + "/edge.jpg"));
			
			System.out.println("done");
//			step 5: clean the resource folder
			 File res = new File(RESOURCE_PATH);
			 FileUtils.cleanDirectory(res);
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		BufferedImage returnMe[] = {img, imgHalfway};
		return returnMe;
	}

}
