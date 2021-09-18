package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Transistor implements Component {
    boolean A = false, B = false, C = false, isOn = false, flowsCurrent = false;
    HashMap<String, Integer> connections;
    final String type = "transistor";
    Image img;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;

    public Transistor(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();
    }

    @SuppressWarnings("unchecked")
    private void initialize() {
        connections = new HashMap() {{
            put("A", 0);
            put("B", 0);
            put("C", 0);
        }};

        BufferedImage imgb = null;
        try {
            imgb = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npn.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        img = imgb.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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
    /**
     * contain() tell if the id given is connected to this transistor
     * if it has it it will put it false
     */
    public boolean contains(int ID) {
        if (connections.get("A") == ID) {
            A = false;
            System.out.println("eliminata connessione tra pin A e il componente di ID:" + ID);
            return true;
        }
        if (connections.get("B") == ID) {
            B = false;
            System.out.println("eliminata connessione tra pin B e il componente di ID:" + ID);
            return true;
        }
        if (connections.get("C") == ID) {
            C = false;
            System.out.println("eliminata connessione tra pin C e il componente di ID:" + ID);
            return true;
        }
        return false;
    }

    /**
     * @param p Y coordinate indicate the transistor letter
     *          X coordinate indicate the ID which will be connected
     */
    public void setConnection(Point p) {
        if (p.y == 2) {
            connections.put("B", p.x);
        }
        if (p.y == 3) {
            connections.put("A", p.x);
        }
        if (p.y == 9) {
            connections.put("C", p.x);
        }
        System.out.println("Connection setted between: " + ID + ", and: " + p.x);
    }

    /**
     * given x,y this function divide the image by 3x3 and return in which location the mouse
     * is located + the id of the component
     * because im dumb i will let in this way A = 3; B = 2; C = 9; invalid = 10
     *
     * @param x - coordinate of the mouse
     * @param y - coordinate of the mouse
     * @return a Point where x indicate the Component ID and Y the letter selected: exaple if table is 3 by 3 and im in
     * the right bottom the it will return 9
     */
    public Point inputTarget(int x, int y) {
        int positionMouseX = ((x - this.x) / 10) + 1;
        int positionMouseY = ((y - this.y) / 10) + 1;
        int numberTarget = positionMouseX * positionMouseY;
        if (numberTarget == 2) { //B
                return new Point(ID, 2);//pin B
        }
        if (numberTarget == 3) { //pin A
                return new Point(ID, 3);
        }
        if (numberTarget == 9) { //pin C
                return new Point(ID, 9);
        }
        return new Point(ID, 10);
    }

    @Override
    public boolean getState(int pin) {
        return isOn;
    }

    @Override
    public void setState(int pin, boolean state) {
        if (pin == 3) {
            this.A = state;
        } else if (pin == 2) {
            this.B = state;
        } else if (pin == 9) {
            this.C = state;
        }

        isOn = B;
        if (A && B) {
            C = true;
        } else if (C && B) {
            A = true;
        }
        System.out.println("pin A: " + A + ", pin B: " + B + ", pin C: " + C);
    }

    @Override
    public boolean flowsCurrent() {
        return this.flowsCurrent;
    }


    @Override
    public String getType() {
        return type;
    }

    public void paint(Graphics g) {//TODO draw rect where the points are selectable
        g.drawImage(img, x, y, parent);
    }


}
