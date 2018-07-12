package source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Subject {
	private ServerSocket server;
	private List<Socket> observerList;
	private Socket master;
	private ArrayList<String> ips;

	private Long t1;
	private Long t2=(long) 0;
	private double soma;

	boolean stop = false;

	public Subject() throws IOException {
		ips = new ArrayList<String>();
		observerList = new ArrayList<Socket>();
		master = new Socket("127.0.0.1", 1234);

		waitRegistration();
		start();
	}

	public static void main(String args[]) throws IOException {
		new Subject();
	}

	private void waitRegistration() throws IOException {
		//Scanner s = new Scanner(System.in);
		//int port = s.nextInt();
		String ip;
		server = new ServerSocket(1238);
		System.out.println("Subject aberto");
		for (int i=0 ; i< 1 ; i++) {
			registerObserver(server.accept());
			System.out.println("Conectado");
			ip = observerList.get(i).getInetAddress().toString();
			ips.add(ip);
		}
		notifyMaster();
		new Thread(() ->{
			try {
				do{
					registerObserver(server.accept());
					System.out.println("Conectado");
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
		msg.setIps(ips);
		out.writeObject(msg);
		out.flush();
	}

	public void start() throws IOException {
		Message msg = new Message();
		int c = 0;
		do {
			t1 = System.currentTimeMillis();
			try{
				ObjectInputStream in = new ObjectInputStream(master.getInputStream());
				msg = (Message) in.readObject();
				messageHandler(msg);
				//System.out.println(observerList.size());
				}catch(Exception e){
					//System.out.println("eRRO");
				}
			t2 = System.currentTimeMillis();
			soma += ((((double) t2.longValue()) - t1.longValue())/1000);
			c++;
			System.out.println(soma/c);
		} while(!stop);
	}

	private void messageHandler(Message msg) throws IOException {
		ArrayList<Dot> dots1 = msg.getDots();
		notifyObservers(dots1);
		if (msg.getValue().equals("close")) {
			this.stop = true;
			System.out.println("end");
		}

	}

	public synchronized void registerObserver(Socket obs) throws IOException {
		obs.setSoTimeout(2500);
		observerList.add(obs);
	}

	public void unregisterObserver(Socket obs) {
		observerList.remove(obs);
	}

	public synchronized void notifyObservers(ArrayList<Dot> dots) throws IOException {
		for (Socket obs : observerList) {
			//System.out.println(obs.getInetAddress().toString());
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

