package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Transistor implements Component, Serializable {
    boolean A, B, C, updated, fromAtoC, BisUpdated, lastState,
            AisUpdated, CisUpdated, isGrounded, AisGrounded, CisGrounded, groundUpdate;

    final int pinA = 3, pinB = 2, pinC = 9;
    final String type = "transistor";
    Image img, imgAB, imgABC, imgAC, imgB, imgBC, imgC, imgA;
    int sizeWidth, sizeHeight, x, y, ID,newConnectionOnPin = 0;
    JPanel parent;
    HashMap<Integer, Component> transistorConnectedToPinA = new HashMap<>();
    HashMap<Integer, Component> transistorConnectedToPinB = new HashMap<>();
    HashMap<Integer, Component> transistorConnectedToPinC = new HashMap<>();
    Component toldToUpdate = null;
    Component toConnect;
    ArrayList<Component> fromComponentToA = new ArrayList();
    ArrayList<Component> fromComponentToC = new ArrayList();

    public Transistor(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / pinB;
        this.y = y - sizeHeight / pinB;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();
        A = B = C = updated = fromAtoC = BisUpdated = AisUpdated = CisUpdated
                = AisGrounded = CisGrounded = groundUpdate = lastState = false;
    }

    //@SuppressWarnings("unchecked")
    private void initialize() {
        BufferedImage imgb, imgbAB, imgbABC, imgbAC, imgbB, imgbBC, imgbC, imgbA;
        String path = System.getProperty("user.dir");
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

    }


    /**
     * tell if the id given is connected to this transistor, if does it disconnect
     * and reset the pin following setConection's rules
     *
     * @param ID is the ID of the element which was allocated with some pin
     */
    public void resetPinIfContain(Component ID) {//TODO fix here forse devi aggiungere da quali componenti arriva la corrente

        if (updateHashMap(transistorConnectedToPinA, ID, fromComponentToA, pinA)){//pinA
            return;
        }
        if(updateHashMap(transistorConnectedToPinB, ID, null, pinB)){//pinB
            return;
        }
        updateHashMap(transistorConnectedToPinC, ID, fromComponentToC, pinC);

    }

    /**
     * this function will remove from fromComponentA/C the element if contained into the hashmap,
     * will check if there is any grounded component and then update if so,
     * will remove from the transistorConnectedToPinA/C the component
     *
     * @param hashMap           to check and delate the component
     * @param toCompare         the component that you want to eliminate
     * @param arrayOfComponents array of components to check -> fromComponentA/C
     */
    private boolean updateHashMap(HashMap<Integer, Component> hashMap, Component toCompare, ArrayList<Component> arrayOfComponents, int pin) {
        boolean grounded = false, containsTheComponent = false, returnValue = false;
        int hashMapSize = hashMap.size();
        for (Component c : hashMap.values()) {
            if (c == toCompare) {//devo controllare se ci sono altri componenti da cui arriva la corrente
                containsTheComponent = true;
            }
            if (arrayOfComponents != null && pin != pinB && arrayOfComponents.contains(toCompare)) {
                arrayOfComponents.remove(toCompare);
            }
            if (c != toCompare && c.getType().equalsIgnoreCase("gnd")) {
                grounded = true;
            }
        }
        if (arrayOfComponents != null && pin != pinB && arrayOfComponents.size() == 0) {
            setState(pin, false);
        }

        if (!grounded) {
            isGrounded = false;
        }
        if (containsTheComponent) {
            hashMap.remove(toCompare.getIDComponent());
            returnValue = true;
            if (pin == pinB && hashMap.size() == 0){
                setState(pin, false);
            }

        }
        return returnValue;
    }

    /**
     * called when this object has been deleted
     */
    @Override
    public void removeConnection() {
        removeComponentFromHashMap(transistorConnectedToPinA);
        removeComponentFromHashMap(transistorConnectedToPinB);
        removeComponentFromHashMap(transistorConnectedToPinC);
    }

    /**
     * aggiorno i pin dei componenti collegati alla funzione resetPinIfContained()
     * che veruifica se al suo interno è presente questo componente
     *
     * @param hm HashMap of the pin to check
     */
    private void removeComponentFromHashMap(HashMap<Integer, Component> hm) {
        for (Component c : hm.values()) {
            c.resetPinIfContain(this);
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
        lastState = state;
        toConnect = c;
        if (pin == pinB) {
            transistorConnectedToPinB.put(c.getIDComponent(), c.returnObjName());
            newConnectionOnPin = pinB;
        }
        if (pin == pinA) {
            transistorConnectedToPinA.put(c.getIDComponent(), c.returnObjName());
            newConnectionOnPin = pinA;
        }
        if (pin == pinC) {
            transistorConnectedToPinC.put(c.getIDComponent(), c.returnObjName());
            newConnectionOnPin = pinC;
            if (state) {
                fromComponentToC.add(c);
                setState(pinC, true);
            }
        }
        //System.out.println("Connection setted between: " + this + ", and: " + c);
    }

    @Override
    public void updateAfterConnection() {
        if(newConnectionOnPin == pinA){
            if (lastState){
                fromComponentToA.add(toConnect);
                fromAtoC = true;
                setState(pinA, true);
            }
        }
        if (newConnectionOnPin == pinB){
            tellToUpdate(this);
            setState(pinB, lastState);

        }
        if (newConnectionOnPin == pinC){
            if (lastState) {
                fromComponentToC.add(toConnect);
                setState(pinC, true);
            }
        }
    }


    @Override
    public Boolean isGrounded() {
        return isGrounded;

    }

    /**
     * updated from gnd and other transistors when connected to gnd or eliminated
     *
     * if AisGrounded it will update the pin A and it's component connected to it
     * otherwise it will be false and will check/update the state of ground checking every component before
     *
     * fro CisGrounded it is the same as AisGrounded
     * <p>
     * when setState() will call this setGrounded in this function the connected pin will be updated to isGrounded = false
     *
     * @param state true = grounded; false otherwise not
     * @param pin   to connect with - if zero then update just the component to the ground State
     */
    @Override
    public void setGrounded(boolean state, int pin) {

        if (pinA == pin) {
            AisGrounded = state;
            if (AisGrounded) {
                setState(pinA, false);
                updateHashMapToGround(transistorConnectedToPinA);
                return;
            }
            if (!checkIfGrounded(transistorConnectedToPinA)) {
                for (Component c : transistorConnectedToPinA.values()) {
                    c.setGrounded(false, 0);
                }

            }
        }

        if (pinC == pin) {
            CisGrounded = state;
            if (CisGrounded) {
                setState(pinC, false);
                updateHashMapToGround(transistorConnectedToPinC);
                return;
            }//controllo se oltre al gnd che rimuovo ce ne sono altri connessi in parallelo
            if (!checkIfGrounded(transistorConnectedToPinC)) {
                for (Component c : transistorConnectedToPinC.values()) {
                    c.setGrounded(false, 0);
                }
            }
        }


        if (!CisGrounded && !AisGrounded) {
            this.isGrounded = false;
        }

        tellToUpdateAfterGrounding(transistorConnectedToPinA);
        tellToUpdateAfterGrounding(transistorConnectedToPinC);
        //System.out.println("AisGrounded: " + AisGrounded + ", CisGrounded " + CisGrounded);
    }

    /**
     * controllo se oltre al gnd che rimuovo ce ne sono altri connessi in parallelo
     *
     * @param hashMap to check
     * @return false if none of the component inside the hashMap is grouned, true otherwise
     */
    private boolean checkIfGrounded(HashMap<Integer, Component> hashMap) {
        boolean grounded = false;
        for (Component c : hashMap.values()) {
            if (c.isGrounded() && toldToUpdate != c) {
                grounded = true;
            }
        }
        return grounded;

    }

    /**
     * will update every object to ground
     *
     * @param hashMap of the component connected to the pin
     */
    private void updateHashMapToGround(HashMap<Integer, Component> hashMap) {
        for (Component c : hashMap.values()) {
            if (c != toldToUpdate) {
                c.tellToUpdate(this);
                c.setGrounded(true, c.getPinFromAnotherObj(this));
            }
        }

    }

    /**
     * update the hashMap passed after being grounded
     *
     * @param hashMap to updated pin to ground
     */
    private void tellToUpdateAfterGrounding(HashMap<Integer, Component> hashMap) {//TODO probabilmente è inutile eccetto per l'update
        for (Component c : hashMap.values()) {
            if (c.getType().equalsIgnoreCase("transistor")) {
                if (c.isGrounded() && !isGrounded) {
                    c.setGrounded(false, c.getPinFromAnotherObj(this));
                }
            } else {
                if (c != toldToUpdate ){
                    c.update();
                }
            }
        }
    }

    public void update() {

        if (BisUpdated) {
            updateTransistor(transistorConnectedToPinB, B);
            BisUpdated = false;
        }

        if (AisUpdated) { //
            updateTransistor(transistorConnectedToPinA, A);
            AisUpdated = false;
        }
        if (CisUpdated) {
            updateTransistor(transistorConnectedToPinC, C);
            CisUpdated = false;
        }
        toldToUpdate = null;
    }

    private void updateTransistor(HashMap<Integer, Component> hashMap, boolean pinState) {
        for (Component c : hashMap.values()) {
            if (c != toldToUpdate) {
                c.tellToUpdate(this);
                c.setState(c.getPinFromAnotherObj(this), pinState);
            }
        }
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
                if (!A && fromComponentToA.size() == 0) {
                    if (fromAtoC) {
                        CisUpdated = true;
                        C = false;
                    }
                }
                if (!A && !fromAtoC && B && C) {
                    AisUpdated = false;
                    A = true;
                }

                if (!A && fromAtoC && B && C) {
                    C = false;
                    CisUpdated = true;
                }
                if (A && !B && !C) {
                    fromAtoC = true;
                }
                if (A && B && !C) {
                    fromAtoC = true;
                    C = true;
                    CisUpdated = true;
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
                if (state) {//B = true
                    if (CisGrounded) {
                        isGrounded = true;
                        for (Component componentA : transistorConnectedToPinA.values()) {
                            componentA.tellToUpdate(this);
                            componentA.setGrounded(true, componentA.getPinFromAnotherObj(this));
                        }
                        if (A) {
                            AisUpdated = true;
                            A = false;
                        }
                    } else if (AisGrounded) {
                        isGrounded = true;
                        for (Component componentC : transistorConnectedToPinC.values()) {
                            componentC.tellToUpdate(this);
                            componentC.setGrounded(true, componentC.getPinFromAnotherObj(this));
                        }
                        if (C) {
                            CisUpdated = true;
                            C = false;
                        }
                    } else {
                        if (isGrounded){
                            setGrounded(false, 0);
                        }
                    }
                    if (A && fromComponentToA.size() != 0) {//A e B attivi -> C attivo
                        fromAtoC = true;
                        C = true;
                        CisUpdated = true;
                    }
                    if (C && fromComponentToC.size() != 0) { // B e C attivi -> A attivo
                        fromAtoC = false;
                        A = true;
                        AisUpdated = true;
                    }
                    if (C) {
                        A = true;
                        AisUpdated = true;
                    }
                    if (A) {
                        C = true;
                        CisUpdated = true;
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
                    if (isGrounded && fromComponentToA.size() != 0) {
                        this.isGrounded = false;
                        AisUpdated = true;
                        A = true;
                    }
                    if (isGrounded && fromComponentToC.size() != 0) {
                        this.isGrounded = false;
                        CisUpdated = true;
                        C = true;
                    }
                }
            }
        } else if (pin == pinC) { //pin C
            if (C != state) {
                CisUpdated = true;
                this.C = state;
                if (!C && fromComponentToC.size() == 0) {
                    if (!fromAtoC) {
                        AisUpdated = true;
                        A = false;
                    }
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
                if (C && !B && !A) {
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
        //System.out.println("ID " + ID + ": (A=" + A + "),(B=" + B + "),(C=" + C + ")");
        update();
    }

    /**
     * is needed from another transistor to get the pin where this transistor is connected
     *
     * @param obgID to compare with
     * @return the number of the pin, A = 3, B = 2, C = 9
     */
    @Override
    public int getPinFromAnotherObj(Component obgID) {

        if (isContainedIn(transistorConnectedToPinA, obgID)) {
            return pinA;
        }
        if (isContainedIn(transistorConnectedToPinB, obgID)) {
            return pinB;
        }
        return pinC;
    }

    private boolean isContainedIn(HashMap<Integer, Component> hashMap, Component obgToCheck) {
        for (Component c : hashMap.values()) {
            if (c == obgToCheck) {
                return true;
            }
        }
        return false;
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
        stream.writeBoolean(C);
        stream.writeBoolean(updated);
        stream.writeBoolean(fromAtoC);
        stream.writeBoolean(BisUpdated);
        stream.writeBoolean(AisUpdated);
        stream.writeBoolean(CisUpdated);
        stream.writeBoolean(isGrounded);
        stream.writeObject(fromComponentToA);
        stream.writeObject(fromComponentToC);
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
        stream.writeObject(transistorConnectedToPinC);
        stream.writeObject(toldToUpdate);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.A = stream.readBoolean();
        this.B = stream.readBoolean();
        this.C = stream.readBoolean();
        this.updated = stream.readBoolean();
        this.fromAtoC = stream.readBoolean();
        this.BisUpdated = stream.readBoolean();
        this.AisUpdated = stream.readBoolean();
        this.CisUpdated = stream.readBoolean();
        this.isGrounded = stream.readBoolean();
        this.fromComponentToA = (ArrayList) stream.readObject();
        this.fromComponentToC = (ArrayList) stream.readObject();
        this.AisGrounded = stream.readBoolean();
        this.CisGrounded = stream.readBoolean();
        this.groundUpdate = stream.readBoolean();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.sizeWidth = stream.readInt();
        this.sizeHeight = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
        this.transistorConnectedToPinA = (HashMap) stream.readObject();
        this.transistorConnectedToPinB = (HashMap) stream.readObject();
        this.transistorConnectedToPinC = (HashMap) stream.readObject();
        this.toldToUpdate = (Component) stream.readObject();
        initialize();
    }

    @Override
    public String toString() {
        return "Transistor{" + "A=" + A + ", B=" + B + ", C=" + C + ", fromAtoC=" + fromAtoC +
                ", isGrounded=" + isGrounded + ", AisGrounded=" + AisGrounded +
                ", CisGrounded=" + CisGrounded + ", groundUpdate=" + groundUpdate + ", ID=" + ID +
                ", toldToUpdate=" + toldToUpdate + '}';
    }
}