package WindowElements;


import Components.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener {
    private final Components.Interface Interface;
    private final JPanel parent;
    JMenuItem grid,save,open,removeAll,clearPanel,boxForText;
    JMenu file,option;
    public Menu(Interface inter,JPanel parent){
        setBackground(Color.white);
        this.parent = parent;
        this.Interface = inter;
        grid = new JMenuItem("Turn Grid: OFF");
        file = new JMenu("File");
        save = new JMenuItem("Save Project");
        boxForText = new JMenu("");
        boxForText.setFocusPainted(false);
        boxForText.setFont(new Font("Courier New", Font.PLAIN, 12));
        save.addActionListener(this);
        open= new JMenuItem("Open Project");
        open.addActionListener(this);
        removeAll= new JMenuItem("new Project");//TODO implementare la classe
        removeAll.addActionListener(this);
        option = new JMenu("Options");
        clearPanel = new JMenuItem("Clear Panel");
        clearPanel.addActionListener(this);
        grid.addActionListener(this);
        file.add(save);
        file.add(open);
        option.add(clearPanel);
        add(file);
        add(option);
        add(boxForText);
        option.add(grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (save.equals(source)) {
            Interface.saveObjects();
        } else if (open.equals(source)) {
            Interface.readObject();
        } else if (clearPanel.equals(source)) {
            Object[] options = {"Save and clear panel", "Clear panel", "Cancel"};
            int returnedValue = JOptionPane.showOptionDialog(parent, "Everything not saved will be lost!", "Attention", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
            if (returnedValue == 1) {
                Interface.clearPanel();
            }
            if (returnedValue == 0) {
                Interface.saveObjects();
                Interface.clearPanel();
            }
        } else if (grid.equals(source)) {
            if (Interface.useGrid()) {
                grid.setText("Turn Grid: OFF");
            }else{
                grid.setText("Turn Grid: ON");
            }
        }
    }

    public void setGridOn(){
        grid.setText("Turn Grid: ON");
    }
    public void setBoxText(String text){
        boxForText.setText("Project: "+ text);
    }
}
