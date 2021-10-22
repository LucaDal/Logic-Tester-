package Components;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;

public class BitDisplay extends AbstractComponent {

    static final int pinHigh = 10, pinLow = 11;
    @Serial
    private static final long serialVersionUID = 3312188755651290132L;
    protected Multimap<Integer, ComponentAndRelativePin> connectedComponentHigh = ArrayListMultimap.create();
    protected Multimap<Integer, ComponentAndRelativePin> connectedComponentLow = ArrayListMultimap.create();
    boolean groundedPinHigh = false, groundedPinLow = false,isGrounded = false;

    public BitDisplay(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        super(parent, ID, x, y, sizeWidth, sizeHeight);
    }

    @Override
    public String getType() {
        return "bitDisplay";
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y + 8, sizeWidth, sizeHeight - 8 - 8);
        g.fillOval(x + sizeWidth / 2 - 4, y + sizeHeight - 9, 8, 8);

        g.setColor(Color.white);
        g.fillRect(x + 3, y + 3 + 8, sizeWidth - 6, sizeHeight - 6 - 8 - 8);

        if (state) {
            g.setColor(Color.RED);
            g.drawString("1", x + sizeWidth / 2 - 4, y + sizeHeight / 2 + 4);
            g.fillOval(x + sizeWidth / 2 - 4, y, 8, 8);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(x + sizeWidth / 2 - 4, y, 8, 8);
            g.drawString("0", x + sizeWidth / 2 - 3, y + sizeHeight / 2 + 4);
        }

    }

    public void update() {
        updateComponent(connectedComponentHigh);
        updateComponent(connectedComponentLow);
    }

    @Override
    public boolean checkIfConnectedPinAreUnderVcc(int pin) {
        if (!groundedPinLow){
           return checkIfConnectedPinAreUnderVccPerPin(connectedComponentLow);
        }
        if (!groundedPinHigh){
            return checkIfConnectedPinAreUnderVccPerPin(connectedComponentHigh);
        }
        return false;
    }
    /**
     * @param multiMap of the componentMap to check
     * @return true if a pin is connected to a vcc which is not grounded
     */
    private boolean checkIfConnectedPinAreUnderVccPerPin(Multimap<Integer, ComponentAndRelativePin> multiMap){
        boolean flagState = false;
        for (ComponentAndRelativePin cp : multiMap.values()) {
            if(cp.getComponent().getType().equals("vcc") && cp.getComponent().getState(1)){
                flagState = true;
                break;
            }
        }
        return flagState;
    }
    @Override
    public boolean getGroundedPin(int pin) {
        if(pinHigh == pin){
            return groundedPinHigh;
        }
        return groundedPinLow;
    }

    @Override
    public boolean hasGndConnected(int pin) {
        if(pin == pinLow){
         return groundedPinLow;
        }
        return groundedPinHigh;
    }

    public void updateComponent(Multimap<Integer, ComponentAndRelativePin> multiMap){
        for (ComponentAndRelativePin cp : multiMap.values()) {//TODO aggiungere il tutto a quando inserirsco un nuovo pin
            Component temp = cp.getComponent();
            if (temp != toldToUpdate) {
                temp.tellToUpdate(this);
                temp.setState(cp.getPin(), this.state);
                temp.tellToUpdate(null);
            }
        }
    }

    @Override
    public Boolean isGrounded() {
        return isGrounded;
    }

    @Override
    public void setGrounded(boolean state, int pin) {//TODO risolvere e veder equale pin va a ground
        if (!state && this.state) {
            setState(1, false);
        }
        if (pin == pinHigh && state){
            groundedPinHigh = true;
            updateToGround(connectedComponentHigh,true);
        }else if (pin == pinHigh){
            groundedPinHigh = false;
            updateToGround(connectedComponentHigh,false);
        }

        if (pin == pinLow && state){
            groundedPinLow = true;
            updateToGround(connectedComponentLow,true);
        }else if (pin == pinLow){
            groundedPinLow = false;
            updateToGround(connectedComponentLow,false);
        }
        this.isGrounded = state;
        if (!isGrounded){
            this.state = false;
        }
    }
    private void updateToGround(Multimap<Integer, ComponentAndRelativePin> multiMap,boolean state){
        for (ComponentAndRelativePin cp : multiMap.values()) {
            Component temp = cp.getComponent();
            if (temp != toldToUpdate) {
                temp.tellToUpdate(this);
                temp.setGrounded(state,cp.getPin());
            }
        }
    }

    //pin is unused
    @Override
    public boolean setConnection(Component anotherComponent, int pin, int otherPin, boolean state) {
        if (pin == pinHigh){
            connectedComponentHigh.put(anotherComponent.getIDComponent(), new ComponentAndRelativePin(anotherComponent,otherPin));
        }else if(pin == pinLow){
            connectedComponentLow.put(anotherComponent.getIDComponent(), new ComponentAndRelativePin(anotherComponent,otherPin));
        }
        if (state){
            setState(pin,true);
        }
        return true;
    }

    @Override
    public Point inputTarget(int x, int y) {
        if (x >= this.x && x <= this.x + this.sizeWidth && y >= this.y && y <= this.y + 12) {
            return new Point(this.ID, pinHigh);
        }
        if (x >= this.x && x <= this.x + this.sizeWidth && y >= this.y + this.sizeHeight - 12 && y <= this.y + sizeHeight) {
            return new Point(this.ID, pinLow);
        }
        return new Point(this.ID, 1);
    }

    @Override
    public void removeConnectionFromPins(int pin) {
        if (pin == pinHigh){
            removeConnectionFromHashMap(connectedComponentHigh);
            if(groundedPinHigh){
                setGrounded(false,pinHigh);
            }
            setState(pinHigh,false);
        }else if (pin == pinLow){
            removeConnectionFromHashMap(connectedComponentLow);
            if(groundedPinLow){
                setGrounded(false,pinLow);
            }
            setState(pinLow,false);
        }
    }

    @Override
    public void removeConnection() {
        removeConnectionFromHashMap(connectedComponentHigh);
        removeConnectionFromHashMap(connectedComponentLow);
    }
    private void removeConnectionFromHashMap(Multimap<Integer, ComponentAndRelativePin> multiMap){
        for (ComponentAndRelativePin cp : multiMap.values()) {
            cp.getComponent().resetPinIfContain(this);
        }
        multiMap.clear();
    }

    /**
     * @param pin   in this function is not used
     * @param state to give to this class
     */
    @Override
    public void setState(int pin, boolean state) {//TODO fix

        if (pin == pinHigh && isGrounded){
            this.state = state;
            updateComponent(connectedComponentHigh);
        }
        if (pin == pinLow && isGrounded){
            this.state = state;
            updateComponent(connectedComponentLow);
        }
    }

    protected void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(state);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeBoolean(isGrounded);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(ID);
        stream.writeObject(parent);
        stream.writeObject(connectedComponentHigh);
        stream.writeObject(connectedComponentLow);
    }


    protected void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.state = stream.readBoolean();
        this.sizeWidth = stream.readInt();
        this.sizeHeight = stream.readInt();
        this.isGrounded = stream.readBoolean();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
        this.connectedComponentHigh = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
        this.connectedComponentLow = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
    }
}