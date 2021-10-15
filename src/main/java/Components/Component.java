package Components;

import java.awt.*;
import java.util.HashMap;

public interface Component {
    String getType();
    void paintComponent(Graphics g);
    Point getPosition();
    int getSizeWidth();
    int getSizeHeight();
    int getIDComponent();
    void setPosition(Point position);

    /**
     * used to reset askedpinA/B into transistor class to false, dont use it into transistor class
     */
    void resetAskedPin();

    /**
     * called after two component will be connected
     */
    void updateAfterConnection();
    /**
     * tell if the id given is connected to this transistor
     * if it has it it will put it false
     * @param ID is the ID of the element which was allocated with some pin
     */
    void resetPinIfContain(Component ID);
    Boolean isGrounded();
    void setGrounded(boolean state, int pin);
    /**
     * set a connection between this component and  another Component
     * @param anotherComponent to connect with
     * @param pin of this transistor to set to
     * @param state state of the component which connect to
     */
    void setConnection(Component anotherComponent,int pin,boolean state);

    void removeConnection();

    /**
     * used usually from other transistor to check at what pin they are connected
     * @return the name of the object
     */
    Component returnObjName();
    /**
     * given a point it will tell in which place it has been clicked;
     * @param x
     * @param y
     * @return retrurn the X: ID of the component clicked and Y: the pin clicked - 0 is the invalid pin
     */
    Point inputTarget(int x,int y);
    boolean getState(int pin);
    void setState(int pin,boolean state);

    /**
     * return the pin which the caller is connected
     *
     * @param ObgID name of the component(Obg) that call this function
     * @return the pin number
     */
    int getPinFromAnotherObj(Component ObgID);
    void tellToUpdate(Component fromThisComponent);
    void update();
}
