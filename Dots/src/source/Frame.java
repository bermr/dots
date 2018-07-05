package source;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public Frame() {
		this.setLayout(new BorderLayout()); 
		this.setSize(800, 600);
		this.setVisible(true);
	}
	
}