package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JOptions extends JPanel{
	Handler h;
	JLabel tortuosity,length,density;
	JPanel starting, editing;
	JButton green;
	JButton red;
	JButton clear;
	JButton backToStart;
	
	
	public JOptions(Handler h1){
		this.h = h1;
		System.out.println(h ==null);
		this.setBackground(Color.white);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
		green = new JButton("+");
		red = new JButton("-");
		clear = new JButton("clear");
		green.setBackground(Color.GREEN);
		green.setOpaque(true);
		red.setBackground(Color.RED);
		red.setOpaque(true);

		
		//start9
		tortuosity = new JLabel();
		density = new JLabel();
		length = new JLabel();
		
		for(int i  = 0; i < 3; i ++){this.add(new JLabel(" "));}
		this.add(tortuosity);
		this.add(density);
		this.add(length);
		
		
		for(int i  = 0; i < 4; i ++){this.add(new JLabel(" "));}
		
		this.add(red);
		this.add(green);
		this.add(clear);
		
		setAttr();
		
		
		backToStart = new JButton("Back to Start");
		backToStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               Main.setView(Main.START);
            }});
		
		for(int i  = 0; i < 4; i ++){this.add(new JLabel(" "));}
		this.add(backToStart);
	}
	
	public void setAttr(){
		tortuosity.setText("	tortuosity: " + h.getTortuosity());
		length.setText("	length: " + h.getLength());
		density.setText("	density: " + h.getDensity());
	}
	
}
