package WindowElements;


import Components.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener {
    private final Components.Interface Interface;
    private JPanel parent;
    public Menu(Interface inter,JPanel parent){
        setBackground(Color.white);

        this.Interface = inter;
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save Project");
        save.addActionListener(this);
        JMenuItem open= new JMenuItem("Open Project");
        open.addActionListener(this);
        JMenuItem removeAll= new JMenuItem("new Project");//TODO implementare la classe
        removeAll.addActionListener(this);

        JMenu option = new JMenu("Option");
        JMenuItem clearPanel = new JMenuItem("Clear Panel");
        clearPanel.addActionListener(this);
        file.add(save);
        file.add(open);
        option.add(clearPanel);
        add(file);
        add(option);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Save Project"-> Interface.saveObjects();
            case "Open Project"-> Interface.readObject();
            case "Clear Panel" -> {
                Object [] options = {"Save and delete","Delete","Cancel"};
                int returnedValue = JOptionPane.showOptionDialog(parent,"Everything not saved will be lost!","Attention",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null,options,options[1]);
                if (returnedValue == 1){
                    Interface.clearPanel();
                }
                if (returnedValue == 0){
                    Interface.saveObjects();
                    Interface.clearPanel();
                }
            }
        }
    }
}
