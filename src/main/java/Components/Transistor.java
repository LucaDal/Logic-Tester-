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
    final int pinA = 3, pinB = 2, pinC = 9;
    HashMap<String, Integer> connections;
    final String type = "transistor";
    Image img,imgAB,imgABC,imgAC,imgB,imgBC,imgC,imgA;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;

    public Transistor(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / pinB;
        this.y = y - sizeHeight / pinB;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();
    }

    @SuppressWarnings("unchecked")
    private void initialize() {
        connections = new HashMap();
        connections.put("A", 0);
        connections.put("B", 0);
        connections.put("C", 0);
        BufferedImage imgb,imgbAB,imgbABC,imgbAC,imgbB,imgbBC,imgbC,imgbA;
        imgb = imgbAB = imgbABC = imgbAC = imgbB = imgbBC = imgbC = imgbA = null;
        try {
            imgb = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npn.png"));
            imgbAB = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnAB.png"));
            imgbABC = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnABC.png"));
            imgbAC = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnAC.png"));
            imgbB = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnB.png"));
            imgbBC = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnBC.png"));
            imgbC = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnC.png"));
            imgbA = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npnA.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        img = imgb.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgAB = imgbAB.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgABC = imgbABC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgAC = imgbAC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgB = imgbB.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgBC = imgbBC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgC = imgbC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        imgA = imgbA.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
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
     * tell if the id given is connected to this transistor
     * if it has it it will put it false
     * @param ID is the ID of the element which was allocated with some pin
     * @return a new Point where X indicate the ID of this component, and Y the pin that now is without connection
     * IF y = 0 then doesn't contain a pin connected to the ID passed
     */
    public Point resetIfCointained(int ID) {
        Point IDPin;
        if (connections.get("A") == ID) {
            IDPin = new Point(this.ID, pinA);
            setConnection(IDPin);
            System.out.println("eliminata connessione tra pin A e il componente di ID:" + ID);
            return IDPin;
        }
        if (connections.get("B") == ID) {
            IDPin = new Point(this.ID, pinB);
            setConnection(IDPin);
            System.out.println("eliminata connessione tra pin B e il componente di ID:" + ID);
            return IDPin;
        }
        if (connections.get("C") == ID) {
            IDPin = new Point(this.ID, pinC);
            setConnection(IDPin);
            System.out.println("eliminata connessione tra pin C e il componente di ID:" + ID);
            return IDPin;
        }
        return new Point(this.ID, 0);
    }

    /**
     * @param p Y coordinate indicate the transistor pin;
     *          X coordinate indicate the ID which will be connected
     */
    public void setConnection(Point p) {
        if (p.y == pinB) {
            connections.put("B", p.x);
        }
        if (p.y == pinA) {
            connections.put("A", p.x);
        }
        if (p.y == pinC) {
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
        if (numberTarget == pinB) { //B
            return new Point(ID, pinB);//pin B
        }
        if (numberTarget == pinA) { //pin A
            return new Point(ID, pinA);
        }
        if (numberTarget == pinC) { //pin C
            return new Point(ID, pinC);
        }
        return new Point(ID, 10);
    }

    @Override
    public boolean getState(int pin) {
        return isOn;
    }

    public void update(){
        isOn = B;
        if (A && B) {
            C = true;
        } else if (C && B) {
            A = true;
        }
        System.out.println("ID " + ID + ": (A=" + A + "),(B=" + B + "),(C=" + C + ")");
    }

    @Override
    public void setState(int pin, boolean state) {
        if (pin == pinA) {
            this.A = state;
        } else if (pin == pinB) {
            this.B = state;
        } else if (pin == pinC) {
            this.C = state;
        }
        System.out.println("ID " + ID + ": (A=" + A + "),(B=" + B + "),(C=" + C + ")");
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
        if (!A && !B && !C)
            g.drawImage(img, x, y, parent);
        if (A && !B && !C)
            g.drawImage(imgA, x, y, parent);
        if (!A && B && !C)
            g.drawImage(imgB, x, y, parent);
        if (!A && !B && C)
            g.drawImage(imgC, x, y, parent);
        if (A && B && C)
            g.drawImage(imgABC, x, y, parent);
        if (A && B && !C)
            g.drawImage(imgAB, x, y, parent);
        if (A && !B && C)
            g.drawImage(imgAC, x, y, parent);
        if (!A && B && C)
            g.drawImage(imgBC, x, y, parent);

    }
}
