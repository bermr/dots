package source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Subject {
	private ServerSocket server;
	private List<Socket> observerList;
	private Socket master;
	boolean stop = false;
	
	public Subject() throws IOException {
		observerList = new ArrayList<Socket>();
		master = new Socket("127.0.0.1", 1234);
		waitRegistration();
		start();
	}
	
	public static void main(String args[]) throws IOException {
		new Subject();
	}
	
	private void waitRegistration() throws IOException {
		server = new ServerSocket(1235);
		System.out.println("Subject aberto");
		registerObserver(server.accept());
		System.out.println("Conectado");
		notifyMaster();
		
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
	
	public void notifyMaster() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(master.getOutputStream());
		Message msg = new Message();
		msg.setValue("nothing");
		out.writeObject(msg);
		out.flush();
	}

	public void start() throws IOException {
		Message msg = new Message();
		do {
			try{
				ObjectInputStream in = new ObjectInputStream(master.getInputStream());
				msg = (Message) in.readObject();	
				messageHandler(msg);
				}catch(Exception e){
					e.printStackTrace();;
				}
		} while(!stop);
	}
	
	private void messageHandler(Message msg) throws IOException {
		ArrayList<Dot> dots1 = msg.getDots();
		System.out.println(msg.getValue());
		notifyObservers(dots1);
		if (msg.getValue().equals("close")) { 
			this.stop = true;
			System.out.println("end");
		}
		
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

