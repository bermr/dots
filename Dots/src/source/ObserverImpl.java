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
	private String ip;
	private int port;
	private int frame;

	private Long t1;
	private Long t2=(long) 0;
	private double soma;

	public ObserverImpl() throws UnknownHostException, IOException {
		Frame points = new Frame();
		panel = new Panel();
		points.add(panel, BorderLayout.CENTER);
		this.ip = "127.0.0.1";
		this.port = 1238;
		this.frame = 0;

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
		int c =0;
		long start = System.nanoTime();
		Message msg = new Message();
		try {
			subject = new Socket(this.ip, this.port);
			ObjectInputStream in = null;
			do {
				System.out.println("                         frame: " + frame);
				try{
					in = new ObjectInputStream(subject.getInputStream());
					msg = (Message) in.readObject();
					subject.setSoTimeout(2500);
					messageHandler(msg);
					this.frame++;
					c++;
					}catch(Exception e){
						//wait(2000);
						//Socket soc = new Socket("127.0.0.1", 1236);
						//ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
						//msg = (Message) in.readObject();
						//soc.close();
						//messageHandler(msg);
						in.close();
						subject.close();
						//System.out.println("veio aqui");
						subject = new Socket("192.168.0.232", 1238);
					}
					System.out.println(soma/c);
			} while(isOpen);
		}catch(Exception e) {

		}
	}

	private void messageHandler(Message msg) {
		ArrayList<Dot> dots1 = msg.getDots();
		t1 = System.currentTimeMillis();
		draw(dots1);
		t2 = System.currentTimeMillis();
		soma += ((((double) t2.longValue()) - t1.longValue())/1000);
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
