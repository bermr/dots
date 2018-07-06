package source;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private final BufferedImage image = new BufferedImage(1280, 768, BufferedImage.TYPE_INT_RGB);
	
	public Panel() {
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 10, 10, this);
	}
	
	public void addPoint(int x, int y, int[] rgb) {
		Graphics g = image.getGraphics();
		g.setColor(new Color(rgb[0],rgb[1],rgb[2]));
		g.drawLine(x, y, x, y);
		g.dispose();
	}
	
	public void removePoint(int x, int y){
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillOval(x, y, 6, 6);
		g.dispose();
	}

}
