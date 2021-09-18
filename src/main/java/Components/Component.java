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
    /**
     * tell if the id given is connected to this transistor
     * if it has it it will put it false
     * @param ID is the ID of the element which was allocated with some pin
     * @return a new Point where X indicate the ID of this component, and Y the pin that now is without connection
     * IF y = 0 then doesn't contain a pin connected to the ID passed
     */
    Point resetIfCointained(int ID);
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
    void update();
}
