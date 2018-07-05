package source;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Subject {
	private List<Observer> observerList;
	
	public Subject() {
		observerList = new ArrayList<Observer>();
		start();
	}
	
	public static void Main() {
		Subject subject = new Subject();
	}
	
	public void start() {
		ArrayList<Dot> dots = new ArrayList<Dot>();
		Random r1 = new Random();
		boolean stop = false;
		do {
			for(int i=0;i<1000;i++) {
				int[] rgb = {r1.nextInt(255),r1.nextInt(255),r1.nextInt(255)};
				Dot d = new Dot(r1.nextInt(1000),r1.nextInt(1000), rgb);
				dots.add(d);
			}
		} while (!stop );
		notifyObservers(dots);
	}
	
	public void registerObserver(Observer obs) {
		observerList.add(obs);
	}
	
	public void unregisterObserver(Observer obs) {
		observerList.remove(obs);
	}
	
	public void notifyObservers(ArrayList<Dot> dots) {
		for (Observer obs : observerList) {
			obs.draw(dots);
		}
	}
	
}
