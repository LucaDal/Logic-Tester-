package Components;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Switch implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = -7039775726878008122L;
    final String type = "switch";
    boolean state = false;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;
    Component toldToUpdate = null;
    private Multimap<Integer, Component> connectedComponent = ArrayListMultimap.create();

    public Switch(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {//TODO ampliare grandezza per far entrare il pin
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2 + 5;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, sizeWidth, sizeHeight-7);
        g.setColor(Color.white);
        g.fillRect(x+3, y+3, sizeWidth-6, sizeHeight-7-6);
        if (state) {
            g.setColor(Color.RED);
            g.drawString("1", x + sizeWidth/2 - 3, y + sizeHeight / 2 + 1);
            g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);
            g.drawString("0", x +  sizeWidth/2 - 3, y + sizeHeight / 2 + 1);
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
    public void resetAskedPin() {
    }

    @Override
    public void updateAfterConnection() {
    }

    @Override
    public void resetPinIfContain(Component ID) {
    }

    @Override
    public Boolean isGrounded() {
        return false;
    }

    @Override
    public void setGrounded(boolean state, int pin) {

    }

    @Override
    public void setConnection(Component anotherComponent, int pin, boolean state) {
        connectedComponent.put(anotherComponent.getIDComponent(), anotherComponent.returnObjName());
    }

    @Override
    public void removeConnection() {
        for (Component c : connectedComponent.values()) {
            c.resetPinIfContain(this);
        }
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
            return new Point(this.ID,1);
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
        if (pin == 1) {
            this.state = state;
        }
        update();
        toldToUpdate = null;
    }

    @Override
    public int getPinFromAnotherObj(Component ObgID) {
        return 0;
    }


    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public void update() {
        Component temp = null;
        for (Component c : connectedComponent.values()) {
            if (temp != c){
                c.resetAskedPin();
                temp = c;
            }
            int pinToUpdate = c.getPinFromAnotherObj(this);
            if (c != toldToUpdate) {
                c.tellToUpdate(this);
                c.setState(pinToUpdate, this.state);
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(state);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(ID);
        stream.writeObject(parent);
        stream.writeObject(connectedComponent);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.state =stream.readBoolean();
        this.sizeWidth =stream.readInt();
        this.sizeHeight =stream.readInt();
        this.x =stream.readInt();
        this.y =stream.readInt();
        this.ID =stream.readInt();
        this.parent =(JPanel)stream.readObject();
        this.connectedComponent =(Multimap<Integer, Component>) stream.readObject();
    }

}
