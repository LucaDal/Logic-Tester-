package Components;

import com.google.common.collect.Multimap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractComponent implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = -6138200399111000970L;
    protected int x,y,ID,sizeWidth,sizeHeight;
    protected boolean state = false;
    protected Component toldToUpdate;
    protected int toldToUpdateFromPin;


    JPanel parent;

    public AbstractComponent(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
    }

    abstract public void paintComponent(Graphics g);
    abstract public void update();

    @Override
    public Point getPosition() {
        return new Point(this.x,this.y);
    }

    @Override
    public int getSizeWidth() {
        return sizeWidth;
    }

    @Override
    public int getSizeHeight() {
        return sizeHeight;
    }

    @Override
    public int getIDComponent() {
        return ID;
    }

    @Override
    public boolean getState(int pin) {
        return state;
    }

    @Override
    public void setPosition(Point position) {
        this.x = position.x;
        this.y = position.y;
    }

    public abstract boolean checkIfConnectedPinAreUnderVcc(int pin);

    @Override
    public abstract boolean getGroundedPin(int pin);

    @Override
    public abstract boolean hasGndConnected(int pin);

    @Override
    public boolean getPinState(int pin) {
        return false;
    }

    @Override
    public void updateAfterConnection() {
    }

    @Override
    public Component returnObjName() {
        return this;
    }

    @Override
    public Point inputTarget(int x, int y) {
        return new Point(this.ID,1);
    }

    @Override
    public abstract void resetPinIfContain(Component ID);

    @Override
    public abstract void removeConnectionFromPins(int pin);
    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public void tellToUpdate(Component fromThisComponent, int pin) {
        this.toldToUpdate = fromThisComponent;
        this.toldToUpdateFromPin = pin;
    }

    abstract void writeObject(java.io.ObjectOutputStream stream) throws IOException;
    abstract void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException;
}
