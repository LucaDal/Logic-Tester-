package Components;

import java.awt.*;
import java.io.Serializable;

public class Line implements Serializable{
    int id1, id2, pin1, pin2;


    public Line(int id1, int pin1, int id2, int pin2) {
        this.id1 = id1;
        this.id2 = id2;
        this.pin1 = pin1;
        this.pin2 = pin2;
    }

    /**
     * @param id the id of the component; 1 or 2
     * @return a point containing infromation about the point
     */
    public boolean contain(int id) {
        if (this.id1 == id) {
            return true;
        }
        if (this.id2 == id) {
            return true;
        }
        return false;
    }
}
