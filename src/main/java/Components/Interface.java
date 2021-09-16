package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Interface extends JPanel implements MouseListener, MouseMotionListener {
    Dimension size = new Dimension(900, 600);
    HashMap<Integer, Component> componentMap = new HashMap<>();
    int IDComponent = 1;
    boolean transistorToSet = false, vccToSet = false, gndToSet = false, deleteIsSelected = false,
            selectIsSelected = false;

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
     * used to evaluate if a coordinate is located between a specified segment
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
     * @param mousePosition - the point which indicate the coordinate of the click of the Mouse
     * @return the IDComponent of the component clicked. if 0 then no component is checked
     */
    public int check(Point mousePosition) {
        for (Component c : componentMap.values()) {
//            System.out.println("Posizione origine elemento: X =" + c.getPosition().x + " Y =" + c.getPosition().y + "size ="+c.getSizeWidth());
            if (isBetween(mousePosition.x, c.getPosition().x, c.getSizeWidth()) && isBetween(mousePosition.y, c.getPosition().y, c.getSizeHeight())) {
                return c.getIDComponent();
            }
        }
        return 0;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
/*        System.out.println("X =" + e.getX() + " Y =" + e.getY());
        int IDComponent =check(new Point(e.getX(),e.getY()));
        System.out.println("IDComponent tornato: "+IDComponent);*/
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("X =" + e.getX() + " Y =" + e.getY());
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
            componentMap.put(IDComponent, new Vcc(this, IDComponent, e.getX(), e.getY(), 25, 25));
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
            int IdReturned = check(new Point(e.getX(), e.getY()));
            if (IdReturned != 0)
                componentMap.remove(IdReturned);
        }
        if (selectIsSelected) {
            int IdReturned = check(new Point(e.getX(), e.getY()));
            System.out.println("IDComponent tornato: " + IdReturned + '\n');
        }

        repaint();
    }

    public void delete() {
        deleteIsSelected = true;
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
