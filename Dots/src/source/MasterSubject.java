package source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MasterSubject {
	private ServerSocket server;
	private List<Socket> subjectList;
	public boolean stop = false;
	public int subNumber = 1;
	public int obsNumber;
	
	public MasterSubject() throws IOException, ClassNotFoundException {
		subjectList = new ArrayList<Socket>();
		server = new ServerSocket(1234);
		System.out.println("Master aberto");
		for (int i=0; i<subNumber; i++) {
			registerSubject(server.accept());
			System.out.println("Conectado");
		}
		waitObservers();
		start();
	}
	
	public void waitObservers() throws IOException, ClassNotFoundException {
		Message msg = new Message();	
		ObjectInputStream in = new ObjectInputStream(subjectList.get(0).getInputStream());
		msg = (Message) in.readObject();	
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
			write(dots);
		} while (!stop);
	}
	
	private void write(ArrayList<Dot> dots) throws IOException {
		for (Socket sub : subjectList) {
			ObjectOutputStream out = new ObjectOutputStream(sub.getOutputStream());
			Message msg = new Message(dots);
			msg.setValue("nothing");
			if (this.stop == true)
				msg.setValue("close");
			out.writeObject(msg);
			out.flush();
		}
		
	}

	public static void main(String agrs[]) throws IOException, ClassNotFoundException {
		new MasterSubject();
	}
	
	public void registerSubject(Socket s) {
		subjectList.add(s);
	}
}
