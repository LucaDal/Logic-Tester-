package Components;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class Switch implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = -7039775726878008122L;
    static int pinToChangeState = 4;
    static int pinConnectionSwitch = 1;
    private boolean state = false;
    private int sizeWidth, sizeHeight, x, y, ID;
    private JPanel parent;
    private Component toldToUpdate = null;
    private boolean isGrounded = false;
    private boolean turnOn = false;

    private Multimap<Integer, ComponentAndRelativePin> connectedComponent = ArrayListMultimap.create();
    public Switch(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
    }

    @Override
    public String getType() {
        return "switch";
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, sizeWidth, sizeHeight-7);
        g.setColor(Color.white);
        g.fillRect(x+3, y+3, sizeWidth-6, sizeHeight-7-6);
        if (turnOn) {
            g.setColor(Color.RED);
            g.fillRect(x +  sizeWidth/2 - 2, y + sizeHeight / 2 -4,4,5);
            if (!isGrounded){
                g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);//ovale rosso
            }else{
                g.setColor(Color.BLACK);
                g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);//ovale rosso
            }
            g.setColor(Color.BLACK);
            g.fillRect(x +  sizeWidth/2 - 2, y + sizeHeight / 2 -9,4,5);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);
            g.fillRect(x +  sizeWidth/2 - 2, y + sizeHeight / 2 -4,4,5);
            g.setColor(Color.RED);
            g.fillRect(x +  sizeWidth/2 - 2, y + sizeHeight / 2 -9,4,5);

        }
    }

    @Override
    public Point getPosition() {
        return new Point(x, y);
    }

    @Override
    public int getSizeWidth() {
        return this.sizeWidth;
    }

    @Override
    public int getSizeHeight() {
        return this.sizeHeight;
    }

    @Override
    public int getIDComponent() {
        return this.ID;
    }

    @Override
    public void setPosition(Point position) {
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public boolean checkIfConnectedPinAreUnderVcc(int pin) {
        return state;
    }

    @Override
    public boolean getGroundedPin(int pin) {
        return isGrounded();
    }

    @Override
    public boolean hasGndConnected(int pin) {
        return false;
    }

    @Override
    public boolean getPinState(int pin) {
        return state;
    }


    @Override
    public void updateAfterConnection() {
    }

    @Override
    public void resetPinIfContain(Component ID) {
        ComponentAndRelativePin temp = null;
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            if (cp.getComponent() == ID){
                temp = cp;
                break;
            }
        }
        connectedComponent.remove(ID.getIDComponent(),temp);
        checkIfConnectedToSomeGroundedComp();
    }

    @Override
    public Boolean isGrounded() {
        return isGrounded;
    }

    void checkIfConnectedToSomeGroundedComp() {
        boolean flag = false;
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            Component temp = cp.getComponent();
            if (toldToUpdate != temp) {
                if (temp.getGroundedPin(cp.getPin())) {
                    flag = true;
                    break;
                }
            }
        }
        setGrounded(flag, 1);
    }

    @Override
    public void setGrounded(boolean state, int pin) {
        this.isGrounded = state;
        if (turnOn && !state){
            this.state = true;
        }else if(this.state){
            this.state = false;
        }
        update();
    }

    @Override
    public boolean setConnection(Component anotherComponent, int pin, int otherPin, boolean state) {
        if (!anotherComponent.getType().equalsIgnoreCase("gnd")){
            connectedComponent.put(anotherComponent.getIDComponent(), new ComponentAndRelativePin(anotherComponent,otherPin));
            return true;
        }
        return false;
    }

    @Override
    public void removeConnectionFromPins(int pin) {
        isGrounded = false;
        removeConnection();
    }

    @Override
    public void removeConnection() {
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            cp.getComponent().resetPinIfContain(this);
        }
        connectedComponent.clear();
    }

    @Override
    public Component returnObjName() {
        return this;
    }

    /**
     *
     * @param x x from the mouse clicked
     * @param y y from thwe mouse clicked
     * @return a new point where X = ID of the component and Y the zone clicked: 0 if on switch 2 if on the pin
     */
    @Override
    public Point inputTarget(int x, int y) {
        if (x >=  this.x  && x <= this.x + this.sizeWidth && y >= this.y + this.sizeHeight -8 && y <= this.y + this.sizeHeight){
            return new Point(this.ID,pinConnectionSwitch);
        }
        return new Point(this.ID,0);
    }

    @Override
    public boolean getState(int pin) {
        return state;
    }

    /**
     * only user can update this state, so eventually if you use a pin > 10 you can update
     * this component
     *
     * @param pin   a number = 1 just to preserve other component to update it
     * @param state state to give to the switch
     */
    @Override
    public void setState(int pin, boolean state) {
        if (pin == pinToChangeState && !isGrounded) {
            turnOn = !turnOn;
            this.state = state;
        }else if(pinToChangeState == pin){
            turnOn = !turnOn;
        }
        update();
    }

    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public void rotateComponent() {

    }

    @Override
    public void tellToUpdate(Component fromThisComponent, int pin) {

    }

    @Override
    public void update() {
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            Component temp = cp.getComponent();
            if (temp != toldToUpdate) {
                temp.tellToUpdate(this);
                if (!temp.getGroundedPin(cp.getPin())){
                    temp.setState(cp.getPin(), this.state);
                }
                temp.tellToUpdate(null);
            }
        }
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(state);
        stream.writeBoolean(isGrounded);
        stream.writeBoolean(turnOn);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(ID);
        stream.writeObject(parent);
        stream.writeObject(connectedComponent);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.state =stream.readBoolean();
        this.isGrounded =stream.readBoolean();
        this.turnOn =stream.readBoolean();
        this.sizeWidth =stream.readInt();
        this.sizeHeight =stream.readInt();
        this.x =stream.readInt();
        this.y =stream.readInt();
        this.ID =stream.readInt();
        this.parent =(JPanel)stream.readObject();
        this.connectedComponent = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
    }
}
