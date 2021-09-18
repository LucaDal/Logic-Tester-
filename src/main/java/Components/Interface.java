package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;


public class Interface extends JPanel implements MouseListener, MouseMotionListener {
    Dimension size = new Dimension(900, 700);
    HashMap<Integer, Component> componentMap = new HashMap<>();
    int IDComponent = 1;
    boolean transistorToSet = false, vccToSet = false, gndToSet = false, deleteIsSelected = false,
            selectIsSelected = false, setConnection = false;
    Point tempConnectionPointFirstCall = new Point();
    Point tempConnectionPointSecondCall = new Point();

//  store the first clicked component id and the second one


    public Interface() {

        setPreferredSize(size);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Component c : componentMap.values()) {
            c.paint(g);
        }
    }

    /**
     * used to evaluate if a coordinate is located between two point
     *
     * @param coordinateMouse     - insert X or Y coordinate from the Mouse
     * @param coordinateComponent - insert X or Y coordinate from the origin of the image to evaluate
     * @param size                - the width or the height to add to coordinateComponenet
     * @return true if between the coordinates, false otherwise
     */
    public boolean isBetween(int coordinateMouse, int coordinateComponent, int size) {
        if (coordinateMouse >= coordinateComponent && coordinateMouse <= (coordinateComponent + size)) {
            return true;
        }
        return false;
    }

    /**
     * This function is used to say if the click of the mouse is over a component or not.
     * if so, it will return the componentIDComponent (the area evaluated is rectangular)
     *
     * @param e - the MouseEvent given
     * @return the IDComponent of the component clicked. if 0 then no component is checkMouseOverComponent()ed
     */
    public int checkMouseOverComponent(MouseEvent e) {
//            System.out.println("Posizione origine elemento: X =" + c.getPosition().x + " Y =" + c.getPosition().y + "size ="+c.getSizeWidth());
        for (Component c : componentMap.values()) {
            if (isBetween(e.getX(), c.getPosition().x, c.getSizeWidth()) && isBetween(e.getY(), c.getPosition().y, c.getSizeHeight())) {
                return c.getIDComponent();
            }
        }
        return 0;
    }


    @Override
    public void mouseDragged(MouseEvent e) {//TODO moving Components
/*        System.out.println("X =" + e.getX() + " Y =" + e.getY());
        int IDComponent =checkMouseOverComponent()(new Point(e.getX(),e.getY()));
        System.out.println("IDComponent tornato: "+IDComponent);*/
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (transistorToSet) {
            System.out.println("adding a transistor n :" + IDComponent);
            componentMap.put(IDComponent, new Transistor(this, IDComponent, e.getX(), e.getY(), 30, 30));
            transistorToSet = false;
            IDComponent++;
        }
        if (vccToSet) {
            System.out.println("adding a Vcc plug");
        }
        if (vccToSet) {
            System.out.println("adding a Vcc plug");
            componentMap.put(IDComponent, new Vcc(this, IDComponent, e.getX(), e.getY(), 20, 20));
            vccToSet = false;
            IDComponent++;
        }
        if (gndToSet) {
            System.out.println("adding a gnd plug");
            componentMap.put(IDComponent, new Gnd(this, IDComponent, e.getX(), e.getY(), 25, 25));
            gndToSet = false;
            IDComponent++;
        }
        if (deleteIsSelected) {
            int IdReturned = checkMouseOverComponent(e);
            if (IdReturned != 0)
                componentMap.remove(IdReturned);
            //per i transistor 3 cicli
            for (Component c : componentMap.values()) {
                if (c.getType().equalsIgnoreCase("transistor")) {
                    if (!(c.getIDComponent() == IdReturned)) {
                        c.contains(IdReturned);//if does it delete the connection
                    }
                }
            }
        }
        if (selectIsSelected) {
            int IdReturned = checkMouseOverComponent(e);
            //System.out.println("IDComponent tornato: " + IdReturned);
            if (IdReturned != 0) {
                if (!setConnection) {
                    if ((tempConnectionPointFirstCall = componentMap.get(IdReturned).inputTarget(e.getX(), e.getY())).y < 10) {
                        setConnection = true;
                        System.out.println("initialiting connection between component ID: " + tempConnectionPointFirstCall.x + ", pin: " + tempConnectionPointFirstCall.y);
                    }
                } else {
                    System.out.println("set connection to false, ritorno: " + (componentMap.get(IdReturned).inputTarget(e.getX(), e.getY())).y);
                    if ((tempConnectionPointSecondCall = componentMap.get(IdReturned).inputTarget(e.getX(), e.getY())).y < 10) {
                        System.out.println("and component ID: " + tempConnectionPointSecondCall.x + ", pin: " + tempConnectionPointSecondCall.y);
                        update(tempConnectionPointFirstCall, tempConnectionPointSecondCall);
                    }
                    setConnection = false;
                }
            }

        }
        repaint();
    }

    public void update(Point firstComponent, Point secondComponent) {

        //.x = ID ; .y = pin
        if (!componentMap.get(firstComponent.x).getType().equalsIgnoreCase("transistor")) {
            componentMap.get(secondComponent.x).setState(secondComponent.y, true);
            componentMap.get(secondComponent.x).setConnection(new Point(firstComponent.x, secondComponent.y));
            return;
        }
        if (!componentMap.get(secondComponent.x).getType().equalsIgnoreCase("transistor")) {
            componentMap.get(firstComponent.x).setState(firstComponent.y, true);
            componentMap.get(firstComponent.x).setConnection(new Point(secondComponent.x, firstComponent.y));
            return;
        }
        if (componentMap.get(firstComponent.x).getState(firstComponent.y)) {
            componentMap.get(secondComponent.x).setState(secondComponent.y, true);

        }
        if (componentMap.get(secondComponent.x).getState(secondComponent.y)) {
            componentMap.get(firstComponent.x).setState(firstComponent.y, true);
        }
        /*setto i pin per entrambi i transistor*/
        componentMap.get(firstComponent.x).setConnection(new Point(secondComponent.x, firstComponent.y));
        componentMap.get(secondComponent.x).setConnection(new Point(firstComponent.x, secondComponent.y));

    }

    public void delete() {
        deleteIsSelected = true;
        selectIsSelected = false;
    }

    /**
     * if called this function will set the parameter transistorToSet to true
     * and then after clicking on the jPanel it will rapresent the image
     */
    public void addTransistor() { //TODO change the way of implementing a system of transistor if more then one
        selectIsSelected = false;
        deleteIsSelected = false;
        transistorToSet = true;
    }

    /**
     * Add a Vcc plug to the pnanel
     */
    public void addVcc() {
        selectIsSelected = false;
        deleteIsSelected = false;
        vccToSet = true;
    }

    public void addGnd() {
        selectIsSelected = false;
        deleteIsSelected = false;
        gndToSet = true;
    }

    public void select() {
        deleteIsSelected = false;
        selectIsSelected = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
