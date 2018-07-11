package source;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String value;
	private String ip;
	private ArrayList<Dot> dots;
	private ArrayList<String> ips;
	private int port;
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Message(String v) {
		this.value = v;
		this.ips = new ArrayList<String>();
	}
	
	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}
	
	public void addIp(String ip) {
		this.ips.add(ip);
	}
	
	public ArrayList<String> getIps() {
		return this.ips;
	}
	
	public Message(ArrayList<Dot> dots) {
		this.dots = dots;
	}
	
	public Message(String v, String ip){
		this.value = v;
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Message(){
	}

	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public ArrayList<Dot> getDots(){
		return this.dots;
	}

}
