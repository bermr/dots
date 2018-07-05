package source;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String value;
	private String ip;
	private ArrayList<Dot> dots;

	public Message(String v) {
		this.value = v;
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
