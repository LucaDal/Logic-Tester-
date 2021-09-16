package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Vcc implements Component {
    final String type = "vcc";
    Image img;
    int sizeWidth, sizeHeight, x, y, ID;
    JPanel parent;

    public Vcc(JPanel parent, int ID, int x, int y, int sizeWidth, int sizeHeight) {
        this.parent = parent;
        this.ID = ID;
        this.x = x - sizeWidth / 2;
        this.y = y - sizeHeight / 2;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;

        BufferedImage imgb = null;
        try {
            imgb = ImageIO.read(new File("C:\\Users\\Luca\\Documents\\Projects\\LogicTesterV1\\src\\main\\resources\\vcc.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        img = imgb.getScaledInstance(sizeWidth, sizeHeight, Image.SCALE_SMOOTH);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public int getSizeWidth() {
        return sizeWidth;
    }

    public int getSizeHeight() {
        return sizeHeight;
    }

    public int getIDComponent() {
        return ID;
    }

    public void connect() {

    }

    public void paint(Graphics g) {
        g.drawImage(img, x, y, parent);
    }
}
