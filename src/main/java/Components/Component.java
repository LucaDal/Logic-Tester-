package Components;
import javax.swing.*;
import java.awt.*;

public interface Component {
    void paint(Graphics g);
    void setX(int x);
    void setY(int y);
    Point getPosition();
    int getSizeWidth();
    int getSizeHeight();
    int getID();
}
