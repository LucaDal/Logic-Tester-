package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Interface extends JPanel implements MouseListener, MouseMotionListener {
    Dimension size = new Dimension(900,600);
    int selected = 0; //0 = move; 1 = transistor; 2 = vcc; 3 = gnd
    HashMap<Integer,Component> componentMap = new HashMap<>();
    int ID = 1;
    boolean transistorIsSetted = true,vccIsSetted = true,gndIsSetted = true;
    public Interface(){

        setPreferredSize(size);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     *  if called this function will set the parameter transistorIsSetted to true
     *  and then after clicking on the jPanel it will rapresent the image
     */
    public void addTransistor(){ //TODO change the way of implementing a system of transistor if more then one
        transistorIsSetted = false;
    }
    /**
     * Add a Vcc plug to the pnanel
     */
    public void addVcc(){

        System.out.println("it is vcc");
    }
    public void select(){

        System.out.println("it is select");
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(Component c : componentMap.values()){
            c.paint(g);
        }
    }

    /**
     * used to evaluate if a coordinate is located between a specified segment
     *
     * @param coordinateMouse - insert X or Y coordinate from the Mouse
     * @param coordinateComponent - insert X or Y coordinate from the origin of the image to evaluate
     * @param size - the width or the height to add to coordinateComponenet
     * @return true if between the coordinates, false otherwise
     */
    public boolean isBetween(int coordinateMouse, int coordinateComponent,int size){
        if (coordinateMouse >= coordinateComponent && coordinateMouse <= (coordinateComponent + size)){
            return true;
        }
        return false;
    }

    /**
     * This function is used to say if the click of the mouse is over a component or not.
     * if so, it will return the componentID (the area evaluated is rectangular)
     * @param mousePosition - the point which indicate the coordinate of the click of the Mouse
     * @return the ID of the component clicked. if 0 then no component is checked
     */
    public int check(Point mousePosition){
        for (Component c : componentMap.values()){
//            System.out.println("Posizione origine elemento: X =" + c.getPosition().x + " Y =" + c.getPosition().y + "size ="+c.getSizeWidth());
            if ( isBetween(mousePosition.x,c.getPosition().x,c.getSizeWidth()) && isBetween(mousePosition.y,c.getPosition().y,c.getSizeHeight())){
                return c.getID();
            }
        }
        return 0;
    }



    @Override
    public void mouseDragged(MouseEvent e) {
/*        System.out.println("X =" + e.getX() + " Y =" + e.getY());
        int ID =check(new Point(e.getX(),e.getY()));
        System.out.println("ID tornato: "+ID);*/
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("X =" + e.getX() + " Y =" + e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!transistorIsSetted){
            System.out.println("adding a transistor n :" + ID);
            componentMap.put(ID,new Transistor(this,ID,e.getX(),e.getY(),30,30));
            transistorIsSetted = true;
            ID++;
        }
        if(!vccIsSetted){
            System.out.println("adding a Vcc plug");
        }
        int IdReturned =check(new Point(e.getX(),e.getY()));

        System.out.println("ID tornato: "+IdReturned + '\n');

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

}
