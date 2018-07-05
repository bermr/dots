package source;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Random;

public class ObserverImpl implements Observer{
	private ArrayList<Dot> dots;
	private Panel panel;
	public ObserverImpl() {
		dots = new ArrayList<Dot>();
		Frame points = new Frame();
		panel = new Panel();
		points.setLayout(new BorderLayout()); 
		points.add(panel, BorderLayout.CENTER);
		points.setSize(500, 500);
		points.setVisible(true);
		
		start();
	}
	
	public static void main(String args[]) {
		new ObserverImpl();
	}
	
	private void start() {
		Random r1 = new Random();
		while(true) {
			for(int i=0;i<1000;i++) {
				int[] rgb = {r1.nextInt(255),r1.nextInt(255),r1.nextInt(255)};
				Dot d = new Dot(r1.nextInt(1000),r1.nextInt(1000), rgb);
				dots.add(d);
			}
			draw(dots);
		}
	}

	public void draw(ArrayList<Dot> dots){
		for(Dot d : dots){
			panel.addPoint(d.getX(), d.getY(), d.getColor());
		}
		panel.repaint();
	}
}
