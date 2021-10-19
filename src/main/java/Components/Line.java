package Components;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class Line implements Serializable{
    @Serial
    private static final long serialVersionUID = 5799091444127658990L;
    private int id1 = 0, id2 = 0, pin1 = 0, pin2 = 0;

    public Line(int id1, int pin1, int id2, int pin2) {
        this.id1 = id1;
        this.id2 = id2;
        this.pin1 = pin1;
        this.pin2 = pin2;
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public int getPin1() {
        return pin1;
    }

    public int getPin2() {
        return pin2;
    }

    /**
     * @param id the id of the component; 1 or 2
     * @return a point containing infromation about the point
     */
    public boolean contain(int id) {
        if (this.id1 == id) {
            return true;
        }
        return this.id2 == id;
    }
    public boolean contain(int id, int pin){
        if (id == this.id1){
            return pin == pin1;
        }
        if (id == this.id2){
            return pin == pin2;
        }
        return false;
    }
    public boolean containForID(int id1, int id2){
        if (id1 == this.id1 && id2 == this.id2){
            return true;
        }
        return false;
    }
}
