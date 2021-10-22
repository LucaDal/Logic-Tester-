package Components;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Transistor implements Component, Serializable {
    @Serial
    private static final long serialVersionUID = -8082050773708616884L;
    private boolean A, B, C, updated, fromAtoC, BisUpdated, lastState,
            AisUpdated, CisUpdated, isGrounded, AisGrounded, CisGrounded, groundUpdate, BisGrounded;

    static final int pinA = 3, pinB = 2, pinC = 9;
    private Image img, imgAB, imgABC, imgAC, imgB, imgBC, imgC, imgA;
    private int sizeWidth, sizeHeight, x, y, ID, newConnectionOnPin = 0;
    private JPanel parent;
    private Component toldToUpdate = null;
    private Component toConnect;
    private ArrayList<Component> fromComponentToA = new ArrayList<>();
    private ArrayList<Component> fromComponentToC = new ArrayList<>();
    private Multimap<Integer, ComponentAndRelativePin> transistorConnectedToPinA = ArrayListMultimap.create();
    private Multimap<Integer, ComponentAndRelativePin> transistorConnectedToPinB = ArrayListMultimap.create();
    private Multimap<Integer, ComponentAndRelativePin> transistorConnectedToPinC = ArrayListMultimap.create();

    public Transistor(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / pinB;//TODO weird typo
        this.y = y - sizeHeight / pinB;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
        initialize();
        A = B = C = updated = fromAtoC = BisUpdated = AisUpdated = CisUpdated
                = AisGrounded = CisGrounded = groundUpdate = lastState = BisGrounded = false;
    }

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
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public boolean getGroundedPin(int pin) {
        if (pin == pinA) {
            return AisGrounded;
        }
        if (pin == pinB) {
            return BisGrounded;
        }
        return CisGrounded;
    }

    @Override
    public boolean hasGndConnected(int pin) {
        if (pin == pinA) {
            if (B) {
                return isConnectedToAGnd(transistorConnectedToPinA) && isConnectedToAGnd(transistorConnectedToPinC);
            }
            return isConnectedToAGnd(transistorConnectedToPinA);
        }
        if (pin == pinB) {
            return isConnectedToAGnd(transistorConnectedToPinB);
        }
        if (B) {
            return isConnectedToAGnd(transistorConnectedToPinC) && isConnectedToAGnd(transistorConnectedToPinA);
        }
        return isConnectedToAGnd(transistorConnectedToPinC);
    }

    private boolean isConnectedToAGnd(Multimap<Integer, ComponentAndRelativePin> multiMap) {
        for (ComponentAndRelativePin cp : multiMap.values()) {
            if (cp.getComponent().getType().equals("gnd")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getPinState(int pin) {
        if (pin == pinA) {
            return A;
        }
        if (pin == pinB) {
            return B;
        }
        return C;
    }

    /**
     * tell if the id given is connected to this transistor, if does it disconnect
     * and reset the pin following setConection's rules
     * <p>
     * da non ottimizzare updateHashMap con un if (return true se è il pin esatto) dato che possono esserci doppioni
     *
     * @param ID is the ID of the element which was allocated with some pin
     */
    public void resetPinIfContain(Component ID) {//TODO fix here forse devi aggiungere da quali componenti arriva la corrente

        updateHashMap(transistorConnectedToPinA, ID, fromComponentToA, pinA);
        updateHashMap(transistorConnectedToPinB, ID, null, pinB);
        updateHashMap(transistorConnectedToPinC, ID, fromComponentToC, pinC);

    }

    /**
     * this function will remove from fromComponentA/C the element if contained into the hashmap,
     * will check if there is any grounded component and then update if so,
     * will remove from the transistorConnectedToPinA/C the component
     *
     * @param multiMap          to check and delate the component
     * @param toCompare         the component that you want to eliminate
     * @param arrayOfComponents array of components to check -> fromComponentA/C
     */
    private void updateHashMap(Multimap<Integer, ComponentAndRelativePin> multiMap, Component toCompare, ArrayList<Component> arrayOfComponents, int pin) {
        boolean grounded = false, containsTheComponent = false;
        ComponentAndRelativePin toDelete = null;
        for (ComponentAndRelativePin cp : multiMap.values()) {
            Component temp = cp.getComponent();
            if (temp == toCompare) {//devo controllare se ci sono altri componenti da cui arriva la corrente
                containsTheComponent = true;
                toDelete = cp;
            }
            if (arrayOfComponents != null && pin != pinB) {
                arrayOfComponents.remove(toCompare);
            }
            if (temp != toCompare && temp.getType().equals("gnd")) {
                grounded = true;
            }
        }

        if (!grounded) {
            isGrounded = false;
        }
        if (containsTheComponent) {
            multiMap.remove(toCompare.getIDComponent(), toDelete);
            if (pin == pinB && multiMap.size() == 0) {
                setState(pin, false);
            }

        }
        if (getState(pin)){
            boolean connectedToVcc = false;
            for (ComponentAndRelativePin cp : multiMap.values()) {
                Component temp = cp.getComponent();
                temp.tellToUpdate(this);
                if (temp.checkIfConnectedPinAreUnderVcc(cp.getPin())) {
                    connectedToVcc = true;
                    temp.tellToUpdate(null);
                    break;
                }
                temp.tellToUpdate(null);
            }
            setState(pin, connectedToVcc);
        }

    }

    public boolean checkIfConnectedPinAreUnderVcc(int pin) {
        if (pin == pinA) {
            if (B && C) {
                return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinA) && checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinC);
            }
            return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinA);
        }
        if (pin == pinB) {
            checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinB);
        }
        if (B && A) {
            return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinC) && checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinA);
        }
        return checkIfConnectedPinAreUnderVccPerPin(transistorConnectedToPinC);
    }

    /**
     * @param multiMap of the componentMap to check
     * @return true if a pin is connected to a vcc which is not grounded
     */
    private boolean checkIfConnectedPinAreUnderVccPerPin(Multimap<Integer, ComponentAndRelativePin> multiMap) {
        boolean flagState = false;
        for (ComponentAndRelativePin cp : multiMap.values()) {
            if (cp.getComponent() != toldToUpdate) {
                if (cp.getComponent().getType().equals("vcc") && !cp.getComponent().isGrounded()) {
                    flagState = true;
                    break;
                }
            }
        }
        return flagState;
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
    private void removeComponentFromHashMap(Multimap<Integer, ComponentAndRelativePin> hm) {
        for (ComponentAndRelativePin cp : hm.values()) {
            Component temp = cp.getComponent();
            temp.tellToUpdate(this);
            temp.resetPinIfContain(this);
            temp.tellToUpdate(null);
        }
        hm.clear();
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
        return new Point(ID, 0);
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
     * @param c        the transistor id to set connection with
     * @param pin      pin of this transistor to connect
     * @param otherPin pin of the other component connected to the pin of this transistor
     * @param state    of the pin of the component which connect to
     * @return true if the connection is possible, false otherwise
     */
    public boolean setConnection(Component c, int pin, int otherPin, boolean state) {
        lastState = state;
        toConnect = c;
        if (c.getGroundedPin(otherPin)) {
            setGrounded(true, pin);
        }

        if (pin == pinB) {
            if (B) {
                lastState = true;
            }
            transistorConnectedToPinB.put(c.getIDComponent(), new ComponentAndRelativePin(c, otherPin));
            newConnectionOnPin = pinB;
        } else if (pin == pinA) {
            if (A) {
                lastState = true;
            }
            transistorConnectedToPinA.put(c.getIDComponent(), new ComponentAndRelativePin(c, otherPin));
            newConnectionOnPin = pinA;
        } else if (pin == pinC) {
            if (C) {
                lastState = true;
            }
            transistorConnectedToPinC.put(c.getIDComponent(), new ComponentAndRelativePin(c, otherPin));
            newConnectionOnPin = pinC;
        }
        return true;
    }

    @Override
    public void removeConnectionFromPins(int pin) {//TODO attenzione nell'implementazione del pnp
        if (pin == pinA) {
            removeComponentFromHashMap(transistorConnectedToPinA);
            if (AisGrounded) {
                setGrounded(false, pinA);
            }
            if (fromAtoC && A) {
                setState(pinA, false);
            } else if (C) {
                setState(pinA, false);
            }
        }
        if (pin == pinB) {
            removeComponentFromHashMap(transistorConnectedToPinB);
            if (BisGrounded && B) {
                setGrounded(false, pinB);
            }
            setState(pinB, false);//TODO fix for pnp transistor
        }
        if (pin == pinC) {
            removeComponentFromHashMap(transistorConnectedToPinC);
            if (CisGrounded) {
                setGrounded(false, pinC);
            }
            if (!fromAtoC && C) {
                setState(pinC, false);
            } else if (A) {
                setState(pinC, false);
            }
        }
    }

    @Override
    public void updateAfterConnection() {
        if (newConnectionOnPin == pinA) {
            if (AisGrounded) {
                lastState = false;
            }
            if (lastState) {
                fromComponentToA.add(toConnect);
                fromAtoC = true;
                setState(pinA, true);
            }
        }
        if (newConnectionOnPin == pinB) {
            if (BisGrounded) {
                lastState = false;
            }
            setState(pinB, lastState);
        }
        if (newConnectionOnPin == pinC) {
            if (CisGrounded) {
                lastState = false;
            }
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
     * <p>
     * if AisGrounded it will update the pin A and it's component connected to it
     * otherwise it will be false and will check/update the state of ground checking every component before
     * <p>
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
            if (B && AisGrounded && !state) {//parte simile per il pinB attenzione nel debug -Prof non ci faccia caso pls <3
                if (!CisGrounded) {
                    isGrounded = false;
                    AisGrounded = false;
                }
                if (checkIfGrounded(transistorConnectedToPinC)) {
                    updateHashMapToGroundOrNot(transistorConnectedToPinC, true);
                }
            }
            AisGrounded = state;
            if (AisGrounded) {
                this.isGrounded = true;
                updateHashMapToGroundOrNot(transistorConnectedToPinA, true);
                if (B && !CisGrounded) {
                    updateHashMapToGroundOrNot(transistorConnectedToPinC, true);
                    setState(pinC, false);
                }
                setState(pinA, false);
                return;
            }
            if (!checkIfGrounded(transistorConnectedToPinA)) {
                updateHashMapToGroundOrNot(transistorConnectedToPinA, false);

            }
        }
        if (pin == pinB) {
            this.isGrounded = state;
            BisGrounded = state;
            updateHashMapToGroundOrNot(transistorConnectedToPinB, state);
        }
        //PARTE SPECULARE AD A
        if (pinC == pin) {
            if (B && CisGrounded && !state) {
                if (!AisGrounded) {
                    isGrounded = false;
                }
                if (checkIfGrounded(transistorConnectedToPinA)) {
                    updateHashMapToGroundOrNot(transistorConnectedToPinA, true);
                }
            }
            CisGrounded = state;
            if (CisGrounded) {
                this.isGrounded = true;
                updateHashMapToGroundOrNot(transistorConnectedToPinC, true);
                if (B && !AisGrounded) {
                    updateHashMapToGroundOrNot(transistorConnectedToPinA, true);
                    setState(pinA, false);
                }
                setState(pinC, false);
                return;
            }//controllo se oltre al gnd che rimuovo ce ne sono altri connessi in parallelo
            if (!checkIfGrounded(transistorConnectedToPinC)) {
                updateHashMapToGroundOrNot(transistorConnectedToPinC, false);
            }
        }
        if (!CisGrounded && !AisGrounded && !BisGrounded) {
            this.isGrounded = false;
        }

        tellToUpdateAfterGrounding(transistorConnectedToPinA);
        tellToUpdateAfterGrounding(transistorConnectedToPinC);
    }

    /**
     * controllo se oltre al gnd che rimuovo ce ne sono altri connessi in parallelo
     *
     * @param multiMap to check
     * @return false if none of the component inside the multiMap is grouned, true otherwise
     */
    private boolean checkIfGrounded(Multimap<Integer, ComponentAndRelativePin> multiMap) {
        boolean grounded = false;
        for (ComponentAndRelativePin cp : multiMap.values()) {
            Component temp = cp.getComponent();
            if (temp.isGrounded() && toldToUpdate != temp) {
                grounded = true;
            }
        }
        return grounded;

    }

    /**
     * will update every object to ground
     *
     * @param multiMap of the component connected to the pin
     * @param state    to set to other pin
     */
    private void updateHashMapToGroundOrNot(Multimap<Integer, ComponentAndRelativePin> multiMap, boolean state) {
        for (ComponentAndRelativePin cp : multiMap.values()) {
            Component temp = cp.getComponent();
            if (temp != toldToUpdate) {
                temp.tellToUpdate(this);
                temp.setGrounded(state, cp.getPin());
                temp.tellToUpdate(null);
            }
        }
    }

    /**
     * update the multiMap passed after being grounded
     *
     * @param multiMap to updated pin to ground
     */
    private void tellToUpdateAfterGrounding(Multimap<Integer, ComponentAndRelativePin> multiMap) {//TODO probabilmente è inutile eccetto per l'update
        for (ComponentAndRelativePin cp : multiMap.values()) {
            Component temp = cp.getComponent();
            if (temp.getType().equalsIgnoreCase("transistor")) {
                if (temp.isGrounded() && !isGrounded) {
                    temp.tellToUpdate(this);
                    temp.setGrounded(false, cp.getPin());
                    temp.tellToUpdate(null);
                }
            } else {
                if (temp != toldToUpdate) {
                    temp.update();
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

    /**
     * ho ibserito un if dove controllo se sa diverso da vcc
     * questo perche quando rimuovo le connessioni da un pin non voglio che mi vada a riaggiornare i pin a true
     */
    private void updateTransistor(Multimap<Integer, ComponentAndRelativePin> multiMap, boolean pinState) {
        for (ComponentAndRelativePin cp : multiMap.values()) {
            Component temp = cp.getComponent();
            if (!temp.getType().equalsIgnoreCase("vcc")) {
                if (temp != toldToUpdate) {
                    temp.tellToUpdate(this);
                    temp.setState(cp.getPin(), pinState);
                    temp.tellToUpdate(null);
                }
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
                    //fromComponentToA.add();
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
                        updateHashMapToGroundOrNot(transistorConnectedToPinA, true);
                        if (A) {
                            AisUpdated = true;
                            A = false;
                        }
                    } else if (AisGrounded) {
                        isGrounded = true;
                        updateHashMapToGroundOrNot(transistorConnectedToPinC, true);
                        if (C) {
                            CisUpdated = true;
                            C = false;
                        }
                    } else {
                        if (isGrounded) {
                            setGrounded(false, 0);
                        }
                    }
                    if (A && checkIfConnectedPinAreUnderVcc(pinA)) {//A e B attivi -> C attivo
                        fromAtoC = true;
                        C = true;
                        CisUpdated = true;
                    }
                    if (C && checkIfConnectedPinAreUnderVcc(pinA)) { // B e C attivi -> A attivo
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
                if (!state) {
                    if (A && fromAtoC) {
                        C = false;
                        CisUpdated = true;
                    }
                    if (C && !fromAtoC) {
                        A = false;
                        AisUpdated = true;
                    }
/*                    if (isGrounded && checkIfConnectedPinAreOn(transistorConnectedToPinA)) {
                        this.isGrounded = false;
                        AisUpdated = true;
                        A = true;
                    }
                    if (isGrounded && checkIfConnectedPinAreOn(transistorConnectedToPinC)) {
                        this.isGrounded = false;
                        CisUpdated = true;
                        C = true;
                    }*/
                    if (AisGrounded && !CisGrounded) {
                        updateHashMapToGroundOrNot(transistorConnectedToPinC, false);
                    }

                    if (CisGrounded && !AisGrounded) {
                        updateHashMapToGroundOrNot(transistorConnectedToPinA, false);
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


    @Override
    public void tellToUpdate(Component fromThisComponent) {
        this.toldToUpdate = fromThisComponent;
    }

    @Override
    public String getType() {
        return "transistor";
    }

    public void paintComponent(Graphics g) {
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
    @SuppressWarnings("unchecked")
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
        this.fromComponentToA = (ArrayList<Component>) stream.readObject();
        this.fromComponentToC = (ArrayList<Component>) stream.readObject();
        this.AisGrounded = stream.readBoolean();
        this.CisGrounded = stream.readBoolean();
        this.groundUpdate = stream.readBoolean();
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.sizeWidth = stream.readInt();
        this.sizeHeight = stream.readInt();
        this.ID = stream.readInt();
        this.parent = (JPanel) stream.readObject();
        this.transistorConnectedToPinA = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
        this.transistorConnectedToPinB = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
        this.transistorConnectedToPinC = (Multimap<Integer, ComponentAndRelativePin>) stream.readObject();
        this.toldToUpdate = (Component) stream.readObject();
        initialize();
    }

    @Override
    public String toString() {
        return "Transistor{" + "A=" + A + ", B=" + B + ", C=" + C + ", fromAtoC=" + fromAtoC +
                ", isGrounded=" + isGrounded + ", AisGrounded=" + AisGrounded +
                ", CisGrounded=" + CisGrounded + ", BisGrounded=" + BisGrounded + ", groundUpdate=" + groundUpdate + ", ID=" + ID +
                ", toldToUpdate=" + toldToUpdate + '}';
    }
}