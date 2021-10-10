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
    void setPosition(Point position);
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

    Component returnObjName();
    /**
     * given a point it will tell in which place it has been clicked;
     * @param x
     * @param y
     * @return retrurn the X: ID of the component clicked and Y: the pin clicked
     */
    Point inputTarget(int x,int y);
    boolean getState(int pin);
    void setState(int pin,boolean state);
    int getPinFromAnotherObj(Component ObgID);
    void tellToUpdate(Component fromThisComponent);
    void update();
}
