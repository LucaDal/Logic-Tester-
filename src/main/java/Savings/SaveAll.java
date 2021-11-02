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

/**
 * used knowing the absolut path where to save the obj
 */
public class SaveAll {
    public static void saveObjectsWithPath(JPanel parent, HashMap<Integer, Component> componentMap, HashMap<Line, ArrayList<Integer>> lines, boolean savedInGrid, String path) {

        try (FileOutputStream fos = new FileOutputStream(path)) {
            ObjectOutputStream ow = new ObjectOutputStream(fos);

            ow.writeObject(componentMap);
            ow.writeObject(lines);
            ow.writeBoolean(savedInGrid);

            ow.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Error saving file.");
            e.printStackTrace();
        }

    }
}
