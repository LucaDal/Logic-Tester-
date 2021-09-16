package Components;
import javax.swing.*;
import java.awt.*;

public interface Component {
    final String type = "";
    void paint(Graphics g);
    void setX(int x);
    void setY(int y);
    Point getPosition();
    int getSizeWidth();
    int getSizeHeight();
    int getIDComponent();
    void connect();
}
