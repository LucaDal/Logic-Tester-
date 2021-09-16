package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Transistor implements MouseListener, Component{
    boolean A = false, B = false, C = false;
    final String type = "transistor";
    Image img1;
    int ID;
    int sizeWidth, sizeHeight, x,y;
    JPanel parent;

    public Transistor(JPanel parent,int ID, int x, int y,int sizeWidth,int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth/2;
        this.y = y - sizeHeight/2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;

        BufferedImage img1b = null;
        try {
            img1b = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npn.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        img1 = img1b.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public Point getPosition(){
        return new Point(x ,y);
    }

    @Override
    public int getSizeWidth() {
        return sizeWidth;
    }

    @Override
    public int getSizeHeight() {
        return sizeHeight;
    }

    @Override
    public int getID() {
        return ID;
    }

    public void paint(Graphics g){
        g.drawImage(img1,x,y,parent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
