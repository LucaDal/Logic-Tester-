package Components;

import java.awt.*;

public class Lines {
    int id1, id2, pin1, pin2;

    public Lines(int id1, int id2, int pin1, int pin2) {
        this.id1 = id1;
        this.id2 = id2;
        this.pin1 = pin1;
        this.pin2 = pin2;
    }

    /**
     * @param id the id of the component; 1 or 2
     * @return a point containing infromation about the point
     */
    public Point getIdPin(int id) {
        if (id == 1) {
            return new Point(id1, pin2);
        }
        return new Point(id2, pin2);
    }
}
