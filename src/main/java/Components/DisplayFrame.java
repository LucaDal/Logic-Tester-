package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class DisplayFrame extends JFrame implements ActionListener {
    Interface Interface = new Interface();
    JButton transistor, vcc, select, gnd, delete,switchButton;

    public DisplayFrame() {
        super("Logic Tester");
        setLayout(new BorderLayout());
        transistor = new JButton("Transistor");//TODO: implementig the selection of npn or pnp transistor
        vcc = new JButton("Vcc");
        select = new JButton("Select");
        gnd = new JButton("GND");
        delete = new JButton("Delete");
        switchButton = new JButton("Switch");
        transistor.addActionListener(this);
        vcc.addActionListener(this);
        select.addActionListener(this);
        gnd.addActionListener(this);
        delete.addActionListener(this);
        switchButton.addActionListener(this);

        /* inserirsco Interface bottoni nel Jpanel */
        JPanel j1 = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        j1.add(select, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.3;
        c.gridx = 1;
        c.gridy = 0;
        j1.add(transistor, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        j1.add(vcc, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        c.gridy = 0;
        j1.add(gnd, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 4;
        c.gridy = 0;
        j1.add(delete, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 5;
        c.gridy = 0;
        j1.add(switchButton, c);

        /*key bindigs*/
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());

        add(j1, BorderLayout.NORTH);
        add(Interface, BorderLayout.CENTER);
        pack();
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Transistor" -> Interface.addTransistor();
            case "Vcc" -> Interface.addVcc();
            case "Select" -> Interface.select();
            case "GND" -> Interface.addGnd();
            case "Delete" -> Interface.readObject();
            case "Switch" -> Interface.saveObjects();//TODO change and crerate a manu
        }
    }

    private class MyDispatcher implements KeyEventDispatcher {//TODO implementing every button
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Interface.resetAll();
            }
            if (e.getKeyCode() == KeyEvent.VK_1) {
                Interface.select();
            }
            if (e.getKeyCode() == KeyEvent.VK_2) {
                Interface.addTransistor();
            }
            if (e.getKeyCode() == KeyEvent.VK_3) {
                Interface.addVcc();
            }
            if (e.getKeyCode() == KeyEvent.VK_4) {
                Interface.addGnd();
            }
            if (e.getKeyCode() == KeyEvent.VK_5) {
                Interface.delete();
            }
            return false;
        }
    }
}
