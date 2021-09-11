package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Interface extends JPanel implements MouseListener {
    Dimension size = new Dimension(700,600);
    int selected = 0; //0 = move; 1 = transistor; 2 = vcc; 3 = gnd
    public Interface(){

        setPreferredSize(size);
        setFocusable(true);
    }

    /**
     * Add a transistor to the pnanel
     */
    public void addTransistor(){
        System.out.println("adding a transistor");

    }
    /**
     * Add a Vcc plug to the pnanel
     */
    public void addVcc(){
        System.out.println("it is vcc");
    }
    public void select(){
        System.out.println("it is vcc");
    }
    @Override
    public void mouseClicked(MouseEvent e) {
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
