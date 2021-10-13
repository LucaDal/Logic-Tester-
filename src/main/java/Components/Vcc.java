package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Vcc implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = 2344710408277521742L;
    final String type = "vcc";
    boolean state = true, isGrounded = false, imBeenDeleted = false;
    Image img;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;
    Component toldToUpdate = null;
    HashMap<Integer, Component> connectedComponent = new HashMap<>();

    public Vcc(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();

    }
    private void initialize(){
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
    public void updateAfterConnection() {

    }

    @Override
    public void resetPinIfContain(Component ID) {
        if (connectedComponent.containsValue(ID)) {
            connectedComponent.remove(ID);
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
        if (state) {
            this.state = false;
        } else {
            this.state = true;
        }
    }

    @Override
    public void setConnection(Component anotherComponent, int ID, boolean state) {
        connectedComponent.put(anotherComponent.getIDComponent(), anotherComponent.returnObjName());
        if (anotherComponent.isGrounded() && (anotherComponent.getPinFromAnotherObj(this) != 2)) {
            setGrounded(true, 0);
        }
    }

    @Override
    public void removeConnection() {
        imBeenDeleted = true;
        for (Component c : connectedComponent.values()) {
            c.resetPinIfContain(this);
        }
    }

    @Override
    public void update() {
        checkIfConnectedToSomeGroundedComp();
        if (!imBeenDeleted) {
            for (Component c : connectedComponent.values()) {
                int pinToUpdate = c.getPinFromAnotherObj(this);
                if (c != toldToUpdate) {
                    c.tellToUpdate(this);
                    c.setState(pinToUpdate, this.state);
                } else if (!c.getState(c.getPinFromAnotherObj(this)) && state) {
                    c.tellToUpdate(this);
                    c.setState(pinToUpdate, this.state);
                }
            }
        }

    }

    void checkIfConnectedToSomeGroundedComp() {
        boolean flag = false;
        for (Component c : connectedComponent.values()) {
            if (c.isGrounded() && c.getPinFromAnotherObj(this) != 2) {
                flag = true;
            }
        }
        if (!flag) {
            setGrounded(false, 0);
        } else {
            setGrounded(true, 0);
        }
    }

    @Override
    public void setState(int pin, boolean state) {
        update();
    }

    /**
     * @param pin
     * @return state of this pin, usally always true, when not connnected to GND
     */
    @Override
    public boolean getState(int pin) {
        checkIfConnectedToSomeGroundedComp();
        return state; //opposite of ground
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
    public int getPinFromAnotherObj(Component ObgID) {
        return 0;
    }


    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public String getType() {
        return type;
    }

    public void paint(Graphics g) {
        g.drawImage(img, x, y, parent);
    }

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

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {

        this.state =stream.readBoolean();
        this.isGrounded =stream.readBoolean();
        this.sizeWidth =stream.readInt();
        this.sizeHeight =stream.readInt();
        this.x =stream.readInt();
        this.y =stream.readInt();
        this.ID =stream.readInt();
        this.parent =(JPanel)stream.readObject();
        this.connectedComponent =(HashMap) stream.readObject();
        this.toldToUpdate =(Component)stream.readObject();
        initialize();
    }

    @Override
    public String toString() {
        return "Vcc{"+
                ", state=" + state +
                ", isGrounded=" + isGrounded +
                ", x=" + x +
                ", y=" + y +
                ", ID=" + ID +
               // ", connectedComponent=" + connectedComponent.toString() +
                '}';
    }
}
