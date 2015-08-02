package UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Select the .avi video to be stitched
 */
public class FileTo extends View implements ActionListener{
	JFileChooser mfc;
	
	public FileTo(){
		this.setLayout( new BorderLayout());
		
		JLabel lab = new JLabel("<html><h1>Select a .avi file</h1></html>");
		lab.setAlignmentY(Component.TOP_ALIGNMENT);
		this.add(lab,BorderLayout.NORTH);
		
		mfc = new JFileChooser();
		FileNameExtensionFilter avifilter = new FileNameExtensionFilter("avi files (*.avi)", "avi");
		mfc.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		mfc.setFileFilter(avifilter);
		mfc.setAcceptAllFileFilterUsed(false);
		this.add(mfc,BorderLayout.SOUTH);
		
		mfc.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            System.out.println("File selected: " + mfc.getSelectedFile().getAbsolutePath());
            Main.setView(Main.FILE_DESINATION);
        }else if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)){
        	Main.setView(Main.START);
        }
	}
}
