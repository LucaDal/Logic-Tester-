package Components;
import java.awt.*;

public interface Component {
    final String type = "";
    String getType();
    void paint(Graphics g);
    Point getPosition();
    int getSizeWidth();
    int getSizeHeight();
    int getIDComponent();
    boolean contains(int ID);
    void setConnection(Point p);

    /**
     * given a point it will tell in which place it has benn clicked;
     * @param x
     * @param y
     * @return retrurn the X: ID of the component clicked and Y: the pin clicked
     */
    Point inputTarget(int x,int y);
    boolean getState(int pin);
    void setState(int pin,boolean state);
    boolean flowsCurrent();
}
