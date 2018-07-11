package source;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ObserverImpl implements Observer{
	private Panel panel;
	private boolean isOpen = true;
	private Socket subject;
	private String ip;
	private int port;
	
	public ObserverImpl() throws UnknownHostException, IOException {
		Frame points = new Frame();
		panel = new Panel();
		points.add(panel, BorderLayout.CENTER);
		this.ip = "192.168.0.243";
		this.port = 1235;
		
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
		Message msg = new Message();
		try {
			subject = new Socket(this.ip, this.port);
			do {
				try{
					//subject.setSoTimeout(1500);
					ObjectInputStream in = new ObjectInputStream(subject.getInputStream());
					msg = (Message) in.readObject();	
					messageHandler(msg);
					}catch(Exception e){
						//wait(2000);
						subject.close();
						//Socket soc = new Socket("127.0.0.1", 1236);
						//ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
						//msg = (Message) in.readObject();
						//soc.close();
						//messageHandler(msg);
						subject = new Socket("192.168.0.232", 1238);
					}
			} while(isOpen);
		}catch(Exception e) {
			//se cair conecta dnv
		}
	}
	
	private void messageHandler(Message msg) {
		ArrayList<Dot> dots1 = msg.getDots(); 
		draw(dots1);
		if (msg.getValue().equals("close")) { 
			this.isOpen = false;
			System.out.println("end");
		}
		if (msg.getValue().equals("reconectar")) {
			this.ip = msg.getIp();
			this.port = msg.getPort();
		}
	}

	public void draw(ArrayList<Dot> dots){
		for(Dot d : dots){
			panel.addPoint(d.getX(), d.getY(), d.getColor());
		}
		panel.repaint();
	}
}
