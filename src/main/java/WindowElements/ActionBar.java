package WindowElements;

import javax.swing.*;
import java.awt.*;

public class ActionBar extends JPanel {
    String toPrint = "";
    Integer numComponent = 0;
    String nameOfTheProject = "";

    public ActionBar() {
        setBackground(Color.white);
        setPreferredSize(new Dimension(900, 12));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int spaceAdjust = numComponent.toString().length()*8;
        g.drawString(toPrint,7,10);
        g.drawString(numComponent.toString(),getWidth()-3 - spaceAdjust,10);
        g.drawString(nameOfTheProject,getWidth()/2-(nameOfTheProject.length()*8)/2,10);
    }

    public void setNameOfTheProject(String nameOfTheProject) {
        this.nameOfTheProject = nameOfTheProject;
        repaint();
    }

    public void setStringToPrint(String toPrint){
        this.toPrint = toPrint;
        repaint();
    }
    public void setNumberComponent(int numberComponent){
        this.numComponent = numberComponent;
        repaint();
    }
}
