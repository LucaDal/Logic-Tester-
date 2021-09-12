package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Interface extends JPanel implements MouseListener, MouseMotionListener {
    Dimension size = new Dimension(700,600);
    int selected = 0; //0 = move; 1 = transistor; 2 = vcc; 3 = gnd
    HashMap<Integer,Component> transitorMap = new HashMap<>();
    int numberOfTransistor = 0;

    public Interface(){

        setPreferredSize(size);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Add a transistor to the pnanel
     */
    public void addTransistor(){
        System.out.println("adding a transistor");
        transitorMap.put(numberOfTransistor,new Transistor(this,450,300));
        numberOfTransistor++;
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
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("premuto");
    }

    public void paintComponent(Graphics g){

        for(Component c : transitorMap.values()){
            c.paint(g);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) { }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("X =" + e.getX() + " Y =" + e.getY());
    }
}
