package source;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Subject {
	private ServerSocket server;
	private List<Socket> observerList;
	boolean stop = false;
	
	public Subject() throws IOException {
		observerList = new ArrayList<Socket>();
		waitRegistration();
		start();
	}
	
	private void waitRegistration() throws IOException {
		server = new ServerSocket(1234);
		System.out.println("Porta 1324 aberta!");
		registerObserver(server.accept());
		
		new Thread(() ->{
			try {
				do{
					registerObserver(server.accept());
				} while(!stop);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String args[]) throws IOException {
		new Subject();
	}
	
	public void start() throws IOException {
		ArrayList<Dot> dots = new ArrayList<Dot>();
		Random r1 = new Random();
		long start = System.nanoTime();
		do {
			for(int i=0;i<1000;i++) {
				if (r1.nextInt(100) < 20) {
					int[] rgb = {r1.nextInt(255),r1.nextInt(255),r1.nextInt(255)};
					Dot d = new Dot(r1.nextInt(1000),r1.nextInt(1000), rgb);
					dots.add(d);
				}
				else {
					int[] rgbb = {0,0,0}; 
					Dot d = new Dot(r1.nextInt(1000),r1.nextInt(1000), rgbb);
					dots.add(d);
				}
				
			}
			if (((System.nanoTime() - start)/1000000000) > 60)
				this.stop = true;
			notifyObservers(dots);
		} while (!stop);
	}
	
	public synchronized void registerObserver(Socket obs) throws IOException {
		observerList.add(obs);
	}
	
	public void unregisterObserver(Socket obs) {
		observerList.remove(obs);
	}
	
	public synchronized void notifyObservers(ArrayList<Dot> dots) throws IOException {
		for (Socket obs : observerList) {
			ObjectOutputStream out = new ObjectOutputStream(obs.getOutputStream());
			Message msg = new Message(dots);
			msg.setValue("nothing");
			if (this.stop == true)
				msg.setValue("close");
			out.writeObject(msg);
			out.flush();
		}
	}
	
}

