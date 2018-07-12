package source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MasterSubject {
	private ServerSocket server;
	private List<Socket> subjectList;
	private Map<String, ArrayList<String>> controlList;
	public boolean stop = false;
	public int subNumber = 2;
	public int obsNumber;
	
	private Long t1;
	private Long t2=(long) 0;
	
	public MasterSubject() throws IOException, ClassNotFoundException {
		controlList = new HashMap<String, ArrayList<String>>();
		subjectList = new ArrayList<Socket>();
		server = new ServerSocket(1234);
		System.out.println("Master aberto");
		for (int i=0; i<subNumber; i++) {
			registerSubject(server.accept());
			System.out.println("Conectado");
		}
		waitObservers();
		new Thread(() ->{
			try {
				do{
					registerSubject(server.accept());
					System.out.println("Conectado");
				} while(!stop);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		start();
	}
	
	public void waitObservers() throws IOException, ClassNotFoundException {
		Message msg = new Message();	
		ObjectInputStream in = new ObjectInputStream(subjectList.get(0).getInputStream());
		msg = (Message) in.readObject();
		
		controlList.put(subjectList.get(0).getInetAddress().toString(), msg.getIps());
		//System.out.println(controlList.containsKey(subjectList.get(0).getInetAddress().toString()));
		/*for (int j=0; j<msg.getIps().size();j++) {
			System.out.println(controlList.get(subjectList.get(0).getInetAddress().toString()).get(j));
		}*/
		
		ObjectInputStream in2 = new ObjectInputStream(subjectList.get(1).getInputStream());
		msg = (Message) in2.readObject();	
		controlList.put(subjectList.get(1).getInetAddress().toString(), msg.getIps());
		
	}
	
	public void start() throws IOException {
		ArrayList<Dot> dots = new ArrayList<Dot>();
		Random r1 = new Random();
		long start = System.nanoTime();
		do {
			t1 = System.currentTimeMillis();
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
			if (((System.nanoTime() - start)/1000000000) > 30)
				this.stop = true;
			write(dots);
			t2 = System.currentTimeMillis();
			System.out.println((((double) t2.longValue()) - t1.longValue())/1000 + "s");
		} while (!stop);
	}
	
	private void write(ArrayList<Dot> dots) throws IOException {
		int var = -1;
		ArrayList<String> aux = new ArrayList<String>();
		for (int i=0 ; i<subjectList.size(); i++) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(subjectList.get(i).getOutputStream());
				Message msg = new Message(dots);
				msg.setValue("nothing");
				if (this.stop == true)
					msg.setValue("close");
				out.writeObject(msg);
				out.flush();
			} catch(IOException e) {
				ArrayList<String> ipsRealloc = controlList.get(subjectList.get(i).getInetAddress().toString());
				//subjectList.remove(sub);
				ArrayList<String> ipsRealloc2 = new ArrayList<String>();
				if (i == 0) {
					ipsRealloc2 = controlList.get(subjectList.get(1).getInetAddress().toString());
				}
				else {
					ipsRealloc2 = controlList.get(subjectList.get(0).getInetAddress().toString());
				}
				
				for (String s : ipsRealloc) {
					aux.add(s);
				}
				for (String s : ipsRealloc2) {
					aux.add(s);
				}

				Map<String, ArrayList<String>> controlList2 = new HashMap<String, ArrayList<String>>();
				if (i == 0) {
					controlList2.put(subjectList.get(1).getInetAddress().toString(), aux);
					this.controlList = controlList2;
					
				}
				else {
					controlList2.put(subjectList.get(0).getInetAddress().toString(), aux);
					this.controlList = controlList2;
				}
				var = i;
				break;
			}
		}
		if (var != -1) {
			subjectList.remove(var);
			//syncObservers(var2, aux);
		}
	}

	private void syncObservers(int k, List<String> aux) {
		for (int i= 0; i<aux.size()-1;i++) {
			try {
				ServerSocket soc = new ServerSocket(1236);		
				Socket s = soc.accept();
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				Message msg = new Message();
				msg.setValue("reconectar");
				msg.setIp("192.168.0.232");
				msg.setPort(1238);
				out.writeObject(msg);
				out.flush();
				soc.close();
			} catch (Exception e) {
				System.out.println("nao enviou");
			}
		}
	}

	public static void main(String agrs[]) throws IOException, ClassNotFoundException {
		new MasterSubject();
	}
	
	public void registerSubject(Socket s) {
		subjectList.add(s);
		//System.out.println(s.getInetAddress().toString() +" " + s.getRemoteSocketAddress().toString());
		//controlList.put(s.getInetAddress().toString(), s.getRemoteSocketAddress().toString());
	}
}
