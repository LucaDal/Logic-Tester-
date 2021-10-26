package Components;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;


public class Text extends AbstractComponent {
    @Serial
    private static final long serialVersionUID = -3730397820612837281L;
    private String content = "";
    private int upperCase = 0;
    private JTextField field = new JTextField();
    private Boolean isGrounded = false;
    private String[] options = {"Ok", "Cancel"};
    private int constant = 0;
    public Text(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        super(parent, ID, x, y, sizeWidth, sizeHeight);
        int result = JOptionPane.showOptionDialog(parent, field, "Insert text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        if (result == 1) { //cancel is selected
            setGrounded(true, 0);
        }else{
            this.content = field.getText();
            setWidthByText();
        }
    }

    private void setWidthByText() {
        if (content != null) {
            if (content.equals("")) {
                content = " ";
            }
        } else {
            content = " ";
        }
        upperCase = 0;
        for (int i = 0; i < content.length(); i++) {
            int numChar = content.charAt(i);
            if (numChar >= 65 && numChar <= 90) {
                upperCase++;
            }
        }
        constant = ((content.length() - upperCase) * 6) + (upperCase * 8);
        this.sizeWidth = ((content.length() - upperCase) * 6) + (upperCase * 8) + 10;
    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x, y, sizeWidth, sizeHeight);
        g.setColor(Color.WHITE);
        g.fillRect(x + 2, y + 2, sizeWidth - 4, sizeHeight - 4);
        g.setColor(Color.BLACK);
        g.drawString(content, x + sizeWidth / 2 - constant/2, y + 14);
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
    public void resetPinIfContain(Component ID) {
    }

    @Override
    public Boolean isGrounded() {
        return isGrounded;
    }

    /**
     * usato per dire al'interfaccia se mantenere o meno il testo, se viene chiamato con true -> verrà eliminato dopo la creazione
     * @param state if true, it will be deleted
     * @param pin whatever number is ok
     */
    @Override
    public void setGrounded(boolean state, int pin) {
        this.isGrounded = state;
    }

    @Override
    public boolean setConnection(Component anotherComponent, int pin, int otherPin, boolean state) {
        return false;
    }

    @Override
    public void removeConnectionFromPins(int pin) {
    }

    @Override
    public void removeConnection() {
    }

    @Override
    public void setState(int pin, boolean state) {
        int result = JOptionPane.showOptionDialog(parent, field, "Insert text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 1);
        if (result == 0) {
            this.content = field.getText();
            setWidthByText();
        }
    }

    @Override
    public void tellToUpdate(Component fromThisComponent, int pin) {

    }

    @Override
    void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(content);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeInt(upperCase);
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
        this.upperCase = stream.readInt();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
    }
}
