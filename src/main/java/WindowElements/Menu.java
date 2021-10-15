package WindowElements;


import Components.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener {
    Components.Interface Interface;
    public Menu(Interface inter){
        setBackground(Color.white);

        this.Interface = inter;
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save Project");
        save.addActionListener(this);
        JMenuItem open= new JMenuItem("Open Project");
        open.addActionListener(this);
        JMenuItem removeAll= new JMenuItem("new Project");//TODO implementare la classe
        removeAll.addActionListener(this);
        file.add(save);
        file.add(open);
        add(file);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Save Project"-> Interface.saveObjects();
            case "Open Project"-> Interface.readObject();
        }
    }
}
