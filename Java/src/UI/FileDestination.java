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
 * Class used to select a folder to save the image to
 */
public class FileDestination extends View implements ActionListener{
	JFileChooser mfc;
	
	public FileDestination(){
		this.setLayout( new BorderLayout());
		
		JLabel lab = new JLabel("<html><h1>Select a folder to save the stiched image</h1></html>");
		lab.setAlignmentY(Component.TOP_ALIGNMENT);
		this.add(lab,BorderLayout.NORTH);
		
		mfc = new JFileChooser();

		mfc.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		mfc.setAcceptAllFileFilterUsed(false);
		mfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.add(mfc,BorderLayout.SOUTH);
		
		mfc.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            System.out.println("File selected: " + mfc.getSelectedFile().getAbsolutePath());
            Main.setView(Main.EDIT);
        }else if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)){
        	Main.setView(Main.FILE_TO);
        }
	}
}
