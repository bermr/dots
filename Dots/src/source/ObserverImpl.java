package source;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ObserverImpl implements Observer{
	private Panel panel;
	private boolean isOpen = true;
	private Socket subject;
	
	public ObserverImpl() throws UnknownHostException, IOException {
		Frame points = new Frame();
		panel = new Panel();
		points.add(panel, BorderLayout.CENTER);
				
		start();
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException {
		ObserverImpl obs = new ObserverImpl();
	}
	
	public void start() throws IOException {
		subject = new Socket("127.0.0.1", 1234);
		Message msg = new Message();
		
		do {
			try{
				ObjectInputStream in = new ObjectInputStream(subject.getInputStream());
				msg = (Message) in.readObject();	
				messageHandler(msg);
				}catch(Exception e){
				e.printStackTrace();;
			}
		} while(isOpen);
	}

	@SuppressWarnings("unchecked")
	private void messageHandler(Message msg) {
		ArrayList<Dot> dots1 = msg.getDots(); 
		draw(dots1);
	}

	public void draw(ArrayList<Dot> dots){
		for(Dot d : dots){
			panel.addPoint(d.getX(), d.getY(), d.getColor());
		}
		panel.repaint();
	}
}
