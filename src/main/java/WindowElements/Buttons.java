package WindowElements;

import Components.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buttons extends JPanel implements ActionListener {
    JButton transistor, vcc, select, gnd, delete, switchButton,debug, bitDisplay;
    Components.Interface Interface;
    int width = 60, height = 40;

    public Buttons(Interface inter) {
        setBackground(Color.GRAY);//TODO remove comments when implewmentinh images
        setLayout(new GridBagLayout());
        this.Interface = inter;

        transistor = new JButton("Transistor");//TODO: implementig the selection of npn or pnp transistor
        transistor.setPreferredSize(new Dimension(width, height));
        transistor.setContentAreaFilled(false);

        vcc = new JButton("Vcc");
        vcc.setPreferredSize(new Dimension(width, height));
        vcc.setContentAreaFilled(false);
        vcc.setFocusPainted(false);
        //vcc.setBorderPainted(false);

        select = new JButton("Select");
        select.setPreferredSize(new Dimension(width, height));
        select.setContentAreaFilled(false);

        gnd = new JButton("GND");
        gnd.setPreferredSize(new Dimension(width, height));
        gnd.setContentAreaFilled(false);

        delete = new JButton("Delete");
        delete.setPreferredSize(new Dimension(width, height));
        delete.setContentAreaFilled(false);

        switchButton = new JButton("Switch");
        switchButton.setPreferredSize(new Dimension(width, height));
        switchButton.setContentAreaFilled(false);

        debug = new JButton("Debug");
        debug.setPreferredSize(new Dimension(width, height));
        debug.setContentAreaFilled(false);

        bitDisplay = new JButton("BitDisplay");
        bitDisplay.setPreferredSize(new Dimension(width, height));
        bitDisplay.setContentAreaFilled(false);

        debug = new JButton("Debug");
        debug.setPreferredSize(new Dimension(width, height));
        debug.setContentAreaFilled(false);
        //===================================================
        transistor.addActionListener(this);
        vcc.addActionListener(this);
        select.addActionListener(this);
        gnd.addActionListener(this);
        delete.addActionListener(this);
        switchButton.addActionListener(this);
        bitDisplay.addActionListener(this);
        debug.addActionListener(this);

        /* inserirsco Interface bottoni nel Jpanel */
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        add(select, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        add(transistor, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 2;
        add(vcc, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 3;
        add(gnd, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 4;
        add(delete, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 5;
        add(switchButton, c);
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 6;
        //c.weighty = 1;//TODO riattiva dopo che elimini il debug
        add(bitDisplay, c);
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 7;
        c.weighty = 1;
        add(debug, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Transistor" -> Interface.addTransistor();
            case "Vcc" -> Interface.addVcc();
            case "Select" -> Interface.select();
            case "GND" -> Interface.addGnd();
            case "Delete" -> Interface.delete();
            case "Switch" -> Interface.addSwitch();
            case "BitDisplay" -> Interface.addBitDisplay();
            case "Debug" -> Interface.debug();
        }
    }

}
