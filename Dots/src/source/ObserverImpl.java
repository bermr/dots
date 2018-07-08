package source;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
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
		
		/*new Thread(() ->{
			try {
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();*/
		
		try {
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException {
		new ObserverImpl();
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

	private void messageHandler(Message msg) {
		ArrayList<Dot> dots1 = msg.getDots(); 
		draw(dots1);
		if (msg.getValue().equals("close")) { 
			this.isOpen = false;
			System.out.println("end");
		}
		
	}

	public void draw(ArrayList<Dot> dots){
		for(Dot d : dots){
			panel.addPoint(d.getX(), d.getY(), d.getColor());
		}
		panel.repaint();
	}
}
