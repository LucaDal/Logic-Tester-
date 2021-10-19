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


public class Gnd implements Component, Serializable {

    @Serial
    private static final long serialVersionUID = 6584013415603752513L;
    private Image img;
    private int sizeWidth, sizeHeight, x, y, ID;
    private JPanel parent;
    private Multimap<Integer, ComponentAndRelativePin> connectedComponent = ArrayListMultimap.create();
    private Component toldToUpdate = null;

    public Gnd(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();

    }
    private void initialize(){
        try {
            String path = System.getProperty("user.dir");
            BufferedImage imgb = ImageIO.read(new File(path + "\\src\\main\\resources\\gnd.png"));
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
    }

    @Override
    public Boolean isGrounded() {
        return true;
    }

    /**
     * uselles - always true so you wont need it
     *
     * @param state state
     * @param pin   pin
     */
    @Override
    public void setGrounded(boolean state, int pin) {
    }

    /**
     * then set the other component to ground because this class is gnd so..
     * @param anotherComponent to connect with
     * @param pin              of this transistor to set to
     * @param otherPin          pin of the other component that has bin connected to this one
     * @param state            state of the component which connect to
     * @return
     */
    @Override
    public boolean setConnection(Component anotherComponent, int pin, int otherPin, boolean state) {
        if (anotherComponent.getType().equalsIgnoreCase("vcc") || anotherComponent.getType().equalsIgnoreCase("switch")){
            JOptionPane.showMessageDialog(parent,"cannot connect to GND component","Impossible connection",JOptionPane.WARNING_MESSAGE);
            return false;
        }else{
            connectedComponent.put(anotherComponent.getIDComponent(), new ComponentAndRelativePin(anotherComponent,otherPin));
            anotherComponent.setGrounded(true, otherPin);
        }
        return true;
    }

    @Override
    public void removeConnectionFromPins(int pin) {
        removeConnection();
    }


    @Override
    public void removeConnection() {
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            Component temp = cp.getComponent();
            temp.tellToUpdate(this);
            temp.resetPinIfContain(this);
            temp.setGrounded(false, cp.getPin());
            temp.tellToUpdate(null);
        }
        connectedComponent.clear();
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
    public boolean getState(int pin) {
        return false;
    }

    @Override
    public void setState(int pin, boolean state) {

    }

    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public void update() {
        for (ComponentAndRelativePin cp : connectedComponent.values()) {
            cp.getComponent().setGrounded(true,cp.getPin());
        }
    }

    @Override
    public String getType() {
        return "gnd";
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, x, y, parent);
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
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
        this.sizeWidth =stream.readInt();
        this.sizeHeight =stream.readInt();
        this.x =stream.readInt();
        this.y =stream.readInt();
        this.ID =stream.readInt();
        this.parent =(JPanel)stream.readObject();
        this.connectedComponent =(Multimap<Integer, ComponentAndRelativePin>)stream.readObject();
        this.toldToUpdate =(Component)stream.readObject();
        initialize();
    }
}
