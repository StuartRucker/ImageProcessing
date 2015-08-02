package UI;

import javax.swing.JPanel;
/*
 * All of the different UI views
 */
public abstract class View extends JPanel{
	public void set(boolean on){
		if(on)this.setVisible(true);
		else this.setVisible(false);
	} 
}
