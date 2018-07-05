package source;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class ObserverImpl implements Observer{
	private Panel panel;
	private boolean isOpen = true;
	public ObserverImpl() {
		Frame points = new Frame();
		panel = new Panel();
		points.setLayout(new BorderLayout()); 
		points.add(panel, BorderLayout.CENTER);
		points.setSize(500, 500);
		points.setVisible(true);
		
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		new ObserverImpl();
	}
	
	public void start() throws IOException {
		DatagramPacket packet = null;
		DatagramSocket socket = new DatagramSocket(7070);
		socket.setBroadcast(true);

		while(this.isOpen ){
			byte[] buf = new byte[socket.getReceiveBufferSize()];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			
			ObjectInputStream iStream;
			iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));

			new Thread(()->{
				try {
					Object[] msg = (Object []) iStream.readObject();
					messageHandler(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		socket.close();
	}

	private void messageHandler(Object[] msg) {
		ArrayList<Dot> dots1 = (ArrayList<Dot>) msg[1]; 
		draw(dots1);
	}

	public void draw(ArrayList<Dot> dots){
		for(Dot d : dots){
			panel.addPoint(d.getX(), d.getY(), d.getColor());
		}
		panel.repaint();
	}
}
