package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


public class Transistor implements Component, Serializable {
    boolean A, B, C, updated, fromAtoC, BisUpdated,
            AisUpdated, CisUpdated, isGrounded, fromComponentToA,
            fromComponentToC, AisGrounded, CisGrounded, groundUpdate;
    final int pinA = 3, pinB = 2, pinC = 9;
    final String type = "transistor";
    Image img, imgAB, imgABC, imgAC, imgB, imgBC, imgC, imgA;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;
    Component transistorConnectedToPinA = null; // predispongo per avere piu transistor in parallelo
    Component transistorConnectedToPinB = null;
    Component transistorConnectedToPinC = null;
    Component toldToUpdate = null;

    public Transistor(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / pinB;
        this.y = y - sizeHeight / pinB;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();
    }

    //@SuppressWarnings("unchecked")
    private void initialize() {
        BufferedImage imgb, imgbAB, imgbABC, imgbAC, imgbB, imgbBC, imgbC, imgbA;
        String path = System.getProperty("user.dir");
        A = B = C = updated = fromAtoC = BisUpdated = AisUpdated = CisUpdated =
                fromComponentToA = fromComponentToC = AisGrounded = CisGrounded = groundUpdate = false;
        try {
            imgb = ImageIO.read(new File((path + "\\src\\main\\resources\\npn.png")));
            imgbAB = ImageIO.read(new File(path + "\\src\\main\\resources\\npnAB.png"));
            imgbABC = ImageIO.read(new File(path + "\\src\\main\\resources\\npnABC.png"));
            imgbAC = ImageIO.read(new File(path + "\\src\\main\\resources\\npnAC.png"));
            imgbB = ImageIO.read(new File(path + "\\src\\main\\resources\\npnB.png"));
            imgbBC = ImageIO.read(new File(path + "\\src\\main\\resources\\npnBC.png"));
            imgbC = ImageIO.read(new File(path + "\\src\\main\\resources\\npnC.png"));
            imgbA = ImageIO.read(new File(path + "\\src\\main\\resources\\npnA.png"));
            img = imgb.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgAB = imgbAB.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgABC = imgbABC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgAC = imgbAC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgB = imgbB.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgBC = imgbBC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgC = imgbC.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
            imgA = imgbA.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

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

    /**
     * tell if the id given is connected to this transistor, if does it disconnect
     * and reset the pin following setConection's rules
     *
     * @param ID is the ID of the element which was allocated with some pin
     */
    public void resetPinIfContain(Component ID) {
        if (ID.getType().equalsIgnoreCase("gnd")) {
            isGrounded = false;
        }
        if (transistorConnectedToPinA == ID) {
            if (fromComponentToA) {
                setConnection(null, pinA, false);
            } else {
                transistorConnectedToPinA = null;
            }
            System.out.println("eliminata connessione tra pin A e il componente di ID:" + ID);
        }
        if (transistorConnectedToPinB == ID) {
            setConnection(null, pinB, false);
            System.out.println("eliminata connessione tra pin B e il componente di ID:" + ID);
        }
        if (transistorConnectedToPinC == ID) {
            if (fromComponentToC) {
                setConnection(null, pinC, false);
            } else {
                transistorConnectedToPinC = null;
            }
            System.out.println("eliminata connessione tra pin C e il componente di ID:" + ID);
            transistorConnectedToPinC = null;

        }
    }


    @Override
    public void removeConnection() {
        if (transistorConnectedToPinA != null) {
            transistorConnectedToPinA.resetPinIfContain(this);
        }
        if (transistorConnectedToPinB != null) {
            transistorConnectedToPinB.resetPinIfContain(this);
        }
        if (transistorConnectedToPinC != null) {
            transistorConnectedToPinC.resetPinIfContain(this);
        }
    }

    @Override
    public Component returnObjName() {
        return this;
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
    public Point inputTarget(int x, int y) { //TODO fix the problem which it has be devided by three
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

    /**
     * @param pin number to get the state of the relative pin; if incorrect it will return the stat e of the transistor
     * @return true if it is on, false otherwise
     */
    @Override
    public boolean getState(int pin) {
        if (pin == pinA) {
            return A;
        }
        if (pin == pinB) {
            return B;
        }
        return C;
    }

    /**
     * sett connection between this component and the component c
     * and if the current comes from the other component (state == true )-> set fromComponentToA/C = ture
     *
     * @param c     the transistor id to set connection with
     * @param pin   pin of this transistor to connect
     * @param state of the pin of the component which connect to
     */
    public void setConnection(Component c, int pin, boolean state) {

        if (pin == pinB) {
            transistorConnectedToPinB = c;
            setState(pinB, state);
        }
        if (pin == pinA) {
            if (state) {
                fromComponentToA = true;
                fromAtoC = true;
            }
            setState(pinA, state);
            transistorConnectedToPinA = c;
        }
        if (pin == pinC) {
            if (state) {
                fromComponentToC = true;
            }
            setState(pinC, state);
            transistorConnectedToPinC = c;
        }
        System.out.println("Connection setted between: " + this + ", and: " + c);
    }

    @Override
    public Boolean isGrounded() {
        return isGrounded;

    }

    /**
     * updated from gnd class when connected or eliminated
     *
     * @param state true = grounded; false otherwise not
     * @param pin   to connect with - if zero then update just the component to the ground State
     */
    @Override
    public void setGrounded(boolean state, int pin) {//TODO a lot of bug
        if (pinA == pin) {
            AisGrounded = state;
            if (AisGrounded) {
                setState(pinA, false);
                return;
            }
            if (transistorConnectedToPinA != null) {
                transistorConnectedToPinA.setGrounded(false, 0);
            }
        }

        if (pinC == pin) {
            CisGrounded = state;
            if (CisGrounded) {
                setState(pinC, false);
                return;
            }
            if (transistorConnectedToPinC != null) {
                transistorConnectedToPinC.setGrounded(false, 0);
            }
        }


        if (!CisGrounded && !AisGrounded) {
            this.isGrounded = false;
        }
        if (transistorConnectedToPinA != null) {
            transistorConnectedToPinA.update();
        }
        if (transistorConnectedToPinC != null) {
            transistorConnectedToPinC.update();
        }

        System.out.println("AisGrounded: " + AisGrounded + ", CisGrounded " + CisGrounded);
    }

    public void update() {//TODO una volta updatato va in loop, evitare che continui ad updatare il componenete che gli ha detto di farlo

        if (BisUpdated) {
        }

        if (AisUpdated) { //
            if (transistorConnectedToPinA != null && transistorConnectedToPinA != toldToUpdate) {
                transistorConnectedToPinA.tellToUpdate(this);
                transistorConnectedToPinA.setState(transistorConnectedToPinA.getPinFromAnotherObj(this), A);
            }
            AisUpdated = false;
        }
        if (CisUpdated) {
            if (transistorConnectedToPinC != null && transistorConnectedToPinC != toldToUpdate) {
                transistorConnectedToPinC.tellToUpdate(this);
                transistorConnectedToPinC.setState(transistorConnectedToPinC.getPinFromAnotherObj(this), C);
            }
            CisUpdated = false;
        }
        toldToUpdate = null;
        System.out.println("ID " + ID + ": (A=" + A + "),(B=" + B + "),(C=" + C + ")");
    }

    /**
     * setState of this transistor's pin with a given state
     *
     * @param pin   to update
     * @param state false or true; state to gave the Pin
     */
    @Override
    public void setState(int pin, boolean state) {
        if (pin == pinA) {
            if (A != state) {
                AisUpdated = true;
                this.A = state;

                if (!A && fromComponentToA) {
                    fromComponentToA = false;
                }
                if (!A && !fromAtoC && B && C) {
                    AisUpdated = false;
                    A = true;
                }

                if (!A && fromAtoC && B && C) {
                    C = false;
                    CisUpdated = true;
                }
                if (A && B && !C) {
                    CisUpdated = true;
                    C = true;
                    fromAtoC = true;
                }
            }
            if (AisGrounded && B) {
                this.isGrounded = true;
                if (C) {
                    CisUpdated = true;
                    C = false;
                }
            }

        } else if (pin == pinB) {//pinB
            if (B != state) {
                BisUpdated = true;
                this.B = state;
                if (state) {//TODO fix sometihing here
                    if (CisGrounded) {
                        isGrounded = true;
                        if (transistorConnectedToPinA != null) {
                            transistorConnectedToPinA.tellToUpdate(this);
                            transistorConnectedToPinA.setGrounded(true, transistorConnectedToPinA.getPinFromAnotherObj(this));
                        }
                        if (A) {
                            AisUpdated = true;
                            A = false;
                        }
                    } else if (AisGrounded) {
                        isGrounded = true;
                        if (transistorConnectedToPinC != null) {
                            transistorConnectedToPinC.tellToUpdate(this);
                            transistorConnectedToPinC.setGrounded(true, transistorConnectedToPinC.getPinFromAnotherObj(this));
                        }
                        if (C) {
                            CisUpdated = true;
                            C = false;
                        }
                    } else {
                        setGrounded(false, 0);
                    }
                    if (A) {//A e B attivi -> C attivo
                        fromAtoC = true;
                        C = true;
                        CisUpdated = true;
                    }
                    if (C) { // B e C attivi -> A attivo
                        fromAtoC = false;
                        A = true;
                        AisUpdated = true;
                    }
                }
                if (!state) { // means off //TODO tell if corrent comes from another component
                    if (A && fromAtoC) {
                        C = false;
                        CisUpdated = true;
                    }
                    if (C && !fromAtoC) {
                        A = false;
                        AisUpdated = true;
                    }
                }
            }
        } else if (pin == pinC) { //pin C
            if (C != state) {
                CisUpdated = true;
                this.C = state;

                if (!C && fromComponentToC) {
                    fromComponentToC = false;
                }
                if (!C && B && fromAtoC && A) {
                    CisUpdated = false;
                    this.C = true;
                }
                if (!C && !fromAtoC && B && A) {
                    A = false;
                    AisUpdated = true;
                }
                if (C && B && !A) {
                    A = true;
                    AisUpdated = true;
                    fromAtoC = false;
                }
            }
            if (CisGrounded && B) {
                this.isGrounded = true;
                if (A) {
                    AisUpdated = true;
                    A = false;
                }
            }
        }
        System.out.println("ID " + ID + ": (A=" + A + "),(B=" + B + "),(C=" + C + ")");
        update();
    }

    /**
     * is needed from another transistor to get the pin where this transistor is connected
     *
     * @param ObgID to compare with
     * @return the number of the pin, A = 3, B = 2, C = 9
     */
    @Override
    public int getPinFromAnotherObj(Component ObgID) {
        if (transistorConnectedToPinA == ObgID) {
            return pinA;
        }
        if (transistorConnectedToPinB == ObgID) {
            return pinB;
        }
        return pinC;
    }

    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
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

    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(A);
        stream.writeBoolean(B);
        stream.writeBoolean(updated);
        stream.writeBoolean(fromAtoC);
        stream.writeBoolean(BisUpdated);
        stream.writeBoolean(AisUpdated);
        stream.writeBoolean(CisUpdated);
        stream.writeBoolean(isGrounded);
        stream.writeBoolean(fromComponentToA);
        stream.writeBoolean(fromComponentToC);
        stream.writeBoolean(AisGrounded);
        stream.writeBoolean(CisGrounded);
        stream.writeBoolean(groundUpdate);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(sizeWidth);
        stream.writeInt(sizeHeight);
        stream.writeInt(ID);
        stream.writeObject(parent);
        stream.writeObject(transistorConnectedToPinA);
        stream.writeObject(transistorConnectedToPinB);
        stream.writeObject(transistorConnectedToPinB);
        stream.writeObject(toldToUpdate);

    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.A = stream.readBoolean();
        this.B = stream.readBoolean();
        this.updated = stream.readBoolean();
        this.fromAtoC = stream.readBoolean();
        this.BisUpdated = stream.readBoolean();
        this.AisUpdated = stream.readBoolean();
        this.CisUpdated = stream.readBoolean();
        this.isGrounded = stream.readBoolean();
        this.fromComponentToA = stream.readBoolean();
        this.fromComponentToC = stream.readBoolean();
        this.AisGrounded = stream.readBoolean();
        this.CisGrounded = stream.readBoolean();
        this.groundUpdate = stream.readBoolean();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.sizeWidth = stream.readInt();
        this.sizeHeight = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
        this.transistorConnectedToPinA = (Component) stream.readObject();
        this.transistorConnectedToPinB = (Component) stream.readObject();
        this.transistorConnectedToPinB = (Component) stream.readObject();
        this.toldToUpdate = (Component) stream.readObject();
        initialize();
    }
}