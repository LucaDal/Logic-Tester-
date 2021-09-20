package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Interface extends JPanel implements MouseListener, MouseMotionListener {
    Dimension size = new Dimension(900, 700);
    HashMap<Integer, Component> componentMap = new HashMap<>();
    HashMap<Line, ArrayList<Integer>> lines = new HashMap<>();

    int IDComponent = 1, pinA = 3, pinB = 2, pinC = 9;
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
        for (ArrayList<Integer> l : lines.values()) {
            g.setColor(Color.black);
            g.drawLine(l.get(0), l.get(1), l.get(2), l.get(3));
        }
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
        return coordinateMouse >= coordinateComponent && coordinateMouse <= (coordinateComponent + size);
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
            componentMap.put(IDComponent, new Vcc(this, IDComponent, e.getX(), e.getY(), 15, 15));
            vccToSet = false;
            IDComponent++;
        }
        if (gndToSet) {
            System.out.println("adding a gnd plug");
            componentMap.put(IDComponent, new Gnd(this, IDComponent, e.getX(), e.getY(), 25, 25));
            gndToSet = false;
            IDComponent++;
        }
        if (deleteIsSelected) { //se l'ID tornato ( ovvero l'oggetto da eliminare è Vcc ) allora è possibile settare il pin to false
            int IdReturned = checkMouseOverComponent(e);
            if (IdReturned != 0 && IdReturned <= IDComponent) {
                boolean ReturnedVcc = false;
                if (componentMap.get(IdReturned).getType().equalsIgnoreCase("vcc")) {
                    ReturnedVcc = true;
                }
                /* delete lines between components */
                Iterator<Map.Entry<Line, ArrayList<Integer>>> iter = lines.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<Line, ArrayList<Integer>> entry = iter.next();
                    Line key = entry.getKey();
                    if (key.contain(IdReturned)) {
                        iter.remove();
                    }
                }
                for (Component c : componentMap.values()) {
                    //eseguo il ciclo per ogni tranistor
                    if (c.getType().equalsIgnoreCase("transistor")) {
                        /*deleting the lines*/
                        if (!(c.getIDComponent() == IdReturned)) { // TODO avendo tre pin devo controllare se tutti e tre i pin sono connessi allo stesso componente
                            for (int j = 0; j < 3; j++) { // verifico su tutti i pin
                                Point IDPin = new Point(c.resetIfCointained(IdReturned));//if does it delete the connection
                                if (IDPin.y != 0) {
                                    if (ReturnedVcc) {
                                        c.setState(IDPin.y, false); // da modificare
                                        c.update();
                                    } else {//verifico che non ci siano vcc collegati ad A or C
                                        updateState(); // dopo aver eliminato tutto
                                    }
                                }
                            }
                        }
                    }
                }
                ReturnedVcc = false;
                componentMap.remove(IdReturned);
            }
        }
        if (selectIsSelected) {
            int IdReturned = checkMouseOverComponent(e);
            //System.out.println("IDComponent tornato: " + IdReturned);
            if (IdReturned != 0) {
                if (!setConnection) {
                    if ((tempConnectionPointFirstCall = componentMap.get(IdReturned).inputTarget(e.getX(), e.getY())).y < 10) {//10 is an invalid pin( valid are 2,3,9)
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
                updateState();
            }
        }
        repaint();
    }

    private void updateState() {

        for(Component c : componentMap.values()){

        }

    }
    @SuppressWarnings("unchecked")
    public void updateLine(Point firstComponent, Point secondComponent) {
        //inserisco i dati per disegnare
        ArrayList<Integer> linesCooridnate = new <Integer>ArrayList();

        if (componentMap.get(firstComponent.x).getType().equalsIgnoreCase("transistor")) {
            if (firstComponent.y == pinA) {
                linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().x + componentMap.get(firstComponent.x).getSizeWidth() - 5);
                linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().y + componentMap.get(firstComponent.x).getSizeHeight() - 25);
            }
            if (firstComponent.y == pinB) {
                linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().x + componentMap.get(firstComponent.x).getSizeWidth() - 25);
                linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().y + componentMap.get(firstComponent.x).getSizeHeight() - 15);
            }
            if (firstComponent.y == pinC) {
                linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().x + componentMap.get(firstComponent.x).getSizeWidth() - 5);
                linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().y + componentMap.get(firstComponent.x).getSizeHeight() - 5);
            }
        } else {
            linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().x + componentMap.get(firstComponent.x).getSizeWidth() / 2);
            linesCooridnate.add(componentMap.get(firstComponent.x).getPosition().y + componentMap.get(firstComponent.x).getSizeHeight() / 2);
        }

        if (componentMap.get(secondComponent.x).getType().equalsIgnoreCase("transistor")) {
            if (secondComponent.y == pinA) {
                linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().x + componentMap.get(secondComponent.x).getSizeWidth() - 5);
                linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().y + componentMap.get(secondComponent.x).getSizeHeight() - 25);
            }
            if (secondComponent.y == pinB) {
                linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().x + componentMap.get(secondComponent.x).getSizeWidth() - 25);
                linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().y + componentMap.get(secondComponent.x).getSizeHeight() - 15);
            }
            if (secondComponent.y == pinC) {
                linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().x + componentMap.get(secondComponent.x).getSizeWidth() - 5);
                linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().y + componentMap.get(secondComponent.x).getSizeHeight() - 5);
            }
        } else {
            linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().x + componentMap.get(secondComponent.x).getSizeWidth() / 2);
            linesCooridnate.add(componentMap.get(secondComponent.x).getPosition().y + componentMap.get(secondComponent.x).getSizeHeight() / 2);
        }
        Line coordinates = new Line(firstComponent.x, firstComponent.y, secondComponent.x, secondComponent.y);
        lines.put(coordinates, linesCooridnate);
        //linesUpdated = true;

    }

    /**
     *
     * @param IDPin X = ID; Y = Pin
     */
    public void recursiveUpdate(Point IDPin){
        for(Component c : componentMap.values()){

        }
    }

    public void update(Point firstComponent, Point secondComponent) {

        updateLine(firstComponent, secondComponent);

        //.x = ID ; .y = pin
        if (!componentMap.get(firstComponent.x).getType().equalsIgnoreCase("transistor")) {
            componentMap.get(secondComponent.x).setState(secondComponent.y, true);
            componentMap.get(secondComponent.x).setConnection(new Point(firstComponent.x, secondComponent.y));
            componentMap.get(secondComponent.x).update();
            recursiveUpdate(secondComponent);
            return;
        }
        if (!componentMap.get(secondComponent.x).getType().equalsIgnoreCase("transistor")) {
            componentMap.get(firstComponent.x).setState(firstComponent.y, true);
            componentMap.get(firstComponent.x).setConnection(new Point(secondComponent.x, firstComponent.y));
            componentMap.get(firstComponent.x).update();
            return;
        }
        if (componentMap.get(firstComponent.x).getState(firstComponent.y)) {
            componentMap.get(secondComponent.x).setState(secondComponent.y, true);
            componentMap.get(secondComponent.x).update();

        }
        if (componentMap.get(secondComponent.x).getState(secondComponent.y)) {
            componentMap.get(firstComponent.x).setState(firstComponent.y, true);
            componentMap.get(firstComponent.x).update();
        }
        /*aggiorno i pin per entrambi i transistor(dicendogli con quale ID sono collegati)*/
        componentMap.get(firstComponent.x).setConnection(new Point(secondComponent.x, firstComponent.y));
        componentMap.get(secondComponent.x).setConnection(new Point(firstComponent.x, secondComponent.y));
        //aggiorno gli stati per entrambi
        componentMap.get(firstComponent.x).update();
        componentMap.get(secondComponent.x).update();


    }

    public void delete() {
        deleteIsSelected = true;
        selectIsSelected = false;
        gndToSet = false;
        vccToSet = false;
        transistorToSet = false;
    }

    /**
     * if called this function will set the parameter transistorToSet to true
     * and then after clicking on the jPanel it will rapresent the image
     */
    public void addTransistor() { //TODO change the way of implementing a system of transistor if more then one
        selectIsSelected = false;
        deleteIsSelected = false;
        transistorToSet = true;
        vccToSet = false;
        gndToSet = false;
    }

    public void addVcc() {
        selectIsSelected = false;
        deleteIsSelected = false;
        gndToSet = false;
        transistorToSet = false;
        vccToSet = true;
    }

    public void addGnd() {
        selectIsSelected = false;
        deleteIsSelected = false;
        vccToSet = false;
        transistorToSet = false;
        gndToSet = true;
    }

    public void select() {
        gndToSet = false;
        vccToSet = false;
        transistorToSet = false;
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

    public void resetAll() {
        select();
        /* Ripristino la connessione a false in caso fosse stata messa a true */
        setConnection = false;
    }
}
