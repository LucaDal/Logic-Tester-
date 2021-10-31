package Savings;

import Components.Component;
import Components.Line;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class WriteObjects {
    JPanel parent;

    public WriteObjects(JPanel parent){
        this.parent = parent;
    }
    /**
     * Saves componentMap HashMap and Lines HashMap
     *
     * to get Lines Hashmap call readLines();
     */
    public void saveObjects(HashMap<Integer, Component> componentMap, HashMap<Line, ArrayList<Integer>> lines,boolean savedInGrid) {
        JFileChooser save = new JFileChooser();
        save.setDialogTitle("Saving Project");
        save.setSelectedFile(new File(".bin"));
        save.setApproveButtonText("save");
        save.setFileFilter(new FileNameExtensionFilter("bin file","bin"));
        int option = save.showOpenDialog(parent);
        if (option == APPROVE_OPTION) {

            try (FileOutputStream fos = new FileOutputStream(save.getSelectedFile())) {
                ObjectOutputStream ow = new ObjectOutputStream(fos);

                    ow.writeObject(componentMap);
                    ow.writeObject(lines);
                    ow.writeBoolean(savedInGrid);

                ow.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent,"Error saving file.");
                e.printStackTrace();
            }
        }
    }
}
