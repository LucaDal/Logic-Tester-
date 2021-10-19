package Savings;

import Components.Component;
import Components.Line;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ReadObjects {
    JPanel parent;
    HashMap<Integer, Component> componentMap;
    JFileChooser open = new JFileChooser();
    int option;

    HashMap<Integer, Component> components = null;
    HashMap<Line, ArrayList<Integer>> lines = null;

    public ReadObjects(JPanel parent) {
        this.parent = parent;
        this.open.setDialogTitle("Reading Project");
        this.open.setApproveButtonText("Open");
        this.open.setFileFilter(new FileNameExtensionFilter("bin file", "bin"));
        this.option = open.showOpenDialog(parent);
    }

    /**
     * Reads componentMap HashMap and Lines HashMap
     *
     * to get Lines Hashmap call readLines();
     * @return componentMap
     */
    @SuppressWarnings("unchecked")
    public HashMap<Integer, Component> readComponents() {

        if (option == JFileChooser.APPROVE_OPTION) {
            System.out.println("file scelto :" + open.getSelectedFile());
            try (FileInputStream stream = new FileInputStream(open.getSelectedFile())) {

                ObjectInputStream ois = new ObjectInputStream(stream);
                components = (HashMap<Integer, Component>) ois.readObject();
                lines = (HashMap<Line, ArrayList<Integer>>) ois.readObject();
                ois.close();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent,"Error loading file.");
            }
        }
        return components;
    }

    public HashMap<Line, ArrayList<Integer>> readLines() {
        return lines;
    }
}
