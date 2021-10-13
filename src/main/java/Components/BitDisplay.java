package Components;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;

public class BitDisplay extends AbstractComponent {

    static final int pinHigh = 10,pinLow = 11;
    @Serial
    private static final long serialVersionUID = 3312188755651290132L;
    HashMap<Integer, Component> connectedComponent = new HashMap<>();
    public BitDisplay(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        super(parent, ID, x, y, sizeWidth, sizeHeight);
    }

    @Override
    public String getType() {
        return "bitDisplay";
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y+8, sizeWidth, sizeHeight-8-8);
        g.fillOval(x + sizeWidth/2 - 4,y +sizeHeight-9,8,8);

        g.setColor(Color.white);
        g.fillRect(x+3, y+3+8, sizeWidth-6, sizeHeight-6-8-8);

        if (state) {
            g.setColor(Color.RED);
            g.drawString("1", x + sizeWidth/2 - 4, y + sizeHeight / 2 + 4);
            g.fillOval(x + sizeWidth/2 - 4,y,8,8);
        } else {
            g.setColor(Color.BLACK);
            g.fillOval(x + sizeWidth/2 - 4,y,8,8);
            g.drawString("0", x +  sizeWidth/2 - 3, y + sizeHeight / 2 + 4);
        }

    }

    @Override
    public Boolean isGrounded() {
        return false;
    }

    @Override
    public void setGrounded(boolean state, int pin) {
        if(!state && this.state){
            setState(1,false);
        }
        this.isGrounded = state;
    }

    @Override
    public void setConnection(Component anotherComponent, int pin, boolean state) {
        connectedComponent.put(anotherComponent.getIDComponent(), anotherComponent.returnObjName());
    }

    @Override
    public Point inputTarget(int x, int y) {
        if (x >=  this.x  && x <= this.x + this.sizeWidth && y >= this.y && y <= this.y + 12){
            return new Point(this.ID,pinHigh);
        }
        if (x >=  this.x  && x <= this.x + this.sizeWidth && y >= this.y + this.sizeHeight -12 && y <= this.y + sizeHeight){
            return new Point(this.ID,pinLow);
        }
        return new Point(this.ID,0);
    }

    @Override
    public void removeConnection() {
        for (Component c : connectedComponent.values()) {
            c.resetPinIfContain(this);
        }
    }

    /**
     *
     * @param pin in this function is not used
     * @param state to give to this class
     */
    @Override
    public void setState(int pin, boolean state) {
        if (isGrounded){
            this.state = state;
        }
    }

    @Override
    public int getPinFromAnotherObj(Component ObgID) {
        return 1;
    }
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(state);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeBoolean(isGrounded);
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
        this.isGrounded = stream.readBoolean();
        this.x =stream.readInt();
        this.y =stream.readInt();
        this.ID =stream.readInt();
        this.parent =(JPanel)stream.readObject();
        this.connectedComponent =(HashMap<Integer, Component>) stream.readObject();
    }
}