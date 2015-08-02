package UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

public class Edit  extends View{
	JOptions jo;
	Handler h;
	JDisplay jd;
	public Edit(){
		
		this.setLayout(new BorderLayout());
		h = new Handler(jo,jd);
		
		System.out.println(h ==null);
		
		jo = new JOptions(h);
		jd = new JDisplay(h);
		

		
		this.add(jo, BorderLayout.LINE_START);
		this.add(jd, BorderLayout.CENTER);		
	}
	
	@Override
	public void set(boolean b){
		super.set(b);
		if(b){
			h.loadImage();
			jo.setAttr();
		}
	}
}
