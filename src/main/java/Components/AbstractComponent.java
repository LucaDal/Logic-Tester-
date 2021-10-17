package Components;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractComponent implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = -6138200399111000970L;
    protected int x,y,ID,sizeWidth,sizeHeight,pinOfTheClass = 1;
    protected boolean state = false,isGrounded;
    protected Component toldToUpdate;
    protected Multimap<Integer, ComponentAndRelativePin> connectedComponent = ArrayListMultimap.create();

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

    public void update() {
        for (ComponentAndRelativePin cp : connectedComponent.values()) {//TODO aggiungere il tutto a quando inserirsco un nuovo pin
            Component temp = cp.getComponent();
            if (temp != toldToUpdate) {
                temp.tellToUpdate(this);
                temp.setState(cp.getPin(), this.state);
            }
        }
    }
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

    @Override
    public void updateAfterConnection() {
    }

    @Override
    public Component returnObjName() {
        return this;
    }

    @Override
    public Point inputTarget(int x, int y) {
        return new Point(this.ID,0);
    }

    @Override
    public void resetPinIfContain(Component ID) {
    }

    @Override
    public void tellToUpdate(Component fromThisComponent) {
    }

}
