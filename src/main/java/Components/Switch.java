package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Switch implements Component {
    final String type = "switch";
    boolean state = false;
    Image img;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;
    Component toldToUpdate = null;
    HashMap<Integer, Component> connectedComponent = new HashMap<>();

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
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, sizeWidth, sizeHeight-8);
        //g.fillRect(x, y, sizeWidth, sizeHeight);
        if (state) {
            g.setColor(Color.RED);
            g.drawString("1", x + sizeWidth/2 - 3, y + sizeHeight / 2);
            g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);
        } else {
            g.fillOval(x + sizeWidth/2 - 4,y + sizeHeight -8,8,8);
            g.drawString("0", x +  sizeWidth/2 - 3, y + sizeHeight / 2);

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
    }

    @Override
    public int getPinFromAnotherObj(Component ObgID) {
        return 0;
    }


    @Override
    public void tellToUpdate(Component fromThisComponent) {
    }

    @Override
    public void update() {
        for (Component c : connectedComponent.values()) {
            int pinToUpdate = c.getPinFromAnotherObj(this);
            if (c != toldToUpdate) {
                c.tellToUpdate(this);
                c.setState(pinToUpdate, this.state);
            }
        }
    }
}
