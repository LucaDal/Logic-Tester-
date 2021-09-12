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
    Image img1;
    int width = 40, height = 40, x,y;
    JPanel parent;

    public Transistor(JPanel parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;

        BufferedImage img1b = null;
        try {
            img1b = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\npn.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        img1 = img1b.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public void paint(Graphics g){
        g.drawImage(img1,x,y,parent);
    }

    @Override
    public Point position() {
        return null;
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
