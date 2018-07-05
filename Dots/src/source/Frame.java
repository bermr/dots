package source;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) {
		JFrame points = new JFrame();
		points.setLayout(new BorderLayout());   
		points.add(new Panel(), BorderLayout.CENTER);
		points.setSize(500, 500);
		points.setVisible(true);
		points.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	
}