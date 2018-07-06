package source;

import java.io.BufferedOutputStream;
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
	
	public Subject() throws IOException {
		observerList = new ArrayList<Socket>();
		waitRegistration();
		start();
	}
	
	private void waitRegistration() throws IOException {
		server = new ServerSocket(1234);
		System.out.println("Porta 1324 aberta!");

		for(int i = 0; i < 1; i++) {
			registerObserver(server.accept());
			System.out.println(i+1 + " conectado");
		}
	}

	public static void main(String args[]) throws IOException {
		new Subject();
	}
	
	public void start() throws IOException {
		ArrayList<Dot> dots = new ArrayList<Dot>();
		Random r1 = new Random();
		boolean stop = false;
		do {
			for(int i=0;i<1000;i++) {
				int[] rgb = {r1.nextInt(255),r1.nextInt(255),r1.nextInt(255)};
				Dot d = new Dot(r1.nextInt(1000),r1.nextInt(1000), rgb);
				dots.add(d);
			}
			notifyObservers(dots);
		} while (!stop);
		
	}
	
	public void registerObserver(Socket obs) throws IOException {
		observerList.add(obs);
	}
	
	public void unregisterObserver(Socket obs) {
		observerList.remove(obs);
	}
	
	public void notifyObservers(ArrayList<Dot> dots) throws IOException {
		for (int i=0;i<observerList.size();i++) {
			ObjectOutputStream out = new ObjectOutputStream(observerList.get(i).getOutputStream());
			Message msg = new Message(dots);
			out.writeObject(msg);
			out.flush();
		}
	}
	
}
