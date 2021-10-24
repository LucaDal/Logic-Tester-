package Components;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class Text extends AbstractComponent{
    @Serial
    private static final long serialVersionUID = -3730397820612837281L;
    private String content = "";
    public Text(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        super(parent, ID, x, y, sizeWidth, sizeHeight);
        this.content = JOptionPane.showInputDialog(parent,"","Insert text",JOptionPane.INFORMATION_MESSAGE);
        setWidthByText();
    }
    private void setWidthByText(){
        if (content != null) {
            if (content.equals("")) {
                content = "  ";
            }
            this.sizeWidth = (content.length() * 6) + 10;
        }else {
            content = "  ";
        }
    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public void paintComponent(Graphics g) {
        g.fillRect(x,y,sizeWidth,sizeHeight);
        g.setColor(Color.WHITE);
        g.fillRect(x+2,y+2,sizeWidth-4,sizeHeight-4);
        g.setColor(Color.BLACK);
        g.drawString(content, x + sizeWidth/2 - (content.length() *6 /2),y+14);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean checkIfConnectedPinAreUnderVcc(int pin) {
        return false;
    }

    @Override
    public boolean getGroundedPin(int pin) {
        return false;
    }

    @Override
    public boolean hasGndConnected(int pin) {
        return false;
    }

    @Override
    public void resetPinIfContain(Component ID) {}

    @Override
    public Boolean isGrounded() {
        return null;
    }

    @Override
    public void setGrounded(boolean state, int pin) {}

    @Override
    public boolean setConnection(Component anotherComponent, int pin, int otherPin, boolean state) {
        return false;
    }

    @Override
    public void removeConnectionFromPins(int pin) {}

    @Override
    public void removeConnection() {}

    @Override
    public void setState(int pin, boolean state) {
        this.content = JOptionPane.showInputDialog(parent,"","Modify text",JOptionPane.INFORMATION_MESSAGE);
        setWidthByText();
    }

    @Override
    void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(content);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(ID);
        stream.writeObject(parent);
    }

    @Override
    void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.content = (String) stream.readObject();
        this.sizeWidth = stream.readInt();
        this.sizeHeight = stream.readInt();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
    }
}
