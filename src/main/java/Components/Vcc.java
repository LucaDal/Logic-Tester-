package Components;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class Vcc implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = 2344710408277521742L;
    final String type = "vcc";
    boolean state = true, isGrounded = false, imBeenDeleted = false, setGroundCall = false;
    Image img;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;
    Component toldToUpdate = null;
    Multimap<Integer, ComponentAndRelativePin> connectedComponent = ArrayListMultimap.create();

    public Vcc(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();

    }

    private void initialize() {
        BufferedImage imgb = null;
        try {
            String path = System.getProperty("user.dir");
            imgb = ImageIO.read(new File(path + "\\src\\main\\resources\\vcc.png"));
            img = imgb.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public int getSizeWidth() {
        return sizeWidth;
    }

    public int getSizeHeight() {
        return sizeHeight;
    }

    public int getIDComponent() {
        return ID;
    }

    @Override
    public void setPosition(Point position) {
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public boolean checkIfConnectedPinAreUnderVcc(int pin) {
        return true;
    }

    @Override
    public boolean getGroundedPin(int pin) {
        return isGrounded;
    }

    @Override
    public boolean hasGndConnected(int pin) {
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            if (cp.getComponent().getType().equals("gnd")) {
                return true;
            }
        }
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
        boolean containsTheComponent = false;
        ComponentAndRelativePin toDelete = null;
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            Component temp = cp.getComponent();
            if (temp == ID) {
                containsTheComponent = true;
                toDelete = cp;
            }
        }
        if (containsTheComponent) {
            connectedComponent.remove(ID.getIDComponent(), toDelete);
        }
        checkIfConnectedToSomeGroundedComp();
    }

    @Override
    public Boolean isGrounded() {
        return isGrounded;
    }

    /**
     * ymake sure this pin will give to other component 0 current if it is grounded
     *
     * @param state true o false
     * @param pin   in this function it is uselles, so every pin is ok
     */
    @Override
    public void setGrounded(boolean state, int pin) {
        this.isGrounded = state;
        this.state = !state;
        setGroundCall = true;
        update();
    }

    @Override
    public boolean setConnection(Component anotherComponent, int ID, int otherPin, boolean state) {
        if (!anotherComponent.getType().equalsIgnoreCase("gnd")) {
            connectedComponent.put(anotherComponent.getIDComponent(), new ComponentAndRelativePin(anotherComponent, otherPin));
            if (anotherComponent.getGroundedPin(otherPin)) {
                setGrounded(true, 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void removeConnectionFromPins(int pin) {
        removeConnection();
        connectedComponent.clear();
        this.state = true;
        this.isGrounded = true;
    }

    @Override
    public void removeConnection() {
        imBeenDeleted = true;
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            cp.getComponent().resetPinIfContain(this);
        }
        connectedComponent.clear();
    }

    @Override
    public void update() {
        if (!setGroundCall) {
            checkIfConnectedToSomeGroundedComp();
        }
        if (!imBeenDeleted) {
            for (ComponentAndRelativePin cp : connectedComponent.values()) {
                Component temp = cp.getComponent();
                int pinToUpdate = cp.getPin();
                if (temp != toldToUpdate) {
                    temp.tellToUpdate(this);
                    temp.setState(pinToUpdate, this.state);
                } else if (!temp.getState(pinToUpdate) && state) {
                    temp.tellToUpdate(this);
                    temp.setState(pinToUpdate, this.state);
                }
            }
        }
        setGroundCall = false;
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
    public void setState(int pin, boolean state) {
        update();
    }

    /**
     * @param pin not used in this class
     * @return state of this pin, usally always true, when not connnected to GND
     */
    @Override
    public boolean getState(int pin) {
        checkIfConnectedToSomeGroundedComp();
        return state; //opposite of isGrounded
    }

    @Override
    public Component returnObjName() {
        return this;
    }

    @Override
    public Point inputTarget(int x, int y) {
        return new Point(ID, 1);
    }


    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public void tellToUpdate(Component fromThisComponent, int pin) {

    }

    @Override
    public String getType() {
        return type;
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, x, y, parent);
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(state);
        stream.writeBoolean(isGrounded);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(ID);
        stream.writeObject(parent);
        stream.writeObject(connectedComponent);
        stream.writeObject(toldToUpdate);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {

        this.state = stream.readBoolean();
        this.isGrounded = stream.readBoolean();
        this.sizeWidth = stream.readInt();
        this.sizeHeight = stream.readInt();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
        this.connectedComponent = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
        this.toldToUpdate = (Component) stream.readObject();
        initialize();
    }

    @Override
    public String toString() {
        return "Vcc{" +
                ", state=" + state +
                ", isGrounded=" + isGrounded +
                ", x=" + x +
                ", y=" + y +
                ", ID=" + ID +
                // ", connectedComponent=" + connectedComponent.toString() +
                '}';
    }
}
