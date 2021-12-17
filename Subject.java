import java.awt.Color;
public interface Subject {
    public void attach(Observer o); 
    public void detach(Observer o); 
    public void notifyObservers(int x, int y, Color color);
}
