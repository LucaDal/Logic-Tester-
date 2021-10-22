package Components;

import java.awt.*;

public interface Component {
    String getType();
    void paintComponent(Graphics g);
    Point getPosition();
    int getSizeWidth();
    int getSizeHeight();
    int getIDComponent();
    void setPosition(Point position);
    boolean checkIfConnectedPinAreUnderVcc(int pin);
    /**
     * @param pin is the pin you want to check
     * @return return true if the passed pin is grounded
     */
    boolean getGroundedPin(int pin);

    /**
     * needed to repristin the non Ground situation
     * @return true if connected to a gnd
     * @param pin
     */
    boolean hasGndConnected(int pin);
    boolean getPinState(int pin);
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
     * @param otherPin - pin of the other component that i'm setting to
     * @param state state of the component which connect to
     * @return @return  true if the connection is possible, false otherwise
     */
    boolean setConnection(Component anotherComponent,int pin,int otherPin,boolean state);
    void removeConnectionFromPins(int pin);
    void removeConnection();

    /**
     * used usually from other transistor to check at what pin they are connected
     * @return the name of the object
     */
    Component returnObjName();
    /**
     * given a point it will tell in which place it has been clicked;
     * @param x input of the mouse
     * @param y input of the mouse
     * @return retrurn the X: ID of the component clicked and Y: the pin clicked - 0 is the invalid pin
     */
    Point inputTarget(int x,int y);
    boolean getState(int pin);
    void setState(int pin,boolean state);
    void tellToUpdate(Component fromThisComponent);
    void update();
}
