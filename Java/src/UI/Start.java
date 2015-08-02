package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Start extends View implements ActionListener{
	JButton cont;
	JButton terms;
	public Start(){
		this.setBackground(Color.green);
		JLabel l = new JLabel("<html><h1 style = \"color:green\">Welcome to Yash's Image stitching Extravaganza</h1></html>");
		this.add(l);
		
		terms = new JButton("about");
		terms.setBackground(new Color( 172,0,194));
		terms.setOpaque(true);
		terms.setBorderPainted(false);
		terms.addActionListener(this);
		this.add(terms);
		
		cont = new JButton("continue!");
		cont.setBackground(new Color( 255,255,60));
		cont.setOpaque(true);
		cont.setBorderPainted(false);
		cont.addActionListener(this);
		this.add(cont);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cont){
			Main.setView(Main.FILE_TO);
		}
		else{
			JFrame s = new JFrame();
			s.setSize(500, 700);
			JPanel holder = new JPanel();
			
			holder.add(new JLabel("YASH said let there be stitching, and there was stitching"));
			
			
			s.setContentPane(new JScrollPane(holder));
			
			s.setVisible(true);
		}
	}	
}
