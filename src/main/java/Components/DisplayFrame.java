package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayFrame extends JFrame implements ActionListener {
    Interface Interface = new Interface();
    JButton transistor, vcc, select, gnd, delete;

    public DisplayFrame() {
        super("Logic Tester");
        setLayout(new BorderLayout());
        transistor = new JButton("Transistor");//TODO: implementig the selection of npn or pnp transistor
        vcc = new JButton("Vcc");
        select = new JButton("Select");
        gnd = new JButton("GND");
        delete = new JButton("Delete");
        transistor.addActionListener(this);
        vcc.addActionListener(this);
        select.addActionListener(this);
        gnd.addActionListener(this);
        delete.addActionListener(this);

        //----- inserirsco Interface bottoni nel Jpanel
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
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Transistor":
                Interface.addTransistor();
                break;
            case "Vcc":
                Interface.addVcc();
                break;
            case "Select":
                Interface.select();
                break;
            case "Delete":
                Interface.delete();
                break;
            case "GND":
                Interface.addGnd();
                break;
        }
    }
}
