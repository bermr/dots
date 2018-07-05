package source;

import java.util.ArrayList;
import java.util.List;

public class Subject {
	private List<Observer> observerList;
	
	public Subject() {
		observerList = new ArrayList<Observer>();
	}
	
	public static void Main() {
		//wait();
		//start();
	}
	
	public void registerObserver(Observer obs) {
		observerList.add(obs);
	}
	
	public void unregisterObserver(Observer obs) {
		observerList.remove(obs);
	}
	
	public void notifyObservers() {
		//envia o conjunto de dots
	}
	
}
