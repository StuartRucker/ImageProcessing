package Util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;


public class PictureLoader {
	
	//loads an array of Color objects
	public static Color[][] loadColor(String name){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		
		return loadColor(img);
	}
	
	
	//loads and array of int 0-765
	public static int[][] loadInt(String name){
		Color[][] c = loadColor(name);
		int[][] a = new int[c.length][c[0].length];
		for(int x = 0; x < c.length; x ++){
			for(int y = 0; y < c[0].length; y ++){	
				a[x][y] = c[x][y].getBlue() + c[x][y].getGreen() + c[x][y].getRed();
			}
		}
		return a;
	}
	
	public static int[][] loadInt(BufferedImage name){
		Color[][] c = loadColor(name);
		int[][] a = new int[c.length][c[0].length];
		for(int x = 0; x < c.length; x ++){
			for(int y = 0; y < c[0].length; y ++){	
				a[x][y] = c[x][y].getBlue() + c[x][y].getGreen() + c[x][y].getRed();
			}
		}
		return a;
	}
	
	private static Color[][] loadColor(BufferedImage img) {
		Color[][] colors = new Color[img.getWidth()][img.getHeight()];
		
		for(int x = 0; x < colors.length; x ++){
			for(int y = 0; y < colors[0].length; y ++){
				Color c = new Color(img.getRGB(x, y), true);
			    colors[x][y] = c;
			   
			    
			   
			    
			 
			}
		}
		return colors;
	}
	
	public static BufferedImage convertRenderedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage)img;	
		}	
		ColorModel cm = img.getColorModel();
		int width = img.getWidth();
		int height = img.getHeight();
		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		Hashtable properties = new Hashtable();
		String[] keys = img.getPropertyNames();
		if (keys!=null) {
			for (int i = 0; i < keys.length; i++) {
				properties.put(keys[i], img.getProperty(keys[i]));
			}
		}
		BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
		img.copyData(raster);
		return result;
	}

	public static BufferedImage loadImg(String name){
		try {
		    return ImageIO.read(new File(name));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static BufferedImage getPic(int[][] s){
		BufferedImage asdf = new BufferedImage(s.length, s[0].length,BufferedImage.TYPE_INT_RGB);
		

			for(int x = 0; x < s.length; x ++){
				for(int y = 0; y < s[0].length; y ++){
					int c = s[x][y]/3;
					if(c > 255) c = 255;
					if(c <0) c= 0;
					asdf.setRGB(x, y, new Color(c,c,c).getRGB());
				}
			}
		return asdf;
	}


	
	public static BufferedImage getPic(Color[][] overlay) {
		BufferedImage asdf = new BufferedImage(overlay.length, overlay[0].length,BufferedImage.TYPE_INT_RGB);
	
		for(int x = 0; x < overlay.length; x ++){
			for(int y = 0; y < overlay[0].length; y ++){

				asdf.setRGB(x, y, overlay[x][y].getRGB());
			}
		}
	return asdf;
	}
}
