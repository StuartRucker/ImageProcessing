package UI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Deals with the UI elements of the image
 * i.e. screen coordinates, overlays
 */
public class JDisplay extends JPanel{
	Handler h;
	public JDisplay(Handler h1){
		this.setBackground(Color.black);
		this.h = h1;
	}
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);
        g.setColor(Color.white);
        for(DisplayImage di: h.getImages()){
        	g.drawImage(di.img,di.x,di.y,this);     
        	
            for(int[] point :  di.getOverlay()){
            	for(int i = 2; i < point.length; i ++){
            		point[i] = (point[i] < 0)? 0: point[i];
            		point[i] = (point[i] > 255)? 255: point[i];
            	}
            	g.setColor(new Color(point[2],point[3],point[4]));
            	g.fillRect(di.x + point[0], di.y + point[1], 1,1);
            }
        }
       

   
    }
    
    
}
