import java.util.ArrayList;
import java.awt.Color;

public class EditorQueue implements Subject{
    // NOTE: This will act as the Subject of our Observer pattern
    private ArrayList<Observer> observers;
    //--
    public EditorQueue(){
        observers = new ArrayList < Observer >(); 
    }
    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }
    @Override
    public void attach(Observer o) {
        observers.add(o);
    }
    @Override
    public void notifyObservers(int x, int y, Color color) {
        for (Observer observer : observers) {
            observer.update(x, y, color);
        }
    }
}
