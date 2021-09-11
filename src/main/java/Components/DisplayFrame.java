package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayFrame extends JFrame implements ActionListener {
    Interface I = new Interface();
    JButton transitor,vcc,select,gnd;
    public DisplayFrame(){
        super("Logic Tester");
        setLayout(new BorderLayout());
        transitor = new JButton("Transistor");
        vcc = new JButton("Vcc");
        select = new JButton("Select");
        gnd = new JButton("GND");
        transitor.addActionListener(this);
        vcc.addActionListener(this);

        //----- inserirsco i bottoni nel Jpanel
        JPanel j1 = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        j1.add(select,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        j1.add(transitor,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        j1.add(vcc,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        c.gridy = 0;
        j1.add(gnd,c);

        add(j1,BorderLayout.NORTH);
        add(I,BorderLayout.CENTER);
        pack();
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch(e.getActionCommand()){
            case "transistor":
                I.addTransistor();
                break;
            case "Vcc":
                I.addVcc();
                break;
            case "select":
                I.select();
                break;
        }
    }
}
